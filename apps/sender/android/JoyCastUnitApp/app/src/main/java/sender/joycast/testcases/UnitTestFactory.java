package sender.joycast.testcases;

import sender.joycast.DiscoveryManager;
import sender.joycast.SessionManager;
import sender.joycast.constant.Configuration;
import sender.joycast.controller.AVController;
import sender.joycast.controller.Controller;
import sender.joycast.controller.PhotoController;
import sender.joycast.dial.DialServer;
import sender.joycast.session.CastSession;
import sender.joycast.session.Session;
import sender.joycast.util.CastLogger;

import static android.content.ContentValues.TAG;

/**
 * Created by tehokang on 28/03/2017.
 */

public class UnitTestFactory {
    public interface OnSearchedDialServerListener {
        void onSearched(DiscoveryManager discoveryManager, DialServer server);
    };

    public interface OnConnectedSessionListener {
        void onConnectedSession(Session session);
    };

    public interface OnAvControllerReadyListener {
        void onAvControllerReady();
    };

    public interface OnPhotoControllerReadyListener {
        void onPhotoControllerReady();
    }

    public interface OnDurationChangedListener {
        void onDurationChanged(double duration);
    }

    public interface OnPositionChangedListener {
        void onPositionChanged(double position);
    }

    public interface OnStoppedListener {
        void onStopped();
    }

    public interface OnSessionEndedListener {
        void onSessionEnded();
    }

    public interface OnHummingAlivedListener {
        void onHummingAlived();
    }

    public interface OnHummingNotAlivedListener {
        void onHummingNotAlived();
    }

    public interface OnPhotoUpdateListener {
        void onLoaded(String url);
        void onUnloaded();
    }

    public void setOnHummingAlivedListener(OnHummingAlivedListener listener) {
        m_listener_humming_alived = listener;
    }

    public void setOnHummingNotAlivedListener(OnHummingNotAlivedListener listener) {
        m_listener_humming_not_alived = listener;
    }

    public void setOnPhotoUpdateListener(OnPhotoUpdateListener listener ) {
        m_listener_onphotoupdate = listener;
    }

    public void setOnSessionEndedListener(OnSessionEndedListener listener) {
        m_listener_onsessionended = listener;
    }

    public void setOnStoppedListener(OnStoppedListener listener) {
        m_listener_onstopped = listener;
    }

    public void setOnSearchedDialServerListener(OnSearchedDialServerListener listener) {
        m_listener_onsearched_dialserver = listener;
    }

    public void setOnConnectedSessionListener(OnConnectedSessionListener listener) {
        m_listener_onconnected_session = listener;
    }

    public void setOnAvControllerReadyListener(OnAvControllerReadyListener listener) {
        m_listener_onavcontroller_ready = listener;
    }

    public void setOnPhotoControllerReadyListener(OnPhotoControllerReadyListener listener) {
        m_listener_onphotocontroller_ready = listener;
    }

    public void setOnDurationChangedListener(OnDurationChangedListener listener ) {
        m_listener_onduration_changed = listener;
    }

    public void setOnPositionChangedListener(OnPositionChangedListener listener) {
        m_listener_onposition_changed = listener;
    }

    public void discover() {
        CastLogger.d(TAG, "Discovering ...");
        /**
         * remove previous handler already registered
         */
        m_discovery_manager.stopDiscovering();
        m_discovery_manager.removeEventListener(m_discovery_manager_listener);
        m_discovery_manager.addEventListener(m_discovery_manager_listener);
        m_discovery_manager.discover();
    }

    public boolean launchJoyCast(DialServer server) {
        CastLogger.d(TAG, "Launching ...");
        m_discovery_manager.stopDiscovering();
        return m_discovery_manager.launch(server, Configuration.JOYCAST);
    }

    public boolean stopJoyCast(DialServer server) {
        CastLogger.d(TAG, "Stopping...");
        return m_discovery_manager.stopApplication(server, Configuration.JOYCAST);
    }

    public boolean launchYouTube(DialServer server) {
        CastLogger.d(TAG, "Launching ...");
        m_discovery_manager.stopDiscovering();
        return m_discovery_manager.launch(server, Configuration.YOUTUBE);
    }

    public boolean stopYouTube(DialServer server) {
        CastLogger.d(TAG, "Stopping...");
        return m_discovery_manager.stopApplication(server, Configuration.YOUTUBE);
    }

    public void connect(final DialServer dialserver, final String protocol) {
        if ( dialserver != null) {
            m_discovery_manager.stopDiscovering();

            if (m_current_castsession != null) {
                SessionManager.destroySession(m_current_castsession);
                m_current_castsession = null;
            }

            new Thread(new Runnable() {

                @Override
                public void run() {
                    CastLogger.d(TAG, "Creating session ...");
                    m_current_castsession = SessionManager.createSession();
                    m_current_castsession.addEventListener(m_cast_session_listener);
                    if ( false == m_current_castsession.start(dialserver, protocol) )
                    {
                        CastLogger.d(TAG, "Fail to connect websocket relay server");
                        m_current_castsession = null;
                    }
                }
            }).start();
        }
    }

