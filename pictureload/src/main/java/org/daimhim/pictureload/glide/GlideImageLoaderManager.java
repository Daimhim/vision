package org.daimhim.pictureload.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;


import org.daimhim.pictureload.IImageLoaderManager;
import org.daimhim.pictureload.ILoaderResultCallback;
import org.daimhim.pictureload.ImageLoaderOptions;
import org.daimhim.pictureload.ImageUtil;
import org.daimhim.pictureload.R;

import java.util.logging.Logger;

import timber.log.Timber;

/**
 * Created by Scofield on 2018/6/7.
 */

public class GlideImageLoaderManager implements IImageLoaderManager {

    @Override
    public void init(Context context) {
        // 暂时不需要初始化
    }

    @Override
    public void loadNet(Context context, ImageLoaderOptions options) {
        if (TextUtils.isEmpty(options.getUrl())) {
            throw new IllegalArgumentException("you should set url first");
        }
        load(Glide
                .with(context)
                .load(options.getUrl()), options);
    }

    @Override
    public void loadResource(Context context, ImageLoaderOptions options) {
        if (options.getResId() == 0) {
            throw new IllegalArgumentException("you should set resource id first");
        }
        load(Glide
                .with(context)
                .load(options.getResId()), options);
    }

    @Override
    public void loadFile(Context context, ImageLoaderOptions options) {
        if (options.getFile() == null) {
            throw new IllegalArgumentException("you should set file first");
        }
        if (!options.getFile().exists()) {
            Timber.e("GlideImageLoaderManager the file is not exist");
            return;
        }
        load(Glide.with(context).load(options.getFile()), options);
    }

    @Override
    public void downloadImage(Context context, ImageLoaderOptions options, final ILoaderResultCallback callback) {
        if (TextUtils.isEmpty(options.getUrl())) {
            throw new IllegalArgumentException("you should set url first");
        }
        int width = Target.SIZE_ORIGINAL;
        int height = Target.SIZE_ORIGINAL;
        if (options.getWidth() > 0 && options.getHeight() > 0) {
            width = options.getWidth();
            height = options.getHeight();
        }
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (callback != null) {
                    callback.onLoadSuccess(resource);
                }
            }
        };
        Glide.with(context)
                .asBitmap()
                .load(options.getUrl())
                .into(target);
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    @SuppressLint("CheckResult")
    private void load(RequestBuilder<Drawable> request, final ImageLoaderOptions options) {
        RequestOptions glideOptions = new RequestOptions();
        if (options.getPlaceHolderResId() != 0) {
            // 设置占位符
            glideOptions.placeholder(options.getPlaceHolderResId());
        }
        if (options.getErrorResId() != 0) {
            // 设置错误图片
            glideOptions.error(options.getErrorResId());
        }
        // 是否跳过内存缓存
        glideOptions.skipMemoryCache(options.isSkipMemoryCache());
        if (options.isCrossFade()) {
            // 是否渐变
            request.transition(DrawableTransitionOptions.withCrossFade());
        }
        if (options.getWidth() > 0 && options.getHeight() > 0) {
            // 调整图片大小
            request.override(options.getWidth(), options.getHeight());
        }
        if (options.getTransformation() != null) {
            glideOptions.transform(options.getTransformation());
        }
        if (options.getCallback() != null) {

            request.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    options.getCallback().onLoadFail();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    options.getCallback().onLoadSuccess(ImageUtil.drawableToBitmap(resource));
                    return false;
                }
            });
        }
        request
                .apply(glideOptions)
                .into(options.getImageView());
    }

}
