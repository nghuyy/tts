import 'dart:async';

import 'package:flutter/services.dart';

class Tts {
  static const MethodChannel _channel = MethodChannel('tts');

  static Future<void> init(String lang) async {
    await _channel.invokeMethod('init', <String, String>{"lang": lang});
  }

  static Future<void> speak(String text) async {
    await _channel.invokeMethod('speak', <String, String>{"text": text});
  }

  static Future<void> get stop async {
    await _channel.invokeMethod('stop');
  }
}
