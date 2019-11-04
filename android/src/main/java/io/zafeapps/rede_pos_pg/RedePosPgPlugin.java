package io.zafeapps.rede_pos_pg;

import android.app.Activity;
import android.content.Intent;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import rede.smartrede.sdk.FlexTipoPagamento;
import rede.smartrede.sdk.RedePayments;

/**
 * RedePosPgPlugin
 */
public class RedePosPgPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "rede_pos_pg");
        RedePosPgPlugin plugin = new RedePosPgPlugin(channel, registrar.activity());
        registrar.addActivityResultListener(plugin);
        channel.setMethodCallHandler(plugin);
    }

    private MethodChannel methodChannel;
    private Result result;
    private Activity activity;
    private int requestCode;
    private RedePayments redePayments;

    public RedePosPgPlugin(MethodChannel methodChannel, Activity activity) {
        this.methodChannel = methodChannel;
        this.activity = activity;
        this.redePayments = RedePayments.getInstance(activity);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        getPlataformVersion(call, result);
    }

    private void getPlataformVersion(MethodCall call, Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }

    private void mountPayment(MethodCall call, Result result) {
//        mout payment from call data and handle result on activity result
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
//    when receive a result from other activity do:
        return false;
    }
}
