package io.zafeapps.rede_pos_pg;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import rede.smartrede.sdk.FlexTipoPagamento;
import rede.smartrede.sdk.Payment;
import rede.smartrede.sdk.PaymentIntentBuilder;
import rede.smartrede.sdk.RedePaymentValidationError;
import rede.smartrede.sdk.RedePayments;

/**
 * RedePosPgPlugin
 */
public class RedePosPgPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {

    private static final int PAYMENT_REQUEST_CODE = 1001;
    private static final int REVERSAL_REQUEST_CODE = 1002;
    private static final int REPRINT_REQUEST_CODE = 1003;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "rede_pos_pg");
        RedePosPgPlugin plugin = new RedePosPgPlugin(registrar.activity());
        registrar.addActivityResultListener(plugin);
        channel.setMethodCallHandler(plugin);
    }

    private Result result;
    private Activity activity;
    private RedePayments redePayments;

    private RedePosPgPlugin(Activity activity) {
        this.activity = activity;
        this.redePayments = RedePayments.getInstance(activity);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        this.result = result;
        mountPayment(call);
        doReversal(call);
        reprint(call);
    }

    private void reprint(MethodCall call) {
        if (call.method.equals("reprint")) {
            try {
                Intent reprint = redePayments.intentForReprint();
                this.activity.startActivityForResult(reprint, REPRINT_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
                this.result.error("Error", e.getMessage(), null);
            }
        }
    }

    private void mountPayment(MethodCall call) {
        if (call.method.equals("payment")) {
            try {
                FlexTipoPagamento paymentType = FlexTipoPagamento.valueOf(call.<String>argument("paymentType"));
                Long ammount = Long.parseLong(Objects.requireNonNull(call.argument("ammount")).toString());
                int installments = Integer.parseInt(Objects.requireNonNull(call.argument("installments")).toString());
                PaymentIntentBuilder paymentIntentBuilder = redePayments.intentForPaymentBuilder(paymentType, ammount);
                if (installments > 1) paymentIntentBuilder.setInstallments(installments);
                Intent intent = paymentIntentBuilder.build();
                this.activity.startActivityForResult(intent, PAYMENT_REQUEST_CODE);
            } catch (ActivityNotFoundException | RedePaymentValidationError e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
                this.result.error("Error", e.getMessage(), null);
            }
        }
    }

    private void doReversal(MethodCall call) {
        if (call.method.equals("reversal")) {
            try {
                Intent reversal = redePayments.intentForReversal();
                this.activity.startActivityForResult(reversal, REVERSAL_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
                this.result.error("Error", e.getMessage(), null);
            }
        }
    }


    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (handlerPaymentResponse(requestCode, resultCode, data)) return true;
        else if (handlerReversalResponse(requestCode, resultCode, data)) return true;
        return handlerReprintResponse(requestCode, resultCode, data);
    }

    private boolean handlerReprintResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REPRINT_REQUEST_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    this.result.success(true);
                } else {
                    this.result.error("Error on reprint", null, null);
                }
            } catch (Exception e) {
                this.result.error("Error:", e.getMessage(), null);
            }
        }
        return false;
    }

    private boolean handlerReversalResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REVERSAL_REQUEST_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Payment payment = RedePayments.getPaymentFromIntent(data);
                        this.result.success(Objects.requireNonNull(payment, "payment not found").getStatus().toString());
                        return true;
                    }
                } else {
                    this.result.error("Error on reversal operation", null, null);
                }
            } catch (Exception e) {
                this.result.error("Error:", e.getMessage(), null);
            }
        }
        return false;
    }

    private boolean handlerPaymentResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Payment payment = RedePayments.getPaymentFromIntent(data);
                        this.result.success(Objects.requireNonNull(payment, "payment not found").getStatus().toString());
                        return true;
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    this.result.error("Payment cancelled by the user", null, null);
                }
            } catch (Exception e) {
                this.result.error("Error:", e.getMessage(), null);
            }
        }
        return false;
    }
}
