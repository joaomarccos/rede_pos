import 'dart:async';

import 'package:flutter/services.dart';

enum PaymentType {
  CREDITO_A_VISTA,
  CREDITO_PARCELADO,
  CREDITO_PARCELADO_EMISSOR,
  DEBITO,
  VOUCHER,
}

class RedePosPg {
  static const MethodChannel _channel = const MethodChannel('rede_pos_pg');

  /// Starts a new payment. The ammount is the transaction value multiplied by 100.
  /// Returns the payment status code:
  /// AUTHORIZED: Transação realizada/estornada com sucesso
  /// FAILED: Falha na transação
  /// DECLINED: Transação negada.
  static Future<String> startPayment(PaymentType paymentType, int ammount, {int installments = 0}) async {
    final String result = await _channel.invokeMethod('payment', {
      'paymentType': paymentType.toString(),
      'ammount': ammount,
      'installments': installments
    });
    return result;
  }

  /// Cancel a payment.
  /// Returns the reversal status code:
  /// AUTHORIZED: Transação realizada/estornada com sucesso
  /// FAILED: Falha na transação
  /// DECLINED: Transação negada.
   static Future<String> startReversal() async {
    final String result = await _channel.invokeMethod('reversal');
    return result;
  }

  /// Reprint a payment.
  /// Returns true if printed. Otherwise false.  
   static Future<bool> startReprint() async {
    final bool result = await _channel.invokeMethod('reprint');
    return result;
  }
}
