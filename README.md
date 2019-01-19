自己分析的插件化管理机制，免安装运行是在[intercept-activity][9]中，主要解决了资源的插件化问题。


# understand-plugin-framework
分析[DroidPlugin][1]，深入理解插件化框架，内容如下：

- [Hook机制之动态代理][2]
- [Hook机制之Binder Hook][3]
- [Hook机制之AMS&PMS][4]
- [Activity生命周期管理][5]
- [插件加载机制][6]
- [广播的处理方式][7]
- [Service的管理][8]
- ContentProvider的管理
- 插件加载解析之自定义包管理服务(PackageManager)
- 插件进程管理机制（ActivityMAnager）
- 插件机制之资源管理
- DroidPlugin插件通信机制
- DroidPlugin框架缺陷
- 不同插件框架方案对比
- 插件化的未来

[1]: https://github.com/DroidPluginTeam/DroidPlugin
[2]: http://weishu.me/2016/01/28/understand-plugin-framework-proxy-hook/
[3]: http://weishu.me/2016/02/16/understand-plugin-framework-binder-hook/
[4]: http://weishu.me/2016/03/07/understand-plugin-framework-ams-pms-hook/
[5]: http://weishu.me/2016/03/21/understand-plugin-framework-activity-management/
[6]: http://weishu.me/2016/04/05/understand-plugin-framework-classloader/
[7]: http://weishu.me/2016/04/12/understand-plugin-framework-receiver/
[8]: http://weishu.me/2016/05/11/understand-plugin-framework-service/
[9]: https://github.com/TmacZhang/virtualapp_jin/tree/master/intercept-activity
