import 'dart:async';

import 'package:flutter/services.dart';

class RedePosPg {
  static const MethodChannel _channel =
      const MethodChannel('rede_pos_pg');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
