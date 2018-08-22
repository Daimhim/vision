package org.daimhim.roundimg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 项目名称：org.daimhim.roundimg
 * 项目版本：vision
 * 创建时间：2018.08.22 16:21  星期三
 * 创建人：Daimhim
 * 修改时间：2018.08.22 16:21  星期三
 * 类描述：Daimhim 太懒了，什么都没有留下
 * 修改备注：Daimhim 太懒了，什么都没有留下
 *
 * @author：Daimhim
 */
public class RoundImageView extends ImageView {
    private boolean round;
    private int round_degree;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(RoundedBitmapDrawableFactory.create(getResources(),bm));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }
}
