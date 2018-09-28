package sender.joycast.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.unitapp.R;

/**
 * Created by tehokang on 03/04/2017.
 */

public class PhotoSelectDialog extends Dialog {

    public class PhotoInfo {
        PhotoInfo(String title, String url) {
            this.title = title;
            this.url = url;
        }
        public String title;
        public String url;
    }

    public PhotoSelectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.photoselect_dialog);

        initView();

        initPhotoList();
    }

    @Override
    public void cancel() {
        super.cancel();

        m_listener.onPhotoClosed();
    }

    public interface Listener {
        void onPhotoSelected(String url);
        void onPhotoClosed();
    }

    public void setEventListener(PhotoSelectDialog.Listener listener) {
        m_listener = listener;
    }

    public class PhotoListAdapter extends BaseAdapter {
        private List<PhotoInfo> items;

        PhotoListAdapter(List<PhotoInfo> photoes) {
            items = photoes;
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
                convertView = inflater.inflate(R.layout.photoselect_dialog_item, parent, false);
            }

            PhotoInfo photo = items.get(position);

            TextView photo_title = (TextView) convertView.findViewById(R.id.photo_title);
            ImageView photo_iv = (ImageView) convertView.findViewById(R.id.photo);

            photo_title.setText(photo.title);
            Glide.with(context).load(photo.url).override(50,50).centerCrop().into(photo_iv);

            return convertView;
        }
    }

    private void initView() {
        m_photoes = new ArrayList<>();
        m_photoes_listview_adapter = new PhotoListAdapter(m_photoes);
        m_photoes_listview = (ListView)findViewById(R.id.photoList);
        m_photoes_listview.setAdapter(m_photoes_listview_adapter);
        m_photoes_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( m_listener != null ) {
                    m_listener.onPhotoSelected(m_photoes.get(position).url);
                }
            }
        });
    }

    private void initPhotoList() {
        int index=0;
        m_photoes.add(new PhotoInfo("SlamDumk", "http://10.0.14.169/thkang2/photo/1.jpg"));
        m_photoes.add(new PhotoInfo("SlamDumk", "http://10.0.14.169/thkang2/photo/2.jpg"));
        m_photoes.add(new PhotoInfo("SlamDumk", "http://10.0.14.169/thkang2/photo/3.jpg"));
        m_photoes.add(new PhotoInfo("Humax Art", "http://10.0.14.169/thkang2/photo/4.jpg"));
        m_photoes.add(new PhotoInfo("Humax Front", "http://10.0.14.169/thkang2/photo/5.jpg"));
        m_photoes.add(new PhotoInfo("Humax Young", "http://10.0.14.169/thkang2/photo/6.jpg"));
        m_photoes.add(new PhotoInfo("Humax Logo", "http://10.0.14.169/thkang2/photo/7.jpg"));
        m_photoes.add(new PhotoInfo("Teho", "http://10.0.14.169/thkang2/photo/8.jpg"));
        m_photoes.add(new PhotoInfo("Freesat", "http://10.0.14.169/thkang2/photo/9.jpg"));
        m_photoes.add(new PhotoInfo("HD-Nano", "http://10.0.14.169/thkang2/photo/10.jpg"));

        m_photoes.add(new PhotoInfo("마음의소리1", "http://10.0.14.169/thkang2/photo/11.jpg"));
        m_photoes.add(new PhotoInfo("마음의소리2", "http://10.0.14.169/thkang2/photo/12.jpg"));
        m_photoes.add(new PhotoInfo("ComicBook", "http://10.0.14.169/thkang2/photo/13.jpg"));
        m_photoes.add(new PhotoInfo("Marvel Hero", "http://10.0.14.169/thkang2/photo/14.jpg"));
        m_photoes.add(new PhotoInfo("Toy", "http://10.0.14.169/thkang2/photo/15.jpg"));
        m_photoes.add(new PhotoInfo("Marvel Heroes", "http://10.0.14.169/thkang2/photo/16.jpg"));
        m_photoes.add(new PhotoInfo("Marvel", "http://10.0.14.169/thkang2/photo/17.jpg"));
        m_photoes.add(new PhotoInfo("Korean Superman", "http://10.0.14.169/thkang2/photo/18.jpg"));
        m_photoes.add(new PhotoInfo("Hosoja", "http://10.0.14.169/thkang2/photo/19.jpg"));
        m_photoes.add(new PhotoInfo("Sewol", "http://10.0.14.169/thkang2/photo/20.jpg"));

        m_photoes.add(new PhotoInfo("Swiss", "http://10.0.14.169/thkang2/photo/21.jpg"));
        m_photoes.add(new PhotoInfo("한국 경치", "http://10.0.14.169/thkang2/photo/22.jpg"));
        m_photoes.add(new PhotoInfo("전라", "http://10.0.14.169/thkang2/photo/23.jpg"));
        m_photoes.add(new PhotoInfo("강원도", "http://10.0.14.169/thkang2/photo/24.jpg"));
        m_photoes.add(new PhotoInfo("Viking Line", "http://10.0.14.169/thkang2/photo/25.jpg"));
        m_photoes.add(new PhotoInfo("Silja Line", "http://10.0.14.169/thkang2/photo/26.jpg"));
        m_photoes.add(new PhotoInfo("Waterfall", "http://10.0.14.169/thkang2/photo/27.jpg"));
        m_photoes.add(new PhotoInfo("Autumn", "http://10.0.14.169/thkang2/photo/28.jpg"));
        m_photoes.add(new PhotoInfo("Geek", "http://10.0.14.169/thkang2/photo/29.jpg"));
        m_photoes.add(new PhotoInfo("Present", "http://10.0.14.169/thkang2/photo/30.jpg"));

        m_photoes_listview_adapter.notifyDataSetChanged();
    }

    private PhotoSelectDialog.Listener m_listener;
    private PhotoListAdapter m_photoes_listview_adapter;
    private ListView m_photoes_listview;
    private List<PhotoInfo> m_photoes;
}
