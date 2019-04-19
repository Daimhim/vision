package org.daimhim.pictureload;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * 项目名称：org.daimhim.pictureload
 * 项目版本：vision
 * 创建时间：2018.06.25 17:04
 * 修改人：Daimhim
 * 修改时间：2018.06.25 17:04
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
@Deprecated
public class ImgLoadingUtil {

    public static ImageLoadConfig getDefaultConfig() {
        return new ImageLoadConfig();
    }

    public static class ImageLoadConfig {
        /**
         * 没有任何图片加载模式
         */
        public static final int LOAD_TYPE_NONE = 0;
        /**
         * 短边拉伸，长边裁剪填满控件
         */
        public static final int LOAD_TYPE_CENTER_CROP = 1;
        /**
         * 长短拉伸，短边也拉伸，无视图片比例
         */
        public static final int LOAD_TYPE_FIT_XY = 2;
        /**
         * 图片加载模式 centerCrop, fitXY， 现在使用的是Glide，仅支持这两种设置模式，默认是都没有
         */
        private int loadType = LOAD_TYPE_NONE;
        /**
         * 图片切换的淡入淡出动画，时间默认为300，可以手动修改
         */
        private int crossFadeTime = 300;
        /**
         * 不进行缓存
         */
        public static final int CACHE_TYPE_NONE = 0;
        /**
         * 仅内存缓存
         */
        public static final int CACHE_TYPE_MEMORY = 1;
        /**
         * 仅磁盘缓存
         */
        public static final int CACHE_TYPE_DISK = 2;
        /**
         * 二者都缓存
         */
        public static final int CACHE_TYPE_BOTH = 3;
        /**
         * 图片缓存策略，仅内存，仅磁盘，二者都，二者都不
         */
        private int cacheType = CACHE_TYPE_BOTH;

        /**
         * 图片下载中显示的图片
         */
        private int placeHolder = -1;
        /**
         * 图片下载失败显示的图片
         */
        private int errorHolder = -1;

        public void setLoadType(int loadType) {
            this.loadType = loadType;
        }

        public void setPlaysholder(@DrawableRes int placeHolder) {
            this.placeHolder = placeHolder;
        }

        public void setErrorHolder(@DrawableRes int errorHolder) {
            this.errorHolder = errorHolder;
        }

        public void setCrossFadeTime(int crossFadeTime) {
            this.crossFadeTime = crossFadeTime;
        }

        public void setCacheType(int cacheType) {
            this.cacheType = cacheType;
        }
    }

    public static void loadImage(ImageView iv, Object url) {
        loadImage(getDefaultConfig(), iv, url);
    }

    public static void loadImage(ImageView iv, Object url, @DrawableRes int placeHolder, @DrawableRes int errorHolder) {
        ImageLoadConfig defaultConfig = getDefaultConfig();
        defaultConfig.setPlaysholder(placeHolder);
        defaultConfig.setErrorHolder(errorHolder);
        loadImage(defaultConfig, iv, url);
    }

    public static void loadImage(ImageLoadConfig config, ImageView iv, Object url) {
        //各种对应的参数配置
        RequestOptions requestOptions = new RequestOptions();
        if (config.placeHolder != -1) {
            requestOptions = requestOptions.placeholder(config.placeHolder);
        }
        if (config.errorHolder != -1) {
            requestOptions = requestOptions.error(config.errorHolder);
        }
        switch (config.cacheType) {
            case ImageLoadConfig.CACHE_TYPE_BOTH:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case ImageLoadConfig.CACHE_TYPE_DISK:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case ImageLoadConfig.CACHE_TYPE_MEMORY:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case ImageLoadConfig.CACHE_TYPE_NONE:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            default:
                break;
        }
        switch (config.loadType) {
            case ImageLoadConfig.LOAD_TYPE_CENTER_CROP:
                requestOptions.centerCrop();
                break;
            case ImageLoadConfig.LOAD_TYPE_FIT_XY:
                requestOptions.fitCenter();
                break;
            case ImageLoadConfig.LOAD_TYPE_NONE:
                requestOptions.centerInside();
                break;
            default:
                break;
        }
        //图片加载后的渐变动画
        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions();
        if (config.crossFadeTime == 0) {
            transitionOptions.dontTransition();
        } else {
            transitionOptions = transitionOptions.crossFade(config.crossFadeTime);
        }
        //最后加载图片
        Glide.with(iv)
                .load(url)
                .transition(transitionOptions)
                .apply(requestOptions)
                .into(iv);
    }
}
