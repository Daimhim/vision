package org.daimhim.vision;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.daimhim.errorcollection.ErrorCollection;

import timber.log.Timber;

/**
 * 项目名称：org.daimhim.vision
 * 项目版本：vision
 * 创建时间：2018.05.30 21:07
 * 修改人：Daimhim
 * 修改时间：2018.05.30 21:07
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class StartApp extends Application {
    private static class SingletonHolder {
        private static final StartApp INSTANCE = new StartApp();
    }

    private StartApp() {
    }

    public static StartApp getInstance() {
        return StartApp.SingletonHolder.INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        ErrorCollection.Config config = new ErrorCollection.Config();
        config.setListener(new ErrorCollection.ErrorListener() {
            @Override
            public void errorBefore(Thread t, Throwable e) {

            }

            @Override
            public void errorAfter(String filePath) {
                //启动页
                Intent intent = new Intent(StartApp.getInstance(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                StartApp.getInstance().startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }

        });
        ErrorCollection.getInstance().init(this, config);
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
