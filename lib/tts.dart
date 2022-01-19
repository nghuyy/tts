import 'dart:async';

import 'package:flutter/services.dart';

class Tts {
  static const MethodChannel _channel = MethodChannel('tts');


  Tts([String? lang]){
    lang ??= "vi";
     _channel.invokeMethod('init', <String, String>{"lang": lang});
  }


  Future<Tts> speak(String text) async {
    await _channel.invokeMethod('speak', <String, String>{"text": text});
    return this;
  }

  Future<Tts> stop() async {
    await _channel.invokeMethod('stop');
    return this;
  }
}
