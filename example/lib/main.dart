import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rede_pos_pg/rede_pos_pg.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _results = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

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

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_results\n'),
        ),
      ),
    );
  }
}
