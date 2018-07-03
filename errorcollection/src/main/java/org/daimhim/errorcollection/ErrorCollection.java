package org.daimhim.errorcollection;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.ErrorListener;

/**
 * 项目名称：org.daimhim.errorcollection
 * 项目版本：vision
 * 创建时间：2018.06.30 10:43
 * 修改人：Daimhim
 * 修改时间：2018.06.30 10:43
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class ErrorCollection implements Thread.UncaughtExceptionHandler {
    private Config mConfig;
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (null!=mConfig.getListener()) {
            mConfig.getListener().errorBefore(t, e);
        }
        String lS = saveErrorMessages(t, e);
        if (null!=mConfig.getListener()){
            mConfig.getListener().errorAfter(lS);
        }
    }

    private static class SingletonHolder {
        private static final ErrorCollection INSTANCE = new ErrorCollection();
    }

    private ErrorCollection() {
    }

    public static ErrorCollection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application pApplication,Config config) {
        if (null == config){
            mConfig = new Config();
        }else {
            mConfig = config;
        }
        //初始化log保存日志
        if (TextUtils.isEmpty(mConfig.getCachePath())) {
            mConfig.setCachePath(getSystemFilePath(pApplication) + "/crash/");
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private String saveErrorMessages(Thread t,Throwable e) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String fileName = "error-" + time + "-" + System.currentTimeMillis() + ".log";
        String lSystemFilePath = mConfig.cachePath;
        File dir = new File(lSystemFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(lSystemFilePath + fileName);
            fos.write(getTrace(e).getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return lSystemFilePath + fileName;
    }

    public String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    private String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static class Config{
        private String cachePath;
        private ErrorListener listener;

        public String getCachePath() {
            return cachePath;
        }

        public void setCachePath(String cachePath) {
            this.cachePath = cachePath;
        }

        public ErrorListener getListener() {
            return listener;
        }

        public void setListener(ErrorListener listener) {
            this.listener = listener;
        }
    }

    public interface ErrorListener{
        void errorBefore(Thread t, Throwable e);
        void errorAfter(String filePath);
    }
}