    public void connect(final String ip, final int port, final String protocol) {
        m_discovery_manager.stopDiscovering();

        if ( m_current_castsession != null ) {
            SessionManager.destroySession(m_current_castsession);
            m_current_castsession = null;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                CastLogger.d(TAG, "Creating session ...");
                m_current_castsession = SessionManager.createSession();
                m_current_castsession.addEventListener(m_cast_session_listener);
                if ( false == m_current_castsession.start(ip, port, protocol) )
                {
                    CastLogger.d(TAG, "Fail to connect websocket relay server");
                    m_current_castsession = null;
                }
            }
        }).start();
    }

    public boolean healthCheck(long timeout_msec) {
        if ( m_current_castsession != null ) {
            return m_current_castsession.healthCheck(timeout_msec);
        }
        return false;
    }

    public void customMessage(String name, String value) {
        m_av_controller.sendCustomMessage(name, value);
    }

    public void seek(double position) { m_av_controller.seek(position); }

    public void display320x240() {
        m_av_controller.moveScreen(100, 100, 320, 240);
    }

    public void display720x480() {
        m_av_controller.moveScreen(0, 0, 720, 480);
    }

    public void displayFullscreen() {
        m_av_controller.fullScreen();
    }

    public void jumpBackward() {
        m_av_controller.jumpBackward(5.0);
    }

    public void jumpForward() {
        m_av_controller.jumpForward(5.0);
    }

    public void mute() {
        m_av_controller.mute();
    }

    public void pause() {
        m_av_controller.pause();
    }

    public void play() {
        m_av_controller.play();
    }

    public void playFaster() {
        m_av_controller.playFaster();
    }

    public void playNormal() {
        m_av_controller.playbackRate(1.0);
    }

    public void playSlower() {
        m_av_controller.playSlower();
    }

    public void playSource1() {
        m_av_controller.play("KungFu Panda", "http://10.0.14.169/thkang2/video/KungFuPanda-SecretAdmirer-720p.mp4");
    }

    public void playSource2() {
        m_av_controller.play("demo", "http://10.0.14.169/thkang2/video/kbcore.mp4");
    }

    public void playSource3() {
        m_av_controller.play("Me before U", "http://10.0.14.169/thkang2/video/mebeforeyou.mp4");
    }

    public void playSourceCustom(String title, String url) {
        m_av_controller.play(title, url);
    }

    public void stop() {
        m_av_controller.stop();
    }

    public void unmute() {
        m_av_controller.unmute();
    }

    public void volumeUp() {
        m_av_controller.volumeUp();
    }

    public void volumeDown() {
        m_av_controller.volumeDown();
    }

    public void loadPhoto(String url) { m_photo_controller.load(url); }

    public void unloadPhoto() { m_photo_controller.unload(); }

    public CastSession getCurrentSession() { return m_current_castsession; }

    protected DiscoveryManager.Listener m_discovery_manager_listener = new DiscoveryManager.Listener() {
        @Override
        public void onSearched(DialServer server) {
            m_listener_onsearched_dialserver.onSearched(m_discovery_manager, server);
        }
    };

    protected CastSession.Listener m_cast_session_listener = new CastSession.Listener() {
        @Override
        public void onAddedSupportingAvController(CastSession session, Controller controller) {
            CastLogger.d("onAddedSupportingAvController");

            m_av_controller = (AVController) controller;
            m_av_controller.addEventListener(new AVController.Listener() {
                @Override
                public void onLoad(double duration) {
                    CastLogger.i(TAG, "onLoad (duration : " + duration +")");
                    m_current_duration = duration;
                }

                @Override
                public void onPlayed() {
                    CastLogger.i(TAG, "onPlayed");
                }

                @Override
                public void onPaused() {
                    CastLogger.i(TAG, "onPaused");
                }

                @Override
                public void onStopped() {
                    CastLogger.i(TAG, "onStopped");
                    if ( m_listener_onstopped != null ) m_listener_onstopped.onStopped();
                }

                @Override
                public void onVolumeChanged(double volume) {
                    CastLogger.i(TAG, "onVolumeChanged (volume : " + volume + ")");
                    m_current_volume = volume;
                }

                @Override
                public void onEnded() {
                    CastLogger.i(TAG, "onEnded");
                }

                @Override
                public void onError() {
                    CastLogger.i(TAG, "onError");
                }

                @Override
                public void onPositionChanged(double duration, double position) {
                    CastLogger.i("onPositionChanged.1");
                    CastLogger.i(TAG, "onPositionChanged (position : " + position + ")");
                    m_current_position = position;

                    m_listener_onduration_changed.onDurationChanged(duration);
                    m_listener_onposition_changed.onPositionChanged(position);
                }

                @Override
                public void onPlaying() {
                    CastLogger.i(TAG, "onPlaying");
                }

                @Override
                public void onSeeking() {
                    CastLogger.i(TAG, "onSeeking");
                }

                @Override
                public void onSeeked() {
                    CastLogger.i(TAG, "onSeeked");
                }

                @Override
                public void onDurationChanged(double duration) {
                    CastLogger.i(TAG, "onDurationChanged (duration :" + duration + ")");
                    m_current_duration = duration;

                    m_listener_onduration_changed.onDurationChanged(duration);
                }

                @Override
                public void onMute() {
                    CastLogger.i(TAG, "onMute");
                }

                @Override
                public void onUnmute() {
                    CastLogger.i(TAG, "onUnmute");
                }

                @Override
                public void onReceivedCustomMessage(String name, String value) {
                    CastLogger.i(TAG, "onReceivedCustomMessage [name:" + name + ", value: " + value + "]");
                }

                @Override
                public void onPlayRateChanged(double playrate) {
                    CastLogger.i(TAG, "onPlayRateChanged [playrate: " + playrate + "]");
                    m_current_playrate = playrate;
                }

                @Override
                public void onWaiting() {
                    CastLogger.i(TAG, "onWaiting");
                }

            });

            if ( m_listener_onavcontroller_ready != null )
                m_listener_onavcontroller_ready.onAvControllerReady();
        }

        @Override
        public void onAddedSupportingPhotoController(CastSession session, Controller controller) {
            CastLogger.d("onAddedSupportingPhotoController");
            m_photo_controller = session.getPhotoController();
            m_photo_controller.addEventListener(new PhotoController.Listener(){
                @Override
                public void onLoaded(String s) {
                    CastLogger.d("Loaded Photo [" + s  + "]");
                    m_listener_onphotoupdate.onLoaded(s);
                }

                @Override
                public void onUnloaded() {
                    CastLogger.d("Unloaded Photo");
                    m_listener_onphotoupdate.onUnloaded();
                }
            });
            if ( m_listener_onphotocontroller_ready != null )
                m_listener_onphotocontroller_ready.onPhotoControllerReady();
        }

        @Override
        public void onHummingAlived() {
            CastLogger.d("onHummingAlived");
            m_listener_humming_alived.onHummingAlived();
        }

        @Override
        public void onHummingNotAlived() {
            CastLogger.d("onHummingNotAlived");
            m_listener_humming_not_alived.onHummingNotAlived();
        }

        @Override
        public void onSessionEnded(Session session, int error) {
            CastLogger.d("onSessionEnded");
            m_listener_onsessionended.onSessionEnded();
        }

        @Override
        public void onSessionStartFailed(Session session, int error) {
            CastLogger.d("onSessionStartFailed");
            m_listener_onsessionended.onSessionEnded();
        }

        @Override
        public void onSessionStarted(Session session) {
            CastLogger.d("onSessionStarted");
            m_listener_onconnected_session.onConnectedSession(session);
        }

        @Override
        public void onSessionStarting(Session session) {
            CastLogger.d("onSessionStarting");
        }

        @Override
        public void onSessionResumeFailed(Session session, int error) {
            CastLogger.d("onSessionResumeFailed");
        }

        @Override
        public void onSessionResuming(Session session) {
            CastLogger.d("onSessionResuming");
        }

        @Override
        public void onSessionResumed(Session session) {
            CastLogger.d("onSessionResumed");
        }
    };

    protected OnSearchedDialServerListener m_listener_onsearched_dialserver;
    protected OnConnectedSessionListener m_listener_onconnected_session;
    protected OnAvControllerReadyListener m_listener_onavcontroller_ready;
    protected OnDurationChangedListener m_listener_onduration_changed;
    protected OnPositionChangedListener m_listener_onposition_changed;
    protected OnStoppedListener m_listener_onstopped;
    protected OnSessionEndedListener m_listener_onsessionended;
    protected OnPhotoUpdateListener m_listener_onphotoupdate;
    protected OnPhotoControllerReadyListener m_listener_onphotocontroller_ready;
    protected OnHummingAlivedListener m_listener_humming_alived;
    protected OnHummingNotAlivedListener m_listener_humming_not_alived;

    protected double m_current_duration = 0;
    protected double m_current_position = 0;
    protected double m_current_volume = 0;
    protected double m_current_playrate = 0;

    protected AVController m_av_controller = null;
    protected PhotoController m_photo_controller = null;
    protected CastSession m_current_castsession = null;
    protected DiscoveryManager m_discovery_manager = DiscoveryManager.getInstance();
}
