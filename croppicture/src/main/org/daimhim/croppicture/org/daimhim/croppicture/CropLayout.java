package org.daimhim.croppicture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 项目名称：org.daimhim.croppicture
 * 项目版本：vision
 * 创建时间：2018.06.08 10:38
 * 修改人：Daimhim
 * 修改时间：2018.06.08 10:38
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class CropLayout extends ViewGroup {

    public CropLayout(Context context) {
        this(context, null);
    }

    public CropLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
