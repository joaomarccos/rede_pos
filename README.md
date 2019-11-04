# [rede_pos](https://pub.dev/packages/rede_pos_pg)

A plugin wrapper to use rede pos hardware sdk in your flutter apps.

**ONLY WORKS ON ANDROID**

## Usage

### Configure your project

Change **minSdkVersion to 19** in your app
```
 defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId "com.example.test_getnet_pos_package"
        minSdkVersion 19
        targetSdkVersion 28
        versionCode flutterVersionCode.toInteger()
        versionName flutterVersionName
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
``` 

### method..