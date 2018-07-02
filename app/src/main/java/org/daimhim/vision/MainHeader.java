package org.daimhim.vision;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import timber.log.Timber;

/**
 * 项目名称：org.daimhim.vision
 * 项目版本：vision
 * 创建时间：2018.06.21 14:29
 * 修改人：Daimhim
 * 修改时间：2018.06.21 14:29
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class MainHeader implements RefreshHeader {
    private Context mContext;
    private View mInflate;

    public MainHeader(Context pContext) {
        mContext = pContext;
        mInflate = LayoutInflater.from(mContext).inflate(R.layout.header_main, null);
    }

    @NonNull
    @Override
    public View getView() {
        Timber.e("getView");
        return mInflate;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        Timber.e("getSpinnerStyle:");
        return SpinnerStyle.Scale;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        Timber.e("setPrimaryColors:" + colors.length);
    }

    /**
     * 尺寸定义完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     *
     * @param kernel        RefreshKernel
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        Timber.e("height:" + height + "  maxDragHeight:" + maxDragHeight);
    }

    /**
     * 手指拖动下拉（会连续多次调用，添加isDragging并取代之前的onPulling、onReleasing）
     *
     * @param isDragging    true 手指正在拖动 false 回弹动画
     * @param percent       下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+maxDragHeight) / footerHeight )
     * @param offset        下拉的像素偏移量  0 - offset - (footerHeight+maxDragHeight)
     * @param height        高度 HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        Timber.e("onMoving  isDragging:" + isDragging + "  percent:" + percent +"  offset:"+offset+ "  height:" + height + "  maxDragHeight:" + maxDragHeight);
    }

    /**
     * 释放时刻（调用一次，将会触发加载）
     *
     * @param refreshLayout RefreshLayout
     * @param height        高度 HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Timber.e("height:" + height + "  maxDragHeight:" + maxDragHeight);
    }

    /**
     * 开始动画
     *
     * @param refreshLayout RefreshLayout
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Timber.e("onStartAnimator height:" + height + "  maxDragHeight:" + maxDragHeight);
    }

    /**
     * 动画结束
     *
     * @param refreshLayout RefreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        Timber.e("onFinish success:" + success);
        return 1000;
    }

    /**
     * 水平方向的拖动
     *
     * @param percentX  下拉时，手指水平坐标对屏幕的占比（0 - percentX - 1）
     * @param offsetX   下拉时，手指水平坐标对屏幕的偏移（0 - offsetX - LayoutWidth）
     * @param offsetMax 最大的偏移量
     */
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        Timber.e("onHorizontalDrag percentX:" + percentX + "  offsetX:" + offsetX + "  offsetMax:" + offsetMax);
    }
    /**
     * 是否支持水平方向的拖动（将会影响到onHorizontalDrag的调用）
     * @return 水平拖动需要消耗更多的时间和资源，所以如果不支持请返回false
     */
    @Override
    public boolean isSupportHorizontalDrag() {
        Timber.e("isSupportHorizontalDrag ");
        return false;
    }

    /**
     * 状态改变事件 {@link RefreshState}
     *
     * @param refreshLayout RefreshLayout
     * @param oldState      改变之前的状态
     * @param newState      改变之后的状态
     */
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        Timber.e("onStateChanged oldState:" + oldState.toString() + "  newState:" + newState.toString());
    }
}
