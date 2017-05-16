package humaxdigital.joycast.common;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * Created by tehokang on 31/03/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initBackgroundMusic();
    protected abstract void initAnimation();
    protected abstract void initView();
    protected abstract void initHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_text_to_speech_eng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ) {
                    m_text_to_speech_eng.setLanguage(Locale.ENGLISH);
                }
            }
        });

        m_text_to_speech_kor = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ) {
                    m_text_to_speech_kor.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    protected void speechByEnglish(String text) {
        m_text_to_speech_eng.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    protected void speechByKorean(String text) {
        m_text_to_speech_kor.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    protected void speechByEnglishAsAdding(String text) {
        m_text_to_speech_eng.speak(text, TextToSpeech.QUEUE_ADD, null, "");
    }

    protected void speechByKoreanAsAdding(String text) {
        m_text_to_speech_kor.speak(text, TextToSpeech.QUEUE_ADD, null, "");
    }

    protected void enableController(View view, boolean enable) {
        view.setEnabled(enable);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableController(group.getChildAt(idx), enable);
            }
        }
    }

    protected TextToSpeech m_text_to_speech_eng;
    protected TextToSpeech m_text_to_speech_kor;
}
