package humaxdigital.joycast.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import humaxdigital.joycast.unitapp.R;

/**
 * Created by tehokang on 30/03/2017.
 */

public class CustomDialog extends Dialog {

    public interface OnRequestSendCustomMessageListener {
        void onRequestSendCustomMessage(String name, String value);
    }

    public void setOnRequestSendCustomMessageListener(OnRequestSendCustomMessageListener listener) {
        m_listener = listener;
    }

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void hide() {
        super.hide();

        m_name_et.setText("");
        m_value1_et.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        initView();

        initHandler();
    }

    private void initView() {
        m_name_et = (EditText)findViewById(R.id.name);
        m_value1_et = (EditText)findViewById(R.id.value1);
        m_value2_et = (EditText)findViewById(R.id.value2);
        m_button1 = (Button)findViewById(R.id.button1);
        m_button2 = (Button)findViewById(R.id.button2);

        m_name_label_tv = (TextView)findViewById(R.id.label_name);
        m_value1_label_tv = (TextView)findViewById(R.id.label_value1);
        m_value2_label_tv = (TextView)findViewById(R.id.label_value2);

    }

    private void initHandler() {
        m_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_listener.onRequestSendCustomMessage(
                        m_name_et.getText().toString(),
                        m_value1_et.getText().toString());
            }
        });

        m_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    protected EditText m_name_et;
    protected EditText m_value1_et;
    protected EditText m_value2_et;
    protected Button m_button1;
    protected Button m_button2;
    protected TextView m_name_label_tv;
    protected TextView m_value1_label_tv;
    protected TextView m_value2_label_tv;
    private OnRequestSendCustomMessageListener m_listener;

}
