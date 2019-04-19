package org.daimhim.pictureload;

import android.graphics.Bitmap;

/**
 * Created by Scofield on 2018/6/7.
 */

public interface ILoaderResultCallback {

    void onLoadSuccess(Bitmap resource);

    void onLoadFail();

}
