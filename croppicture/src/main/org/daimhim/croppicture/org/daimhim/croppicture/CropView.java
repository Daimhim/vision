package org.daimhim.croppicture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 项目名称：org.daimhim.croppicture
 * 项目版本：vision
 * 创建时间：2018.06.08 10:37
 * 修改人：Daimhim
 * 修改时间：2018.06.08 10:37
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class CropView extends View {

    public CropView(Context context) {
        this(context, null);
    }

    public CropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
