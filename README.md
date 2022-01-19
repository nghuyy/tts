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

  Tts tts = Tts("en");
  tts.speak("hello");
  
  tts.stop();

```