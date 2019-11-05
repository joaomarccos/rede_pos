import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:rede_pos_pg/rede_pos_pg.dart';

void main() {
  const MethodChannel channel = MethodChannel('rede_pos_pg');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await RedePosPg.startReversal(), '42');
  });
}
