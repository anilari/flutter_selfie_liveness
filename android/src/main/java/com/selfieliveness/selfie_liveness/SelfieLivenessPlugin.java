package com.selfieliveness.selfie_liveness;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** SelfieLivenessPlugin */
public class SelfieLivenessPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler,  PluginRegistry.ActivityResultListener,PluginRegistry.RequestPermissionsResultListener  {

  ActivityPluginBinding binding;
  private SelfieDelegate delegate;
  FlutterPluginBinding flutterPluginBinding;
  private MethodChannel channel;
  private int PERMISSION_REQUEST_CODE=200;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "elatech_liveliness_plugin");
   this.flutterPluginBinding=flutterPluginBinding;
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("detectliveliness")) {
      detectLiveness(call,result);
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void detectLiveness(MethodCall call,Result result){
    FaceTrackerActivity.flutterPluginBinding=flutterPluginBinding;
   if(checkPermission(binding.getActivity())){
     initialize();
     delegate.detectLivelinesss(call, result);
    }
    else {
     requestPermission(binding.getActivity());
    }
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
  this.binding=binding;
    binding.addActivityResultListener(this);
    binding.addRequestPermissionsResultListener(this);
  }




  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    this.binding=binding;
    binding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivity() {

  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    delegate.onActivityResult(requestCode,resultCode,data);
    return false;
  }

  private boolean checkPermission(Context context) {
    int result1 = ContextCompat.checkSelfPermission(context, CAMERA);
    int result2 = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
    int result3 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
    return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
  }
  private void requestPermission(Activity activity) {

    ActivityCompat.requestPermissions(activity, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

  }


  public void initialize(){
    this.delegate= new SelfieDelegate(binding.getActivity());
  }

  @Override
  public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode==PERMISSION_REQUEST_CODE&&grantResults.length>0){
      for(int x=0; x<grantResults.length; x++){
        if(grantResults[x]!=PackageManager.PERMISSION_GRANTED){
         // Toast.makeText(binding.getActivity().getApplicationContext(),"Permission Not Yet Granted.",Toast.LENGTH_LONG).show();
         return false;
        }
      }
      initialize();
    }
    return true;

  }


  //app functions and method




}
