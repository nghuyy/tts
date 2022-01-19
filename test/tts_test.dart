import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:tts/tts.dart';

void main() {
  const MethodChannel channel = MethodChannel('tts');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Tts.platformVersion, '42');
  });
}
