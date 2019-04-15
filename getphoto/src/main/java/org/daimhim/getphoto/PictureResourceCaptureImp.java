package org.daimhim.getphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.Serializable;


/**
 * 项目版本：eshop-trunk
 * 创建人：Daimhim
 * 创建时间：2017/5/25 9:05
 * 修改人：Daimhim
 * 修改时间：2017/5/25 9:05
 * 类描述：
 * 修改备注：
 */

public class PictureResourceCaptureImp implements Serializable, PictureResourceContract.PictureResourceCapture {
    private final int MAKE_PHOTO = 0x001;// 拍照

    private final int PHOTO_ALBUM = 0x002;// 调用相册

    private final int PHOTO_REQUEST_CUT = 0x003;// 结果

    private final int NULL = 0;// 取消

    private PictureResourceContract.FilesReceiving mFilesReceiving = null;
    private PictureResourceContract.PictureCut mPictureCut = null;
    private PictureResourceContract.PictureFileBuild mPictureFileBuild = null;

    private Activity mActivity;
    private Fragment mFragment;
    //原图Uri
    private Uri mOriginalUri = null;

    //裁剪以后的Uri
    private Uri mPictureCutUri = null;

    private String TAG = this.getClass().getSimpleName();


    public PictureResourceCaptureImp(Activity activity, PictureResourceContract.FilesReceiving filesReceiving) {
        this.mActivity = activity;
        setFilesReceiving(filesReceiving);
        setPictureFileBuild(new PictureFileBuildPresenterImp(activity));
    }
    public PictureResourceCaptureImp(Fragment pFragment, PictureResourceContract.FilesReceiving filesReceiving) {
        mFragment = pFragment;
        setFilesReceiving(filesReceiving);
        setPictureFileBuild(new PictureFileBuildPresenterImp(pFragment.getContext()));
    }

    /**
     * 选择方式对话框
     */
    public ActionSheetDialog actionSheetDialogNoTitle(Context pContext) {
        return new ActionSheetDialog(pContext)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                // 这里添加意图启动手机自带照相机
                                photoFromCamera();
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                photoFromAlbum();
                            }
                        });
    }

    /**
     * 从相册获取图片
     */
    @Override
    public void photoFromAlbum() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        if (null != mActivity) {
            mActivity.startActivityForResult(getAlbum, PHOTO_ALBUM);
        }else {
            mFragment.startActivityForResult(getAlbum, PHOTO_ALBUM);
        }

