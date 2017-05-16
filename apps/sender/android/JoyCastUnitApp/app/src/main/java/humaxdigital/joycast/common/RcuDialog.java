package humaxdigital.joycast.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Locale;

import humaxdigital.joycast.unitapp.R;

/**
 * Created by tehokang on 30/03/2017.
 */

public class RcuDialog extends Dialog {
    public RcuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.rcu_dialog);

        initView();
    }

    private void initView() {
        m_text_to_speech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ) {
                    m_text_to_speech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        ((Button)findViewById(R.id.rcu_arrow_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_arrow_up)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_arrow_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_arrow_left)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_arrow_enter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_arrow_enter)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_arrow_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_arrow_right)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_arrow_down)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_arrow_down)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_red)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "red", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_blue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "blue", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_green)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "green", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_yellow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "yellow", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_rewind)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "rewind", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        ((Button) findViewById(R.id.rcu_play)).getText(),
                        TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_pause)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "pause", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_fastfoward)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "fastforward", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });

        ((Button)findViewById(R.id.rcu_stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_text_to_speech.speak(
                        "stop", TextToSpeech.QUEUE_FLUSH, null, "");
            }
        });
    }

    private TextToSpeech m_text_to_speech;
}
