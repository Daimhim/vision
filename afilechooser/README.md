# 文件选择器
##### 项目简介
> 选择文件
##### 接入指南
###### 1. 依赖

```
implementation 'org.daimhim.afilechooser:afilechooser:1.0'
```
###### 2. 注册

```
 <activity
            android:name="org.daimhim.afilechooser.ipaulpro.afilechooser.FileChooserActivity"
            android:enabled="@bool/use_activity"
            android:exported="true"
            android:icon="@android:drawable/ic_dialog_info"
            android:label="@string/choose_file" >
            <intent-filter>
                <action android:name="android.intent.action.GET_CONTENT" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.OPENABLE" />

                <data android:mimeType="*/*" />
            </intent-filter>
        </activity>

        <provider
            android:name="org.daimhim.afilechooser.ianhanniballake.localstorage.LocalStorageProvider"
            android:authorities="org.daimhim.afilechooser.ianhanniballake.localstorage.documents"
            android:enabled="@bool/use_provider"
            android:exported="true"
            android:grantUriPermissions="true"
            android:permission="android.permission.MANAGE_DOCUMENTS" >
            <intent-filter>
                <action android:name="android.content.action.DOCUMENTS_PROVIDER" />
            </intent-filter>
        </provider>

```
###### 3. 调用

```
private static final int REQUEST_CODE = 6384;
    /**
     * 吊起
     */
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, "上传APK");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                 final Uri uri = data.getData();
                 Log.i(TAG, "Uri = " + uri.toString());
                 try {
                     // Get the file path from the URI
                     final String path = FileUtils.getPath(getContext(), uri);
                     Toast.makeText(getContext(),
                             "File Selected: " + path, Toast.LENGTH_LONG).show();
                 } catch (Exception e) {
                     Log.e(TAG, "File select error", e);
                 }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```
# 版本更新说明
##### 1.0.0
> 1. 初始化项目