//        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (null!=mActivity) {
//            if (intent_photo.resolveActivity(mActivity.getPackageManager()) != null) {
//                File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//                if (tempFile != null) {
//                    mOriginalUri = Uri.fromFile(tempFile);
//                }
//                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, mOriginalUri);
//                mActivity.startActivityForResult(intent_photo, PHOTO_ALBUM);
//            }
//        }else {
//            if (intent_photo.resolveActivity(mFragmentClass.getActivity().getPackageManager()) != null) {
//                File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//                if (tempFile != null) {
//                    mOriginalUri = Uri.fromFile(tempFile);
//                }
//                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, mOriginalUri);
//                mFragmentClass.startActivityForResult(intent_photo, PHOTO_ALBUM);
//            }
//        }

    }

    /**
     * 从相机获取图片
     */
    @Override
    public void photoFromCamera() {
        Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        setOriginalUri(mPictureFileBuild.newlyBuild());//创建文件
        getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, getOriginalUri());//根据uri保存照片
        getPhoto.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//保存照片的质量  0 1
        if (null!=mActivity) {
            if (getPhoto.resolveActivity(mActivity.getPackageManager()) != null) {
                mActivity.startActivityForResult(getPhoto, MAKE_PHOTO);//启动相机拍照
            }
        }else {
            if (getPhoto.resolveActivity(mFragment.getActivity().getPackageManager()) != null) {
                mFragment.startActivityForResult(getPhoto, MAKE_PHOTO);//启动相机拍照
            }
        }
    }

    @Override
    public void showDefaultSelectionBox(Context pContext) {
        actionSheetDialogNoTitle(pContext).show();
    }

    /**
     * 返回的Uri
     */
    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MAKE_PHOTO:  //相机
                    if (null == getOriginalUri()) {
                        return true;
                    }
                    if (isCut()) {//是否裁剪
                        setPictureCutUri(mPictureFileBuild.newlyCacheBuild());
                        onCut(getOriginalUri(), getPictureCutUri(), PHOTO_REQUEST_CUT);
                    } else {
                        mFilesReceiving.receivePicture(getOriginalUri());
                    }
                    return true;
                case PHOTO_ALBUM:  //相册
                    setOriginalUri(data.getData());
                    if (null == getOriginalUri()) {
                        return true;
                    }
                    if (isCut()) {  //是否裁剪
                        setPictureCutUri(mPictureFileBuild.newlyCacheBuild());
                        onCut(getOriginalUri(), getPictureCutUri(), PHOTO_REQUEST_CUT);
                    } else {
                        mFilesReceiving.receivePicture(getRealPathFromURI(getOriginalUri()));
                    }
                    return true;
                case PHOTO_REQUEST_CUT:  //裁剪
                    if (null == getPictureCutUri()) {
                        return true;
                    }
                    mFilesReceiving.receivePicture(getPictureCutUri().getPath().endsWith(".png") ? mPictureCutUri : getRealPathFromURI(mPictureCutUri));
                    return true;
                default:
                    return false;
            }
        } else {
            switch (requestCode) {
                case NULL:
                case MAKE_PHOTO:
                case PHOTO_ALBUM:
                case PHOTO_REQUEST_CUT:
                    mFilesReceiving.errorMessage("请重新选择");
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean isCut() {
        return null != mPictureCut;
    }

    @Override
    public void onCut(Uri fromUri, Uri toUri, int requestCode) {
        mPictureCut.shearCriterion(fromUri, toUri, requestCode);
    }

    /**
     * 有时系统返回的Uri 仅是图片编号  这时需要把它转为 Uri
     *
     * @param contentURI
     * @return
     */
    private Uri getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mFragment.getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return Uri.parse(result);
    }

    /**
     * 用户实现 确定剪切
     *
     * @param pictureCut
     */
    @Override
    public void setPictureCut(PictureResourceContract.PictureCut pictureCut) {
        mPictureCut = pictureCut;
    }

    //文件
    @Override
    public void setPictureFileBuild(PictureResourceContract.PictureFileBuild pictureFileBuild) {
        mPictureFileBuild = pictureFileBuild;
    }

    @Override
    public PictureResourceContract.PictureFileBuild getPictureFileBuild() {
        return mPictureFileBuild;
    }

    @Override
    public void setFilesReceiving(PictureResourceContract.FilesReceiving filesReceiving) {
        mFilesReceiving = filesReceiving;
    }

    //获取拍照/相册的 Uri
    public Uri getOriginalUri() {
        return mOriginalUri;
    }

    //设置拍照/相册的 Uri
    public void setOriginalUri(Uri originalUri) {
        mOriginalUri = originalUri;
    }

    //获取裁剪的 Uri
    public Uri getPictureCutUri() {
        return mPictureCutUri;
    }

    //设置裁剪的 Uri
    public void setPictureCutUri(Uri pictureCutUri) {
        mPictureCutUri = pictureCutUri;
    }

    //--------------------------------------以下是图片工具----------------------------------------------------------

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得Bitmap并压缩返回bitmap用于显示
     *
     * @param uri       文件路径
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return Bitmap  等比放小 不失真
     */
    public Bitmap getSmallBitmap(Uri uri, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  //只返回图片的大小信息
        BitmapFactory.decodeFile(uri.getPath(), options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }

    /**
     * 切图加入缓存策略
     *
     * @param cacheName
     */
    public void addCutCache(String cacheName) {
        if (null == mPictureCutUri) return;
        String path = mPictureCutUri.getPath();
        String b = path.substring(0, path.lastIndexOf("/"));
        (new File(mPictureCutUri.getPath())).renameTo(new File(b + "/" + cacheName));
    }

    public void preservationBitmap(Bitmap bitmap) {

    }

}
