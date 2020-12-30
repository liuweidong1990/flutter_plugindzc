import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPlugindzc {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugindzc');
  StreamSubscription<dynamic> _everntSubscription;
  FlutterPlugindzc(){
    initEvent();
  }
  initEvent(){
    _everntSubscription=_eventChannelFor().receiveBroadcastStream().listen(eventListener,onError:errorListener);
  }
  EventChannel _eventChannelFor(){
    return EventChannel("flutter_plugindzc_event");
  }
  void eventListener(dynamic event){
    final Map<dynamic,dynamic> map = event;

    switch(map['event']){
      case 'demoEvent':
        String value = map['value'];
        print('demo event data :$value');
        break;
    }
  }
  void errorListener(Object obj){
    final PlatformException e=obj;
    throw e;

  }
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<String> sayHello(String message) async {
    final String res = await _channel.invokeMethod('sayHello');
    return res;
/*    final Map<dynamic, dynamic> response = await _channel.invokeMethod(
        'sayHello',<String,dynamic>{'message':message,'info':'infoValue'});
    String res_message = response['message'];
    String res_info = response['info'];
    print('info:' + res_info);
    return res_message;*/
  }
  static Future<String> sayClose(String message) async {
    final String res = await _channel.invokeMethod('sayClose');
    return res;
  }
  static Future<double> getScale(String message) async {

   final double response = await _channel.invokeMethod('getScale');
    return response;
  }
}
