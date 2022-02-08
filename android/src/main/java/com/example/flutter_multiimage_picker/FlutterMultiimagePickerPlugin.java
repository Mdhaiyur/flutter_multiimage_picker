package com.example.flutter_multiimage_picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** FlutterMultiimagePickerPlugin */
public class FlutterMultiimagePickerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context mContext;
  private Activity mActivity;
  private Result mResult;
  private static  final  int REQUEST_CODE_CHOOSE=101;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_multiimage_picker");
    channel.setMethodCallHandler(this);
    mContext=flutterPluginBinding.getApplicationContext();
  }



  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    mResult=result;
    if (call.method.equals("getPlatformVersion")) {
      mResult.success("Android" + android.os.Build.VERSION.RELEASE);
    }else if(call.method.equals("openImagePicker")){

      int maxSelectable=call.argument("maxSelectable");
      openImagePicker(maxSelectable);
    }
    else {
      mResult.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  void openImagePicker(int maxSelectable)
  {
    Matisse.from(mActivity)
            .choose(MimeType.ofAll())
            .countable(true)
            .maxSelectable(maxSelectable)
            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
            .gridExpectedSize(mActivity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .capture(true)
            .captureStrategy(
                    new CaptureStrategy(true, BuildConfig.LIBRARY_PACKAGE_NAME + ".provider"))
            .imageEngine(new GlideEngine())
            .showPreview(false)
            .forResult(REQUEST_CODE_CHOOSE);

  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    binding.addActivityResultListener(this);
    mActivity= binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
     List<String> mSelectedImages = Matisse.obtainPathResult(data);
     mResult.success(mSelectedImages);
    }
    return false;
  }
}
