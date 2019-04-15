package org.daimhim.getphoto;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 项目名称：meyki.distribution.presenter
 * 项目版本：eshop-trunk
 * 创建人：Daimhim
 * 创建时间：2017/6/8 11:42
 * 修改人：Daimhim
 * 修改时间：2017/6/8 11:42
 * 类描述：获取图片目录和缓存图片目录
 * 修改备注：
 */

public class PictureFileBuildPresenterImp implements PictureResourceContract.PictureFileBuild {
    private Context mContext;



    public PictureFileBuildPresenterImp(Context context) {
        mContext = context;
    }

    @Override
    public Uri newlyBuild() {
        ;
        File file = new File(getPictureDirectory(),
                new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String lName = file.getName(); ///storage/emulated/0/daimhim/org.daimhim.muster/20190414124152.png
            return  FileProvider.getUriForFile(
                    mContext,
                    getFileProviderName(mContext),
                    file
                    );
        }else {
            return Uri.fromFile(file);
        }
    }

    @Override
    public Uri newlyCacheBuild() {
        File file = new File(getPictureCacheDirectory(),
                new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String lPackageName = mContext.getPackageName();
            return  FileProvider.getUriForFile(
                    mContext,
                    TextUtils.concat(lPackageName,".provider").toString(),
                    file
            );
        }else {
            return Uri.fromFile(file);
        }
    }
    /**
     * 获取图片目录
     *
     * @return
     */
    private File getPictureDirectory() {
        String path = TextUtils.concat(
                Environment.getExternalStorageDirectory().getPath(),
//                "/daimhim/",
//                mContext.getPackageName(),
                "/img/"
        ).toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.i("","path:"+path);
        return file;
    }

    /**
     * 获取切图缓存目录
     *
     * @return
     */
    private File getPictureCacheDirectory() {
        File photoCacheDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/" + mContext.getPackageName() + "/cache/img/");
        if (!photoCacheDir.exists()) {
            photoCacheDir.mkdirs();
        }
        return photoCacheDir;
    }

    private String getAppTagName(Context pContext){
        String lAppTagName = "daimhim";
        String lPackageName = pContext.getPackageName();
        if (!TextUtils.isEmpty(lPackageName)) {
            int lI = lPackageName.lastIndexOf(".");
            lAppTagName =lPackageName.substring(lI);
        }
        return lAppTagName;
    }

    private String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }
}
