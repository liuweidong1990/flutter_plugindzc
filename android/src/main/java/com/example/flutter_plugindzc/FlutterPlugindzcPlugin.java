package com.example.flutter_plugindzc;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import cn.wintec.aidl.ScaleListener;
import cn.wintec.aidl.ScaleService;
import cn.wintec.aidl.WintecManagerService;

/** FlutterPlugindzcPlugin */
public class FlutterPlugindzcPlugin implements  MethodCallHandler {

  private static EventChannel.EventSink eventSink=null;
  private static EventChannel.StreamHandler steamHandler =new EventChannel.StreamHandler() {
    @Override
    public void onListen(Object o, EventChannel.EventSink sink) {
      eventSink=sink;
    }

    @Override
    public void onCancel(Object arguments) {
      eventSink=null;
    }
  };
  private final Context context;
  public FlutterPlugindzcPlugin(Activity activity, Context context, BinaryMessenger messenger){
    this.context = context;
  }
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_plugindzc");
    channel.setMethodCallHandler(new FlutterPlugindzcPlugin(registrar.activity(), registrar.context(), registrar.messenger()));
    EventChannel  eventChannel=new EventChannel(registrar.messenger(),"flutter_plugindzc_event");
    eventChannel.setStreamHandler(steamHandler);


  }
  private String devicePath = "/dev/ttySAC3";
  private double net;
  private double tare;

  private boolean stable;
  private boolean isFirst = true;
  private WintecManagerService WINTEC;

  public static ScaleService scaleService;
  private ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // 获取远程Service的onBind方法返回的对象的代理
      WINTEC = WintecManagerService.Stub.asInterface(service);
      try {
        Log.e("1314","连接成功");
        scaleService = ScaleService.Stub.asInterface(WINTEC.getScaleService());
        System.out.println( "aaaaabbbbbb"+scaleService);


      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      WINTEC = null;
    }
  };

  private static PluginRegistry.Registrar registrar;
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    Intent intent = new Intent();
    intent.setAction("cn.wintec.SERVICE");
    intent.setPackage("cn.wintec.sdk");
   // System.out.println(registrar.context());
    this.context.bindService(intent, conn, Service.BIND_AUTO_CREATE);


    if (call.method.equals("getPlatformVersion")) {
      // result.success("Android " + android.os.Build.VERSION.RELEASE);
      
      result.success("Android ");
    } else if(call.method.equals("sayHello")){
      if(eventSink!=null){

        try {
          scaleService.SCL_Close();
          scaleService.SCL_Open(devicePath, new ScaleListener.Stub() {
            @Override
            public void onWeightResult(double v, double v1, boolean b, int i, boolean b1, boolean b2) throws RemoteException {
              net = v;
              tare = v1;
              stable = b;
             // System.out.println("SCL_Open133333333aaaaaaaaa++++"+net);
              // Toast.makeText(this,net,Toast.LENGTH_SHORT).show();
              //                    System.out.println("净重========" + net + "皮重========" + tare);


            }
          });

//                    sca_t_net.setText("打开电子秤");
//                    sca_t_tare.setText("打开电子秤");
          //scaleClosed = false;

        } catch (Exception e) {
          System.out.println(e.getMessage());
          //Toast.makeText(this, "打开电子秤出现异常", Toast.LENGTH_SHORT).show();
        }
      }
    }else if(call.method.equals("sayClose")){
      try {
        scaleService.SCL_Close();
        System.out.println("关闭");
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }else if(call.method.equals("getScale")){
     /* ConstraintsMap params = new ConstraintsMap();
      params.putString("event","demoEvent");
      params.putString("value",""+net);
      eventSink.success(params.toMap());*/
      result.success(net);
    }else {
      result.notImplemented();
    }

  }

}
