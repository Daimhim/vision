package org.daimhim.pictureload;

import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;

import java.io.File;

/**
 * Created by Scofield on 2018/6/6.
 */

public class ImageLoaderOptions {

    // 图片容器
    private ImageView imageView;
    // 图片地址
    private String url;
    // 图片地址
    private int resId;
    // 图片文件
    private File file;
    // 占位图
    private int placeHolderResId;
    // 加载错误图片
    private int errorResId;
    // 图片宽度
    private int width;
    // 图片高度
    private int height;
    // 是否作为gif展示
    private boolean asGif;
    // 是否跳过内存缓存
    private boolean isSkipMemoryCache;
    // 是否渐变显示图片
    private boolean isCrossFade;
    // 图形变换
    private Transformation transformation;
    // 回调函数
    private ILoaderResultCallback callback;

    private ImageLoaderOptions(Builder builder) {
        this.imageView = builder.imageView;
        this.url = builder.url;
        this.resId = builder.resId;
        this.file = builder.file;
        this.placeHolderResId = builder.placeHolderResId;
        this.errorResId = builder.errorResId;
        this.width = builder.width;
        this.height = builder.height;
        this.asGif = builder.asGif;
        this.isSkipMemoryCache = builder.isSkipMemoryCache;
        this.isCrossFade = builder.isCrossFade;
        this.transformation = builder.transformation;
        this.callback = builder.callback;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getUrl() {
        return url;
    }

    public int getResId() {
        return resId;
    }

    public File getFile() {
        return file;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isAsGif() {
        return asGif;
    }

    public boolean isSkipMemoryCache() {
        return isSkipMemoryCache;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public ILoaderResultCallback getCallback() {
        return callback;
    }

    public final static class Builder {
        // 图片容器
        private ImageView imageView;
        // 图片地址
        private String url;
        // 图片文件
        private File file;
        // 图片地址
        private int resId;
        // 占位图
        private int placeHolderResId;
        // 加载错误图片
        private int errorResId;
        // 图片宽度
        private int width;
        // 图片高度
        private int height;
        // 是否作为gif展示
        private boolean asGif;
        // 是否跳过内存缓存
        private boolean isSkipMemoryCache;
        // 是否渐变显示图片
        private boolean isCrossFade = true;
        // 图形变换
        private Transformation transformation;
        // 回调函数
        private ILoaderResultCallback callback;

        public Builder setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setResId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public Builder setPlaceHolderResId(int placeHolderResId) {
            this.placeHolderResId = placeHolderResId;
            return this;
        }

        public Builder setErrorResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setAsGif(boolean asGif) {
            this.asGif = asGif;
            return this;
        }

        public Builder setSkipMemoryCache(boolean skipMemoryCache) {
            isSkipMemoryCache = skipMemoryCache;
            return this;
        }

        public Builder setCrossFade(boolean crossFade) {
            isCrossFade = crossFade;
            return this;
        }

        public Builder setTransformation(Transformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder setCallback(ILoaderResultCallback callback) {
            this.callback = callback;
            return this;
        }

        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this);
        }

    }

}
