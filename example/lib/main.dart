import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_plugindzc/flutter_plugindzc.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    FlutterPlugindzc();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterPlugindzc.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }
  String message=null;
  double message1=0.0;
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Running on: $_platformVersion\n  $message1'),
              RaisedButton(
                child: Text('打开',style:TextStyle(fontSize: 28.0)),
              onPressed:() async{
                message=await FlutterPlugindzc.sayHello("hello");
               setState(() {});
                 print("aaa+"+message);
              }),
              RaisedButton(
                  child: Text('关闭',style:TextStyle(fontSize: 28.0)),
                  onPressed:() async{
                    message=await FlutterPlugindzc.sayClose("hello");
                    setState(() {});
                    print("aaa+"+message);
                  }),
              RaisedButton(
                  child: Text('获取重量',style:TextStyle(fontSize: 28.0)),
                  onPressed:() async{
                    message1=await FlutterPlugindzc.getScale("getScale");
                    setState(() {});
                    print(message1);
                  })
            ],
          )
        ),
      ),
    );
  }

}
