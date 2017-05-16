package humaxdigital.joycast.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by tehokang on 31/03/2017.
 */

public class CustomPlayDialog extends CustomDialog {
    public interface OnPlayCustomListener {
        void OnPlayCustom(String title, String url);
    }

    public CustomPlayDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initHandler();
    }

    private void initView() {
        m_name_label_tv.setText("Title : ");
        m_value1_label_tv.setText("URL : ");
        m_name_et.setHint("content title");
        m_value1_et.setHint("content address");
        m_button1.setText("Play");
        m_button2.setText("Cancel");
    }

    private void initHandler() {
        m_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_listener.OnPlayCustom(
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

    public void setOnPlayCustomListener(OnPlayCustomListener listener) {
        m_listener = listener;
    }

    private OnPlayCustomListener m_listener;
}
