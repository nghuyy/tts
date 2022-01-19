import 'dart:async';

import 'package:flutter/services.dart';

class Tts {
  static const MethodChannel _channel = MethodChannel('tts');

  static Future<void> init(String lang) async {
    await _channel.invokeMethod('init', <String, String>{"lang": lang});
  }
}
