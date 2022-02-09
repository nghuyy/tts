package ngghuy.tts.tts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Queue;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * TtsPlugin
 */
public class TtsPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private MethodChannel channel;
    private TextToSpeech tts;
    private Activity activity;
    private Queue<String> listTalkTask = new ArrayDeque<>();
    private boolean isReading = false;
    private String lang;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "tts");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("init")) {
            if (call.argument("lang") == null) {
                initTTSEngineer("vi");
            }else{
                initTTSEngineer(call.argument("lang"));
            }
        } else if (call.method.equals("speak")) {
            listTalkTask.add(call.argument("text"));
            playTTS();
        } else if (call.method.equals("stop")) {
            stopVoice();
        } else {
            result.notImplemented();
        }
    }

    void initTTSEngineer(String lang) {
        this.lang = lang;
        if (tts == null) tts = new TextToSpeech(activity, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale(lang));
            }
        });
    }

    private void stopVoice() {
        if (listTalkTask != null) listTalkTask.clear();
        if (tts != null) tts.stop();
        isReading = false;
    }

    private void playTTS() {
        if (isReading) return;
        isReading = true;
        tts = new TextToSpeech(activity, status -> {
            String text = listTalkTask.poll();
            if (status == TextToSpeech.SUCCESS) {
                if (Build.VERSION.SDK_INT >= 15) {
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {

                        }

                        @Override
                        public void onDone(String s) {
                            isReading = false;
                            if (!listTalkTask.isEmpty()) {
                                playTTS();
                            }
                        }

                        @Override
                        public void onError(String s) {
                            isReading = false;
                        }
                    });
                }
                final AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                if (Build.VERSION.SDK_INT >= 21) {
                    String utteranceId = this.hashCode() + "";
                    Bundle params = new Bundle();
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
                    if (tts != null)
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId);
                    isReading = false;
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                    if (tts != null) tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
                    isReading = false;
                }
            }
        });
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
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
}
