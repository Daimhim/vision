# 一键支付API文档
##### 项目简介
> 本项目集成了支付宝和微信支付，简化了两个支付的调用流程（支付宝好像没简化什么，微信是真心简化了）
##### 接入指南
###### 1. 第一步 依赖

```
//根目录的build添加仓库地址
repositories {
        maven { url 'https://dl.bintray.com/daimhim/widget/'}
    }

//在模块build下添加
dependencies {
        compile 'org.daimhim.onekeypayment:onekeypayment:1.0.7'
}
```
###### 2. 第一步 注册

```
//你需要在AndroidManifest中添加以下代码
 <!-- 支付宝支付界面回调 -->
        <activity
            android:name="com.alipay.sdk.app.H5AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:exported="false"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan|stateHidden" />
        <!-- 微信支付界面回调 -->
        <activity
            android:name="org.daimhim.onekeypayment.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan|stateHidden" />

        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="org.daimhim.onekeypayment.WXPayEntryActivity">
            <intent-filter>
                <data android:scheme="wx2ec90530f9386bdd" />
            </intent-filter>
        </activity-alias>
```
###### 3. 第一步 调用

```
//微信和支付宝调用 传入参数分两种，一种是组装好的（微信，支付宝的支付传参数都需要用几个参数组成一个字符串并传入），一种是未组装的

//以下是未组装的  示例
//微信
WxPayParameter lWxPayParameter = new WxPayParameter();
lWxPayParameter.setAppId(obj.getString("appId"));
lWxPayParameter.setTimeStamp(obj.getString("timeStamp"));
lWxPayParameter.setPackageValue(obj.getString("packageValue"));
lWxPayParameter.setPartnerId(obj.getString("partnerId"));
lWxPayParameter.setNonceStr(obj.getString("nonceStr"));
lWxPayParameter.setPrepayId(obj.getString("prepayId"));
lWxPayParameter.setPaySign(obj.getString("sign"));
PaymentRequest lPaymentRequest = new PaymentRequest(lWxPayParameter);
lPaymentRequest.setPayType(PaymentConst.WX_PAY);
ToPay.getInstance().toPayment(activity.this, lPaymentRequest,activity.this);

//以下是已组装的
//支付宝
AlPayParameter lAlPayParameter = new AlPayParameter();
lAlPayParameter.setSignInfo(obj.getString("signInfo"));
PaymentRequest lPaymentRequest = new PaymentRequest(lAlPayParameter);
lPaymentRequest.setPayType(PaymentConst.AL_PAY);
ToPay.getInstance().toPayment(activity.this, lPaymentRequest, activity.this);

//其中微信的sign和支付宝的signInfo就是传入组装好的参数，如果此参数为空就会根据用户传入的其他参数来自动组装


//支付回调，你需要实现PaymentCallback接口，支付宝和微信统一回调
@Override
    public void onPaymentSuccess(PaymentReponse result) {
        switch (result.getPayStatus()) {
            case PaymentConst.PAY_SUCCESS:
                //支付成功
                Toast.makeText(mContext, "支付成功",Toast.LENGTH_SHORT).show();
                break;
            case PaymentConst.PAY_CANCEL:
                //支付取消
                Toast.makeText(mContext, "您取消了支付",Toast.LENGTH_SHORT).show();
                break;
            case PaymentConst.PAY_FAILURE:
                //失败
                Toast.makeText(mContext, "支付失败",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPaymentFailure(String status) {
      Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
    }
//onPaymentSuccess 支付已成功调起并返回的状态码
//onPaymentFailure 支付还未调起的失败，这种情况请来联系作者
```
# 版本更新说明
##### 1.0.7
> 1. 修复了在微信双开情况下如果取消使用哪个微信支付的选择弹窗，之后支付调不起来的
> 2. 优化了支付调用，使用单例+懒加载，优化内存 
##### 1.0.5 
> 1. 优化参数传入
##### 1.0.3
> 1. 增加了支付后对各个变量的回收
##### 1.0.0
> 1. 初始化项目
