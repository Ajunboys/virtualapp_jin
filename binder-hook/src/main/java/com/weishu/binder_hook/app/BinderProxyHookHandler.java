package com.weishu.binder_hook.app;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 由于ServiceManager里面的sCache里面存储的 IBinder类型基本上都是BinderProxy
 * 因此, ServiceManager的使用者调用getService之后不会直接使用这个map
 * 而是先将他使用asInterface转成需要的接口
 * <p>
 * asInterface函数的代码告诉我们, 它会先使用这个BinderPRoxy查询本进程是否有Binder对象
 * 如果有就使用本地的, 这里恰好就是一个hook点
 * <p>
 * 我们让所有的查询都返回一个"本地Binder"对象
 * <p>
 * 当然,这是一个假象, 我们给它返回的Binder对象自然是符合要求的(要么是本地Binder,要么是Binder代理)
 * 只不过,我们对需要hook的API做了处理
 * <p>
 * 这个类仅仅Hook掉这个关键的 queryLocalInterface 方法
 *
 * @author weishu
 * @date 16/2/15
 */
public class BinderProxyHookHandler implements InvocationHandler {

    private static final String TAG = "BinderProxyHookHandler";

    // 绝大部分情况下,这是一个BinderProxy对象
    // 只有当Service和我们在同一个进程的时候才是Binder本地对象
    // 这个基本不可能
    IBinder base;

    Class<?> stub;

    Class<?> iinterface;

    public BinderProxyHookHandler(IBinder base) {
        this.base = base;
        try {
            this.stub = Class.forName("android.content.IClipboard$Stub");
            this.iinterface = Class.forName("android.content.IClipboard");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("queryLocalInterface".equals(method.getName())) {

            Log.d(TAG, "hook queryLocalInterface");

            // 这里直接返回真正被Hook掉的Service接口
            // 这里的 queryLocalInterface 就不是原本的意思了
            // 我们肯定不会真的返回一个本地接口, 因为我们接管了 asInterface方法的作用
            // 因此必须是一个完整的 asInterface 过的 IInterface对象, 既要处理本地对象,也要处理代理对象
            // 这只是一个Hook点而已, 它原始的含义已经被我们重定义了; 因为我们会永远确保这个方法不返回null
            // 让 IClipboard.Stub.asInterface 永远走到if语句的else分支里面

            /**
             * public static abstract class IClipboard.Stub
             * extends android.os.Binder implements android.content.IClipboard
             *
             * public interface  IClipboard extends android.os.IInterface
             *
             * public class Binder implements IBinder
             *
             * 所以IClipboard.Stub一共实现了三个接口
             * {@# android.content.IClipboard}
             * {@# android.os.IInterface}
             * {@# android.os.IBinder}
             */
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),

                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的
                    new Class[]{IBinder.class, IInterface.class, this.iinterface},
                    new BinderHookHandler(base, stub));
        }

        Log.d(TAG, "method:" + method.getName());
        return method.invoke(base, args);
    }
}
