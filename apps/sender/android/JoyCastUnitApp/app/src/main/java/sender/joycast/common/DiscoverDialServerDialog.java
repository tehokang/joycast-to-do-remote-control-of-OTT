package sender.joycast.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.dial.DialServer;
import sender.joycast.unitapp.R;

/**
 * Created by tehokang on 28/03/2017.
 */

public class DiscoverDialServerDialog extends Dialog {
    public interface Listener {
        void onSelectedDialServer(DialServer dialserver);
    }

    public class DialServerListAdapter extends BaseAdapter {
        private List<DialServer> items;

        DialServerListAdapter(List<DialServer> dialservers) {
            items = dialservers;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position)  {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.discover_dialog_dialserver_item, parent, false);
            }

            TextView manufacturer = (TextView) convertView.findViewById(R.id.manufacturer);
            TextView model_name = (TextView) convertView.findViewById(R.id.model_name);
            TextView friendly_name = (TextView) convertView.findViewById(R.id.friendly_name);

            DialServer dialserver = items.get(position);

            manufacturer.setText(dialserver.getManufacturer());
            model_name.setText(dialserver.getModelName());
            friendly_name.setText(dialserver.getFriendlyName());

            return convertView;
        }
    }

    public DiscoverDialServerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();

        m_dialservers.clear();
        m_dialserver_listview_adapter.notifyDataSetChanged();
    }

    public void setEventListener(DiscoverDialServerDialog.Listener listener) {
        m_listener = listener;
    }

    public void updateDialServerList(List<DialServer> servers) {
        m_dialservers.clear();
        for (DialServer dialserver : servers) {
            m_dialservers.add(dialserver);
        }
        m_update_ui_handler.sendEmptyMessage(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.discover_dialog);

        initView();
    }

    private void initView() {
        m_dialservers = new ArrayList<>();
        m_dialserver_listview_adapter = new DialServerListAdapter(m_dialservers);
        m_dialserver_listview = (ListView)findViewById(R.id.dialserverList);
        m_dialserver_listview.setAdapter(m_dialserver_listview_adapter);
        m_dialserver_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( m_listener != null ) {
                    m_listener.onSelectedDialServer(m_dialservers.get(position));
                }
            }
        });
    }

    private Handler m_update_ui_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            m_dialserver_listview_adapter.notifyDataSetChanged();
        }
    };

    private DiscoverDialServerDialog.Listener m_listener;
    private List<DialServer> m_dialservers;
    private ListView m_dialserver_listview;
    private DialServerListAdapter m_dialserver_listview_adapter;
    private final String TAG = "DiscoverDialServerDialog";
}
