package org.daimhim.getphoto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 项目名称：meyki.distribution.contract
 * 项目版本：eshop-trunk
 * 创建人：Daimhim
 * 创建时间：2017/6/8 11:40
 * 修改人：Daimhim
 * 修改时间：2017/6/8 11:40
 * 类描述：
 * 修改备注：
 */

public interface PictureResourceContract {
    public interface PictureResourceCapture {
        /**
         * 从相册获取图片
         */
        void photoFromAlbum();

        /**
         * 从相机获取图片
         */
        void photoFromCamera();

        /**
         * 展示默认选择框
         * @param pContext 上下文
         */
        void showDefaultSelectionBox(Context pContext);

        /**
         * 返回的Uri
         */
        boolean onActivityResult(int requestCode, int resultCode, Intent data);
        //文件
        PictureResourceContract.PictureFileBuild getPictureFileBuild();
        /**
         * 是否裁剪
         *
         * @return
         */
        boolean isCut();

        //裁剪
        void onCut(Uri fromUri, Uri toUri, int requestCode);

        //裁剪操作
        void setPictureCut(PictureResourceContract.PictureCut pictureCut);

        //文件操作
        void setPictureFileBuild(PictureResourceContract.PictureFileBuild pictureFileBuild);

        //返回操作
        void setFilesReceiving(PictureResourceContract.FilesReceiving filesReceiving);

    }

    public interface PictureFileBuild {
        /**
         * 创建空文件Uri
         */
        Uri newlyBuild();

        /**
         * 创建空缓存文件Uri
         */
        Uri newlyCacheBuild();
    }

    /**
     * 给调用者的接口
     * 用于返回图片
     */
    public interface FilesReceiving {
        void receivePicture(Uri uri);

        void errorMessage(String error);
    }

    /**
     * 给调用者的接口
     * 调用者可以使用这个接口 对图片进行也锁裁剪等操作
     */
    public interface PictureCut {
        /**
         * @param fromUri     裁剪目标Uri
         * @param toUri       裁剪到目标Uri
         * @param requestCode 官方指定Code
         */
        void shearCriterion(Uri fromUri, Uri toUri, int requestCode);
    }
}
