package org.daimhim.onekeypayment;

import android.app.Fragment;
import android.util.Log;

import static org.daimhim.onekeypayment.Paymenting.sIWXAPIEventHandler;

/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.07.18 17:53
 * 修改人：Daimhim
 * 修改时间：2018.07.18 17:53
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class PaymentingFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        if (null!=sIWXAPIEventHandler) {
            sIWXAPIEventHandler.isRun = true;
        }
    }
}
