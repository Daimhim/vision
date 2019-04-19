package org.daimhim.pictureload;

import android.content.Context;

/**
 * Created by Scofield on 2018/6/6.
 */

public interface IImageLoaderManager {

    void init(Context context);

    void loadNet(Context context, ImageLoaderOptions options);

    void loadResource(Context context, ImageLoaderOptions options);

    void loadFile(Context context, ImageLoaderOptions options);

    void downloadImage(Context context, ImageLoaderOptions options, ILoaderResultCallback callback);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    void pause(Context context);

    void resume(Context context);

}
