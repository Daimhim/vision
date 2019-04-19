package org.daimhim.pictureload;


import org.daimhim.pictureload.glide.GlideImageLoaderManager;

/**
 * Created by Scofield on 2018/6/6.
 */

public class ImageLoader {

    private static IImageLoaderManager imageLoader;

    public static IImageLoaderManager getImageLoaderManager() {
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                if (imageLoader == null) {
                    imageLoader = new GlideImageLoaderManager();
                }
            }
        }
        return imageLoader;
    }

}
