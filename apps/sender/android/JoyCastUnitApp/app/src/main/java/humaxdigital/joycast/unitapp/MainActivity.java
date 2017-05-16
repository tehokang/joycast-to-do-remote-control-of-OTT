package humaxdigital.joycast.unitapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import humaxdigital.joycast.DiscoveryManager;
import humaxdigital.joycast.common.BaseActivity;
import humaxdigital.joycast.common.CustomDialog;
import humaxdigital.joycast.common.CustomPlayDialog;
import humaxdigital.joycast.common.CustomRelayConnectDialog;
import humaxdigital.joycast.common.DiscoverDialServerDialog;
import humaxdigital.joycast.common.PhotoSelectDialog;
import humaxdigital.joycast.common.QueryResponseWaitingDialog;
import humaxdigital.joycast.common.RcuDialog;
import humaxdigital.joycast.dial.DialServer;
import humaxdigital.joycast.session.Session;
import humaxdigital.joycast.testcases.UnitTestFactory;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();

        super.onCreate(savedInstanceState);
        /**
         * Hide status bar
         **/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        /**
         * Loading loading sound
         */
        initBackgroundMusic();

        /**
         * Initialize animation during loading
         */
        initAnimation();

        /**
         * Initialize view components
         */
        initView();

        /**
         * Initialize event handler working
         */
        initHandler();
    }

    @Override
    protected void initBackgroundMusic() {
        MediaPlayer.create(this, R.raw.loading).start();
    }

    @Override
    protected void initAnimation() {
        m_logo_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        m_subtitle_fadein_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        m_unittest_fadein_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        m_fadeout_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        m_logo_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                ((TextView)findViewById(R.id.subtitle)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.subtitle)).startAnimation(m_subtitle_fadein_anim);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        m_subtitle_fadein_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                Handler loadingHandler = new Handler();
                loadingHandler.postDelayed(new Runnable(){
                    public void run() {
                        m_loading_layout.setVisibility(View.GONE);
                        m_loading_layout.startAnimation(m_fadeout_anim);
                        m_unittest_layout.setVisibility(View.VISIBLE);
                        m_unittest_layout.startAnimation(m_unittest_fadein_anim);
                        enableController(m_cast_layout, false);
                    }
                }, 300);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    @Override
    protected void initView() {
        m_loading_layout = (LinearLayout)findViewById(R.id.enterance);
        m_unittest_layout = (LinearLayout)findViewById(R.id.unit_test_layout);
        m_cast_layout = (LinearLayout)findViewById(R.id.cast_controller);

        m_unittest_layout.setVisibility(View.GONE);

        Handler loadingHandler = new Handler();
        loadingHandler.postDelayed(new Runnable() {
            public void run() {
                m_loading_layout.setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.logo)).startAnimation(m_logo_anim);
            }
        },1000);

        m_discover_dialserver_dialog = new DiscoverDialServerDialog(this);
        m_custom_message_dialog = new CustomDialog(this);
        m_query_response_waiting_dialog = new QueryResponseWaitingDialog(this);
        m_rcu_dialog = new RcuDialog(this);
        m_custom_play_dialog = new CustomPlayDialog(this);
        m_custom_relay_connect_dialog = new CustomRelayConnectDialog(this);
        m_photo_select_dialog = new PhotoSelectDialog(this);
    }

    @Override
    protected void initHandler() {
        m_unittest_factory.setOnDurationChangedListener(new UnitTestFactory.OnDurationChangedListener() {
            @Override
            public void onDurationChanged(double duration) {
                Message msg = m_update_ui_handler.obtainMessage();
                msg.what = UPDATE_DURATION;
                msg.obj = duration;
                m_update_ui_handler.sendMessage(msg);
            }
        });

        m_unittest_factory.setOnPositionChangedListener(new UnitTestFactory.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(double position) {
                Message msg = m_update_ui_handler.obtainMessage();
                msg.what = UPDATE_POSITON;
                msg.obj = position;
                m_update_ui_handler.sendMessage(msg);
            }
        });

        m_discover_dialserver_dialog.setEventListener(new DiscoverDialServerDialog.Listener() {
            @Override
            public void onSelectedDialServer(DialServer dialserver) {
                m_selected_dialserver = dialserver;
                m_update_ui_handler.sendEmptyMessage(HIDE_DIAL_DISCOVER_DIALOG);
                m_update_ui_handler.sendEmptyMessage(ENABLE_DIAL_LAUNCH);
            }
        });

        m_photo_select_dialog.setEventListener(new PhotoSelectDialog.Listener(){
            @Override
            public void onPhotoSelected(String url) {
                m_unittest_factory.pause();
                m_unittest_factory.loadPhoto(url);
            }

            @Override
            public void onPhotoClosed() {
                m_unittest_factory.stop();
            }
        });

        ((Button)findViewById(R.id.discover)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByKorean("다이얼 서버를 찾아볼께요, JoyCast 스틱을 켜놓으세요");

                m_update_ui_handler.sendEmptyMessage(SHOW_DIAL_DISCOVER_DIALOG);
                m_unittest_factory.setOnSearchedDialServerListener(new UnitTestFactory.OnSearchedDialServerListener() {
                    @Override
                    public void onSearched(DiscoveryManager discoveryManager, DialServer server) {
                        List<DialServer> servers = discoveryManager.getDiscoveredDialServerList();

                        Message msg = m_update_ui_handler.obtainMessage();
                        msg.what = UPDATE_DISCOVER_RESULT;
                        msg.obj = servers;
                        m_update_ui_handler.sendMessage(msg);
                    }
                });
                m_unittest_factory.discover();
            }
        });

        ((Button)findViewById(R.id.launch_joycast)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        if ( m_selected_dialserver != null ) {
                            boolean result = m_unittest_factory.launchJoyCast(m_selected_dialserver);
                            Message msg = m_update_ui_handler.obtainMessage();
                            msg.what = UPDATE_LAUNCH_RESULT;
                            String uiMessage, speechKorean;
                            if ( result ) {
                                uiMessage = "JoyCast launched on " + m_selected_dialserver.getModelName();
                                speechKorean = "조이캐스트가 정상 실행되었습니다";
                            } else {
                                uiMessage = "JoyCast couldn't launch on " + m_selected_dialserver.getModelName();
                                speechKorean = "조이캐스트가 실행되지 않았습니다";
                            }
                            speechByKorean(speechKorean);
                            msg.obj = uiMessage;
                            m_update_ui_handler.sendMessage(msg);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No dialserver selected or already launched"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });

        ((Button)findViewById(R.id.stop_joycast)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        m_unittest_factory.stopJoyCast(m_selected_dialserver);
                        Message msg = m_update_ui_handler.obtainMessage();
                        msg.what = UPDATE_LAUNCH_RESULT;

                        speechByKorean("조이캐스트를 종료합니다");
                        msg.obj = "JoyCast has stopped " + m_selected_dialserver.getModelName();
                        m_update_ui_handler.sendMessage(msg);
                    }
                }).start();
            }
        });

        ((Button)findViewById(R.id.launch_youtube)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (m_selected_dialserver != null) {
                            boolean result = m_unittest_factory.launchYouTube(m_selected_dialserver);
                            Message msg = m_update_ui_handler.obtainMessage();
                            msg.what = UPDATE_LAUNCH_RESULT;
                            String uiMessage, speechKorean;
                            if (result) {
                                uiMessage = "YouTube launched on " + m_selected_dialserver.getModelName();
                                speechKorean = "유투브가 정상 실행되었습니다";
                            } else {
                                uiMessage = "YouTube couldn't launch on " + m_selected_dialserver.getModelName();
                                speechKorean = "유투브가 실행되지 않았습니다";
                            }
                            speechByKorean(speechKorean);
                            msg.obj = uiMessage;
                            m_update_ui_handler.sendMessage(msg);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No dialserver selected or already launched"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });

        ((Button)findViewById(R.id.stop_youtube)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        m_unittest_factory.stopYouTube(m_selected_dialserver);
                        Message msg = m_update_ui_handler.obtainMessage();
                        msg.what = UPDATE_LAUNCH_RESULT;

                        speechByKorean("유투브를 종료합니다");
                        msg.obj = "YouTube has stopped " + m_selected_dialserver.getModelName();
                        m_update_ui_handler.sendMessage(msg);
                    }
                }).start();
            }
        });

        ((Button)findViewById(R.id.connect_relay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.connect_relay)).getText() + "server");

                m_update_ui_handler.sendEmptyMessage(SHOW_RELAY_CONNECTING_DIALOG);
                m_unittest_factory.setOnConnectedSessionListener(new UnitTestFactory.OnConnectedSessionListener() {
                    @Override
                    public void onConnectedSession(Session session) {
                        speechByEnglish("릴레이 서버와 연결되었습니다");
                        m_update_ui_handler.sendEmptyMessage(RELAY_CONNECTED);
                        m_update_ui_handler.sendEmptyMessage(SHOW_WAITING_QUERY_RESPONSE);
                        m_update_ui_handler.sendEmptyMessage(HIDE_RELAY_CONNECTING_DIALOG);
                    }
                });

                m_unittest_factory.setOnAvControllerReadyListener(new UnitTestFactory.OnAvControllerReadyListener(){
                    @Override
                    public void onAvControllerReady() {
                        speechByEnglishAsAdding("Video Controller Ready");
                        m_update_ui_handler.sendEmptyMessage(ENABLE_CAST_CONTROLLER);
                        m_update_ui_handler.sendEmptyMessage(HIDE_WAITING_QUERY_RESPONSE);
                    }
                });

                m_unittest_factory.setOnPhotoControllerReadyListener(new UnitTestFactory.OnPhotoControllerReadyListener() {
                    @Override
                    public void onPhotoControllerReady() {
                        speechByEnglishAsAdding("Photo Controller Ready");
                    }
                });

                m_unittest_factory.setOnSessionEndedListener(new UnitTestFactory.OnSessionEndedListener() {
                    @Override
                    public void onSessionEnded() {
                        speechByKorean("릴레이 서버와 연결이 끊어졌습니다");
                        m_update_ui_handler.sendEmptyMessage(RELAY_DISCONNECTED);
                        m_update_ui_handler.sendEmptyMessage(DISABLE_CAST_CONTROLLER);
                        m_update_ui_handler.sendEmptyMessage(HIDE_RELAY_CONNECTING_DIALOG);
                    }
                });
                m_unittest_factory.connect(m_selected_dialserver, "");
            }
        });

        ((Button)findViewById(R.id.connect_custom_relay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.connect_custom_relay)).getText() + "relay server");
                m_custom_relay_connect_dialog.show();
                m_custom_relay_connect_dialog.setOnConnectCustomRelay(new CustomRelayConnectDialog.OnConnectCustomRelayListener() {
                    @Override
                    public void OnConnectCustomRelay(String ip, int port, String protocol) {
                        m_unittest_factory.setOnConnectedSessionListener(new UnitTestFactory.OnConnectedSessionListener() {
                            @Override
                            public void onConnectedSession(Session session) {
                                speechByEnglish("릴레이 서버와 연결되었습니다");
                                m_update_ui_handler.sendEmptyMessage(RELAY_CONNECTED);
                                m_update_ui_handler.sendEmptyMessage(SHOW_WAITING_QUERY_RESPONSE);
                                m_update_ui_handler.sendEmptyMessage(HIDE_RELAY_CONNECTING_DIALOG);
                            }
                        });

                        m_unittest_factory.setOnAvControllerReadyListener(new UnitTestFactory.OnAvControllerReadyListener(){
                            @Override
                            public void onAvControllerReady() {
                                speechByKorean("비디오 컨트롤러가 준비되었습니다");
                                m_update_ui_handler.sendEmptyMessage(ENABLE_CAST_CONTROLLER);
                                m_update_ui_handler.sendEmptyMessage(HIDE_WAITING_QUERY_RESPONSE);
                            }
                        });

                        m_unittest_factory.setOnPhotoControllerReadyListener(new UnitTestFactory.OnPhotoControllerReadyListener() {
                            @Override
                            public void onPhotoControllerReady() {
                                speechByKorean("포토 컨트롤러가 준비되었습니다");
                            }
                        });
                        m_unittest_factory.setOnSessionEndedListener(new UnitTestFactory.OnSessionEndedListener() {
                            @Override
                            public void onSessionEnded() {
                                speechByKorean("릴레이 서버와 연결이 끊어졌습니다");
                                m_update_ui_handler.sendEmptyMessage(RELAY_DISCONNECTED);
                                m_update_ui_handler.sendEmptyMessage(DISABLE_CAST_CONTROLLER);
                                m_update_ui_handler.sendEmptyMessage(HIDE_RELAY_CONNECTING_DIALOG);
                            }
                        });
                        m_unittest_factory.connect(ip, port, protocol);
                        m_custom_relay_connect_dialog.hide();
                    }
                });
            }
        });

        ((Button)findViewById(R.id.health_check)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                m_unittest_factory.setOnHummingAlivedListener(new UnitTestFactory.OnHummingAlivedListener() {
                    @Override
                    public void onHummingAlived() {
                        m_update_ui_handler.sendEmptyMessage(SHOW_HEALTH_CHECKED_NORMAL);
                    }
                });

                m_unittest_factory.setOnHummingNotAlivedListener(new UnitTestFactory.OnHummingNotAlivedListener() {
                    @Override
                    public void onHummingNotAlived() {
                        m_update_ui_handler.sendEmptyMessage(SHOW_HEALTH_CHECKED_ABNORMAL);
                    }
                });

                if ( false == m_unittest_factory.healthCheck(3000) ) {
                    m_update_ui_handler.sendEmptyMessage(SHOW_HEALTH_CHECKED_ABNORMAL);
                }
            }
        });

        ((Button)findViewById(R.id.play_source_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_source_1)).getText().toString());
                m_unittest_factory.playSource1();
            }
        });

        ((Button)findViewById(R.id.play_source_2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_source_2)).getText().toString());
                m_unittest_factory.playSource2();
            }
        });

        ((Button)findViewById(R.id.play_source_3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_source_3)).getText().toString());
                m_unittest_factory.playSource3();
            }
        });

        ((Button)findViewById(R.id.play_source_custom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_source_custom)).getText().toString());
                m_custom_play_dialog.setOnPlayCustomListener(new CustomPlayDialog.OnPlayCustomListener() {
                    @Override
                    public void OnPlayCustom(String title, String url) {
                        m_unittest_factory.playSourceCustom(title, url);
                        m_custom_play_dialog.hide();
                    }
                });
                m_custom_play_dialog.show();
            }
        });

        ((Button)findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play)).getText().toString());
                m_unittest_factory.play();
            }
        });

        ((Button)findViewById(R.id.pause)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.pause)).getText().toString());
                m_unittest_factory.pause();
            }
        });

        ((Button)findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.stop)).getText().toString());
                m_unittest_factory.setOnStoppedListener(new UnitTestFactory.OnStoppedListener() {
                    @Override
                    public void onStopped() {
                        clearDurationPosition();
                    }
                });
                m_unittest_factory.stop();
            }
        });

        ((Button)findViewById(R.id.mute)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.mute)).getText().toString());
                m_unittest_factory.mute();
            }
        });

        ((Button)findViewById(R.id.unmute)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.unmute)).getText().toString());
                m_unittest_factory.unmute();
            }
        });

        ((Button)findViewById(R.id.play_faster)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_faster)).getText().toString());
                m_unittest_factory.playFaster();
            }
        });

        ((Button)findViewById(R.id.play_slower)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.play_slower)).getText().toString());
                m_unittest_factory.playSlower();
            }
        });

        ((Button)findViewById(R.id.photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.photo)).getText().toString());
                clearDurationPosition();

                m_update_ui_handler.sendEmptyMessage(SHOW_PHOTOSELECT_DIALOG);

                m_unittest_factory.setOnPhotoUpdateListener(new UnitTestFactory.OnPhotoUpdateListener() {
                    @Override
                    public void onLoaded(String url) {
                        Message msg = m_update_ui_handler.obtainMessage();
                        msg.what = LOADED_PHOTO;
                        msg.obj = url;
                        m_update_ui_handler.sendMessage(msg);
                    }

                    @Override
                    public void onUnloaded() {
                        m_update_ui_handler.sendEmptyMessage(UNLOADED_PHOTO);
                    }
                });
            }
        });

        ((Button)findViewById(R.id.custom_message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.custom_message)).getText().toString());
                /* Pop dialog to input message */
                m_update_ui_handler.sendEmptyMessage(SHOW_CUSTOM_MESSAGE_DIALOG);
                m_custom_message_dialog.setOnRequestSendCustomMessageListener(
                        new CustomDialog.OnRequestSendCustomMessageListener() {
                    @Override
                    public void onRequestSendCustomMessage(String name, String value) {
                        m_unittest_factory.customMessage(name, value);
                        m_update_ui_handler.sendEmptyMessage(HIDE_CUSTOM_MESSAGE_DIALOG);
                    }
                });
            }
        });

        ((Button)findViewById(R.id.seek_minus_5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.seek_minus_5)).getText().toString());
                m_unittest_factory.jumpBackward();
            }
        });

        ((Button)findViewById(R.id.seek_plus_5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.seek_plus_5)).getText().toString());
                m_unittest_factory.jumpForward();
            }
        });

        ((Button)findViewById(R.id.volume_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.volume_up)).getText().toString());
                m_unittest_factory.volumeUp();
            }
        });

        ((Button)findViewById(R.id.volume_down)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.volume_down)).getText().toString());
                m_unittest_factory.volumeDown();
            }
        });

        ((Button)findViewById(R.id.display_320_240)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.display_320_240)).getText().toString());
                m_unittest_factory.display320x240();
            }
        });

        ((Button)findViewById(R.id.display_720_480)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.display_720_480)).getText().toString());
                m_unittest_factory.display720x480();
            }
        });

        ((Button)findViewById(R.id.display_fullscreen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.display_fullscreen)).getText().toString());
                m_unittest_factory.displayFullscreen();
            }
        });

        ((Button)findViewById(R.id.rcu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechByEnglish(((Button) findViewById(R.id.rcu)).getText().toString());
                m_update_ui_handler.sendEmptyMessage(SHOW_RCU_DIALOG);
            }
        });

        ((SeekBar)findViewById(R.id.playbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /**
                 * Overhead here
                 */
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                m_unittest_factory.seek(seekBar.getProgress());
            }
        });
    }

    private String getDateFormatted(double seconds) {
        long _seconds = (long)seconds;
        long absSeconds = Math.abs(_seconds);
        String positive = String.format(Locale.ENGLISH,
                "%02d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private void clearDurationPosition() {
        Message msg1 = m_update_ui_handler.obtainMessage();
        msg1.what = UPDATE_DURATION;
        msg1.obj = 0.0d;
        m_update_ui_handler.sendMessage(msg1);

        Message msg2 = m_update_ui_handler.obtainMessage();
        msg2.what = UPDATE_POSITON;
        msg2.obj = 0.0d;
        m_update_ui_handler.sendMessage(msg2);
    }

    private final Handler m_update_ui_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DIAL_DISCOVER_DIALOG:
                    m_discover_dialserver_dialog.show();
                    break;
                case HIDE_DIAL_DISCOVER_DIALOG:
                    m_discover_dialserver_dialog.hide();
                    break;
                case UPDATE_DISCOVER_RESULT:
                    List<DialServer> servers = (ArrayList<DialServer>)msg.obj;
                    m_discover_dialserver_dialog.updateDialServerList(servers);
                    break;
                case ENABLE_DIAL_LAUNCH:
                    ((Button)findViewById(R.id.launch_joycast)).setEnabled(true);
                    ((Button)findViewById(R.id.launch_youtube)).setEnabled(true);
                    ((Button)findViewById(R.id.stop_joycast)).setEnabled(true);
                    ((Button)findViewById(R.id.stop_youtube)).setEnabled(true);
                    ((Button)findViewById(R.id.connect_relay)).setEnabled(true);
                    break;
                case RELAY_CONNECTED:
                    {
                        String message =
                                "Connected to " +
                                m_unittest_factory.getCurrentSession().getConnectedServerUri();
                        ((TextView) findViewById(R.id.connection_status)).setText(message);
                    }
                    break;
                case RELAY_DISCONNECTED:
                    {
                        String message = "Diconnected...";
                        ((TextView) findViewById(R.id.connection_status)).setText(message);
                    }
                    break;
                case ENABLE_CAST_CONTROLLER:
                    enableController(m_cast_layout, true);
                    break;
                case DISABLE_CAST_CONTROLLER:
                    enableController(m_cast_layout, false);
                    clearDurationPosition();
                    break;
                case UPDATE_DURATION:
                    long max = Long.valueOf(new Double((double)msg.obj).longValue());
                    ((SeekBar)findViewById(R.id.playbar)).setMax((int)max);
                    ((TextView)findViewById(R.id.current_duration)).setText(getDateFormatted(max));
                    break;
                case UPDATE_POSITON:
                    long position = Long.valueOf(new Double((double)msg.obj).longValue());
                    ((SeekBar)findViewById(R.id.playbar)).setProgress((int)position);
                    ((TextView)findViewById(R.id.current_position)).setText(getDateFormatted(position));
                    break;
                case UPDATE_LAUNCH_RESULT:
                    ((TextView)findViewById(R.id.launch_result)).setText((String)msg.obj);
                    break;
                case SHOW_CUSTOM_MESSAGE_DIALOG:
                    m_custom_message_dialog.show();
                    break;
                case HIDE_CUSTOM_MESSAGE_DIALOG:
                    m_custom_message_dialog.hide();
                    break;
                case SHOW_RELAY_CONNECTING_DIALOG:
                    ((LinearLayout)findViewById(R.id.connecting_progress)).setVisibility(View.VISIBLE);
                    break;
                case HIDE_RELAY_CONNECTING_DIALOG:
                    ((LinearLayout)findViewById(R.id.connecting_progress)).setVisibility(View.GONE);
                    break;
                case SHOW_WAITING_QUERY_RESPONSE:
                    m_query_response_waiting_dialog.show();
                    break;
                case HIDE_WAITING_QUERY_RESPONSE:
                    m_query_response_waiting_dialog.hide();
                    break;
                case SHOW_RCU_DIALOG:
                    m_rcu_dialog.show();
                    break;
                case SHOW_PHOTOSELECT_DIALOG:
                    m_photo_select_dialog.show();
                    break;
                case HIDE_PHOTOSELECT_DIALOG:
                    m_photo_select_dialog.hide();
                    break;
                case LOADED_PHOTO:
                    Toast.makeText(getApplicationContext(),
                            "Loaded Photo " + String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    break;
                case UNLOADED_PHOTO:
                    Toast.makeText(getApplicationContext(),
                            "Unloaded Photo", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_HEALTH_CHECKED_NORMAL:
                    Toast.makeText(getApplicationContext(),
                            "Humming is still alived", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_HEALTH_CHECKED_ABNORMAL:
                    Toast.makeText(getApplicationContext(),
                            "Please connect to humming", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private LinearLayout m_loading_layout;
    private LinearLayout m_unittest_layout;
    private LinearLayout m_cast_layout;

    private DiscoverDialServerDialog m_discover_dialserver_dialog;
    private DialServer m_selected_dialserver;

    private CustomDialog m_custom_message_dialog;
    private QueryResponseWaitingDialog m_query_response_waiting_dialog;
    private RcuDialog m_rcu_dialog;
    private CustomPlayDialog m_custom_play_dialog;
    private CustomRelayConnectDialog m_custom_relay_connect_dialog;
    private PhotoSelectDialog m_photo_select_dialog;

    private static final int RELAY_CONNECTED = 550;
    private static final int RELAY_DISCONNECTED = 435;
    private static final int ENABLE_DIAL_LAUNCH = 96;
    private static final int ENABLE_CAST_CONTROLLER = 734;
    private static final int DISABLE_CAST_CONTROLLER = 653;
    private static final int UPDATE_DURATION = 86;
    private static final int UPDATE_POSITON = 929;
    private static final int UPDATE_LAUNCH_RESULT = 317;
    private static final int UPDATE_DISCOVER_RESULT = 799;
    private static final int SHOW_DIAL_DISCOVER_DIALOG = 407;
    private static final int HIDE_DIAL_DISCOVER_DIALOG = 852;
    private static final int SHOW_CUSTOM_MESSAGE_DIALOG = 622;
    private static final int HIDE_CUSTOM_MESSAGE_DIALOG = 291;
    private static final int SHOW_RELAY_CONNECTING_DIALOG = 111;
    private static final int HIDE_RELAY_CONNECTING_DIALOG = 631;
    private static final int SHOW_WAITING_QUERY_RESPONSE = 790;
    private static final int HIDE_WAITING_QUERY_RESPONSE = 690;
    private static final int SHOW_PHOTOSELECT_DIALOG = 729;
    private static final int HIDE_PHOTOSELECT_DIALOG = 837;
    private static final int LOADED_PHOTO = 309;
    private static final int UNLOADED_PHOTO = 63;
    private static final int SHOW_RCU_DIALOG = 170;
    private static final int SHOW_HEALTH_CHECKED_NORMAL = 58;
    private static final int SHOW_HEALTH_CHECKED_ABNORMAL = 36;

    private Animation m_logo_anim, m_subtitle_fadein_anim, m_fadeout_anim, m_unittest_fadein_anim;

    private UnitTestFactory m_unittest_factory = new UnitTestFactory();
    private final String TAG = "MainActivity";
}
