# tts

Android TTS Helper

## Getting Started
```yaml
 tts:
        git:
            url: https://github.com/nghuyy/tts.git
            ref: main
```

```dart
  import "package:tts/tts.dart"

  tts.init("en");
  tts.speak("hello");
  
  tts.stop();

```