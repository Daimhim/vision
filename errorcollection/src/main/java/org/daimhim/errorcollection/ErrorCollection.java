package org.daimhim.errorcollection;

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
    private Application mApplication;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        saveErrorMessages(t,e,mApplication);
        Log.d("TAG:ErrorCollection", getTrace(e));
    }

    private static class SingletonHolder {
        private static final ErrorCollection INSTANCE = new ErrorCollection();
    }

    private ErrorCollection() {
    }

    public static ErrorCollection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application pApplication) {
        mApplication = pApplication;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private void saveErrorMessages(Thread t,Throwable e, Context pContext) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String fileName = "error-" + time + "-" + System.currentTimeMillis() + ".log";
        String lSystemFilePath = getSystemFilePath(pContext) + "/crash/";
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
}
