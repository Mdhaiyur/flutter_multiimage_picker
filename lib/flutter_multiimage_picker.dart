
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterMultiimagePicker {
  static const MethodChannel _channel = MethodChannel('flutter_multiimage_picker');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<dynamic>  openImagePicker({required int maxSelectable}) async{
    if (maxSelectable < 0) {
      throw  ArgumentError.value(maxSelectable, 'maxSelectable cannot be negative');
    }

    Map<String,dynamic> arguments={};
    arguments.putIfAbsent('maxSelectable', () => maxSelectable);

    final  images=await _channel.invokeMethod('openImagePicker',arguments);
    return images;
  }
}
