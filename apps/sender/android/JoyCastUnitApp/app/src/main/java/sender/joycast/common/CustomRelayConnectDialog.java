package sender.joycast.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by tehokang on 31/03/2017.
 */

public class CustomRelayConnectDialog extends CustomDialog {
    public interface OnConnectCustomRelayListener {
        void OnConnectCustomRelay(String ip, int port, String protocol);
    }
    public CustomRelayConnectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initHandler();
    }

    private void initView() {
        m_name_label_tv.setText("IP :");
        m_value1_label_tv.setText("PORT :");
        m_value2_label_tv.setText("Protocol :");
        m_value2_label_tv.setVisibility(View.VISIBLE);

        m_name_et.setHint("relay server ip");
        m_value1_et.setHint("relay server port");
        m_value2_et.setHint("websocket protocol");
        m_value2_et.setVisibility(View.VISIBLE);

        m_button1.setText("Connect");
        m_button2.setText("Cancel");
    }

    private void initHandler() {
        m_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_listener.OnConnectCustomRelay(
                        m_name_et.getText().toString(),
                        Integer.valueOf(m_value1_et.getText().toString()),
                        m_value2_et.getText().toString());
            }
        });

        m_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public void setOnConnectCustomRelay(OnConnectCustomRelayListener listener) {
        m_listener = listener;
    }

    private OnConnectCustomRelayListener m_listener;
}
