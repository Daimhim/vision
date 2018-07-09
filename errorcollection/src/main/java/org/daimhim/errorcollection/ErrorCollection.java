package org.daimhim.errorcollection;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

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
public class ErrorCollection  {
    public static final String TAG  = "ErrorCollection";
    private Config mConfig;

    private static class SingletonHolder {
        private static final ErrorCollection INSTANCE = new ErrorCollection();
    }

    private ErrorCollection() {
    }

    public static ErrorCollection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application pApplication, Config config) {
        if (null == config) {
            mConfig = new Config();
        } else {
            mConfig = config;
        }
        //初始化log保存日志
        if (TextUtils.isEmpty(mConfig.getCachePath())) {
            mConfig.setCachePath(getSystemFilePath(pApplication) + "/crash/");
        }
        PackageInfo lPackageInfo = getPackageInfo(pApplication);
        mConfig.setVersionCode(String.valueOf(lPackageInfo.versionCode));
        mConfig.setVersionName(String.valueOf(lPackageInfo.versionName));
        Thread.setDefaultUncaughtExceptionHandler(new ErrorCollectionUncaughtException(mConfig));
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

    public static class Config {
        private String cachePath;
        private ErrorListener listener;
        private String versionName;
        private String versionCode;

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

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String pVersionName) {
            versionName = pVersionName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String pVersionCode) {
            versionCode = pVersionCode;
        }
    }

    public interface ErrorListener {
        /**
         * 保存错误之前
         *
         * @param t 报错线程
         * @param e 错误信息
         */
        void errorBefore(Thread t, Throwable e);

        /**
         * 报错之后
         *
         * @param filePath 文件路径  日志路径
         */
        void errorAfter(Uri filePath);
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    static class PostFile extends AsyncTask<String, Void, Integer> {
        /**
         * @param strings [0] url [1] filepatch [2] in or out [3] requestType
         *                [4] ReadTimeout [5] ConnectTimeout
         * @return
         */
        @Override
        protected Integer doInBackground(String... strings) {
            HttpURLConnection connection = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(strings.length < 4 ? "GET" : strings[3]);
                connection.setReadTimeout(strings.length < 5 ? 5000 : Integer.valueOf(strings[4]));
                connection.setConnectTimeout(strings.length < 6 ? 10000 : Integer.valueOf(strings[5]));
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String lString = strings[2];
                    if ("in".equals(lString)) {
                        in = connection.getInputStream();
                        out = new FileOutputStream(new File(strings[1]));
                    } else {
                        out = connection.getOutputStream();
                        in = new FileInputStream(new File(strings[1]));
                        out.write("file=".getBytes());
                    }
                    byte[] buf = new byte[1024];
                    int ch;
                    while ((ch = in.read(buf)) != -1) {
                        out.write(buf, 0, ch);
                    }
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
                return connection.getResponseCode();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != connection) {
                    connection.disconnect();
                }
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                }
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                }
            }
            return HttpURLConnection.HTTP_NO_CONTENT;
        }

        @Override
        protected void onPostExecute(Integer pInteger) {
            super.onPostExecute(pInteger);
            System.out.println(TAG+"网络请求失败:"+pInteger);
        }
    }


    static class ErrorCollectionUncaughtException implements Thread.UncaughtExceptionHandler{
        Config mConfig;

        public ErrorCollectionUncaughtException(Config pConfig) {
            mConfig = pConfig;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (null != mConfig.getListener()) {
                mConfig.getListener().errorBefore(t, e);
            }
            String lS = saveErrorMessages(t, e);
//        new PostFile().execute("http://192.168.1.83:8080/zsmapi1.12.0/user/savaData",
//                lS,
//                "out",
//                "POST");
            if (null != mConfig.getListener()) {
                mConfig.getListener().errorAfter(Uri.fromFile(new File(lS)));
            }
        }
        private String saveErrorMessages(Thread t, Throwable e) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
            String fileName = "error-" + time + "-" + System.currentTimeMillis() + ".log";
            String lSystemFilePath = mConfig.cachePath;
            File dir = new File(lSystemFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new BufferedWriter(
                        new FileWriter(lSystemFilePath + fileName)));
                printWriter.println(time);
                getSystemInformation(printWriter);
                printWriter.println();
                e.printStackTrace(printWriter);
                printWriter.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
            return lSystemFilePath + fileName;
        }
        void getSystemInformation(PrintWriter printWriter) {
            //系统版本号
            printWriter.print("OS Version:");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print("_");
            printWriter.println(Build.VERSION.SDK_INT);
            //硬件制造商
            printWriter.print("Vendor:");
            printWriter.println(Build.MANUFACTURER);
            //系统定制商
            printWriter.print("Brand:");
            printWriter.println(Build.BRAND);
            printWriter.print("ID:");
            printWriter.println(Build.ID);
            printWriter.print("MODEL :");
            printWriter.println(Build.MODEL);
            printWriter.print("PRODUCT :");
            printWriter.println(Build.PRODUCT);
            printWriter.print("TIME :");
            printWriter.println(Build.TIME);
            printWriter.print("APP_VERSIONNAME :");
            printWriter.println(mConfig.getVersionName());
            printWriter.print("APP_VERSIOCODE :");
            printWriter.println(mConfig.getVersionCode());
            //获取设备指令集名称（CPU的类型）
            printWriter.print("SUPPORTED_ABIS:");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printWriter.println(TextUtils.concat(Build.SUPPORTED_ABIS).toString());
            } else {
                printWriter.println(Build.CPU_ABI);
            }

            printWriter.println();

        }
    }
}
