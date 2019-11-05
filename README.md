# [rede_pos](https://pub.dev/packages/rede_pos_pg)

A plugin wrapper to use rede pos hardware sdk in your flutter apps.

**ONLY WORKS ON ANDROID**

## Usage

### Configure your project

Change **minSdkVersion to 19** in your app
```
 defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId "com.example.test_rede_pos_package"
        minSdkVersion 19
        targetSdkVersion 28
        versionCode flutterVersionCode.toInteger()
        versionName flutterVersionName
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
``` 

### Usage

Import the RedePosPg class. Use the availble static methods.

```
// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String transactionResults = "";
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      // payment: R$90,00 in 3x
      var operationStatus = await RedePosPg.startPayment(PaymentType.CREDITO_PARCELADO, 9000, installments: 3);      
      transactionResults += "payment: R\$90,00 in 3x: [$operationStatus]\n";
      // payment: R$1,00
      operationStatus = await RedePosPg.startPayment(PaymentType.CREDITO_A_VISTA, 100);
      transactionResults += "payment: R\$1,00: [$operationStatus]\n";
      // reversal
      operationStatus = await RedePosPg.startReversal();
      transactionResults += "reversal: [$operationStatus]\n";
      operationStatus = await RedePosPg.startReprint() ? "Success" :  "Failure";
      transactionResults += "reprint: [$operationStatus]\n";
    } on PlatformException {
      transactionResults = 'Failed.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _results = transactionResults;
    });
  }
```