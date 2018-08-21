# 全局异常捕捉接入指南
##### 项目简介
> 项目实现了App崩溃后捕捉异常，并提供了异常后保存以及回调，供调用者做保存和后续处理
##### 接入指南
###### 1. 依赖

```
//根目录的build添加仓库地址
repositories {
        maven { url 'https://dl.bintray.com/daimhim/widget/'}
    }

//在模块build下添加
dependencies {
        compile 'org.daimhim.errorcollection:errorcollection:1.0.2'
}
```
###### 2. 初始化

```
//你需要在项目的Application中添加以下代码
private void initErrorCollection() {
        ErrorCollection.Config config = new ErrorCollection.Config();
        config.setListener(new ErrorCollection.ErrorListener() {
            @Override
            public void errorBefore(Thread t, Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void errorAfter(Uri filePath) {
                Timber.d(filePath.toString());
                //启动页
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                StartApp.getInstance().startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        ErrorCollection.getInstance().init(this, config);
    }
//在这里初始化，你可以配置收集错误日志的各种参数
// 默认日志储存路径：sdcard/Android/data/包名/cache/crash/
```
# 版本更新说明
##### 1.0.3
> 1.增加连续崩溃处理
##### 1.0.2
> 1. 增加手机信息的收集保存
##### 1.0.0
> 1. 初始化项目
