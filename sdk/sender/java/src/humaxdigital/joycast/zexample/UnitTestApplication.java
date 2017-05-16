package humaxdigital.joycast.zexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.DiscoveryManager;
import humaxdigital.joycast.SessionManager;
import humaxdigital.joycast.constant.Configuration;
import humaxdigital.joycast.controller.AVController;
import humaxdigital.joycast.controller.Controller;
import humaxdigital.joycast.controller.PhotoController;
import humaxdigital.joycast.dial.DialServer;
import humaxdigital.joycast.session.CastSession;
import humaxdigital.joycast.session.Session;
import humaxdigital.joycast.util.CastLogger;

public class UnitTestApplication
{
    public static abstract class Action
    {
        public abstract void action();
        
        protected static DialServer m_dial_server = null;
        protected static DiscoveryManager m_discovery_manager = DiscoveryManager.getInstance();
        protected static DiscoveryManager.Listener m_discovery_manager_listener = new DiscoveryManager.Listener() 
        {
            @Override
            public void onSearched(DialServer server) 
            {
                CastLogger.i(TAG, "DialServer");
                CastLogger.i(TAG, "location : " + server.getLocation());
                CastLogger.i(TAG, "ip address : " + server.getIpAddress().getHostAddress());
                CastLogger.i(TAG, "port : " + server.getPort());
                CastLogger.i(TAG, "appsUrl : " + server.getAppsUrl());
                CastLogger.i(TAG, "friendlyName : " + server.getFriendlyName());
                CastLogger.i(TAG, "uuid : " + server.getUuid());
                CastLogger.i(TAG, "manufacturer : " + server.getManufacturer());
                CastLogger.i(TAG, "modelName : " + server.getModelName());
                CastLogger.i(TAG, "version_major : " + server.getVersionMajor());
                CastLogger.i(TAG, "version_minor : " + server.getVersionMinor());
                CastLogger.i(TAG, "manufacturerURL : " + server.getManufacturerURL());
                CastLogger.i(TAG, "modelDescription: : " + server.getModelDescription());
                CastLogger.i(TAG, "serialNumber : " + server.getSerialNumber());
                CastLogger.i(TAG, "");
                
                m_dial_server = server;
            }
        };
        
        protected static PhotoController m_photo_controller = null;
        protected static AVController m_av_controller = null;
        protected static CastSession m_current_castsession = null;
        protected static CastSession.Listener m_cast_session_listener = new CastSession.Listener()
        {
            @Override
            public void onAddedSupportingAvController(CastSession session, Controller controller)
            {
                CastLogger.d("onAddedAvController");
                
                m_av_controller = (AVController) controller;
                m_av_controller.addEventListener(new AVController.Listener() 
                {
                    @Override
                    public void onLoad(double duration)
                    {
                        CastLogger.i(TAG, "onLoad (duration : " + duration +")");
                        m_current_duration = duration;
                    }

                    @Override
                    public void onPlayed()
                    {
                        CastLogger.i(TAG, "onPlayed");
                    }

                    @Override
                    public void onPaused()
                    {
                        CastLogger.i(TAG, "onPaused");
                    }

                    @Override
                    public void onStopped()
                    {
                        CastLogger.i(TAG, "onStopped");
                    }

                    @Override
                    public void onVolumeChanged(double volume)
                    {
                        CastLogger.i(TAG, "onVolumeChanged (volume : " + volume + ")");
                        m_current_volume = volume;
                    }

                    @Override
                    public void onEnded()
                    {
                       CastLogger.i(TAG, "onEnded"); 
                    }

                    @Override
                    public void onError()
                    {
                        CastLogger.i(TAG, "onError");
                    }

                    @Override
                    public void onPositionChanged(double duration, double position)
                    {
                        CastLogger.i(TAG, "onPositionChanged (position : " + position + ")");
                        m_current_duration = duration;
                        m_current_position = position;
                    }

                    @Override
                    public void onPlaying()
                    {
                        CastLogger.i(TAG, "onPlaying");
                    }

                    @Override
                    public void onSeeking()
                    {
                        CastLogger.i(TAG, "onSeeking");
                    }

                    @Override
                    public void onSeeked()
                    {
                        CastLogger.i(TAG, "onSeeked");
                    }

                    @Override
                    public void onDurationChanged(double duration)
                    {
                        CastLogger.i(TAG, "onDurationChanged (duration :" + duration + ")");
                        m_current_duration = duration;
                    }

                    @Override
                    public void onMute()
                    {
                        CastLogger.i(TAG, "onMute");
                    }

                    @Override
                    public void onUnmute()
                    {
                        CastLogger.i(TAG, "onUnmute");
                    }

                    @Override
                    public void onReceivedCustomMessage(String name, String value)
                    {
                        CastLogger.i(TAG, "onReceivedCustomMessage [name:" + name + ", value: " + value + "]");
                    }

                    @Override
                    public void onPlayRateChanged(double playrate)
                    {
                        CastLogger.i(TAG, "onPlayRateChanged [playrate: " + playrate + "]");
                        m_current_playrate = playrate;
                    }

                    @Override
                    public void onWaiting()
                    {
                        CastLogger.i(TAG, "onWaiting");
                    }

                });
            }

            @Override
            public void onAddedSupportingPhotoController(CastSession session, Controller controller)
            {
                m_photo_controller = session.getPhotoController();
                m_photo_controller.addEventListener(new PhotoController.Listener()
                {
                    @Override
                    public void onLoaded(String url)
                    {
                        CastLogger.d("Loaded photo : " + url);
                        
                    }

                    @Override
                    public void onUnloaded()
                    {
                        CastLogger.d("Unloaded photo");
                    }
                });
            }

            @Override
            public void onSessionEnded(Session session, int error) 
            {
                CastLogger.d("onSessionEnded");
            }

            @Override
            public void onSessionStartFailed(Session session, int error) 
            {
                CastLogger.d("onSessionStartFailed");
            }

            @Override
            public void onSessionStarted(Session session) 
            {
                CastLogger.d("onSessionStarted");
            }

            @Override
            public void onSessionStarting(Session session) 
            {
                CastLogger.d("onSessionStarting");
            }

            @Override
            public void onSessionResumeFailed(Session session, int error) 
            {
                CastLogger.d("onSessionResumeFailed");
            }

            @Override
            public void onSessionResuming(Session session) 
            {
                CastLogger.d("onSessionResuming");
            }
            
            @Override
            public void onSessionResumed(Session session)
            {
                CastLogger.d("onSessionResumed");
            }

            @Override
            public void onHummingAlived()
            {
                CastLogger.d("onHummingAlived");
                
            }

            @Override
            public void onHummingNotAlived()
            {
                CastLogger.d("onHummingNotAlived");
            }
        };
    }
    
    public static class Discover extends Action
    {
        public void action()
        {
            CastLogger.d(TAG, "Discovering ...");
            /**
             * remove previous handler already registered
             */
            m_discovery_manager.removeEventListener(m_discovery_manager_listener);
            m_discovery_manager.addEventListener(m_discovery_manager_listener);
            m_discovery_manager.discover(); 
        }
    }
    
    public static class StopDiscovering extends Action
    {
        public void action()
        {
            m_discovery_manager.stopDiscovering();
        }
    }
    
    public static class Launch extends Action
    {
        public void action()
        {
            CastLogger.d(TAG, "Launching ...");
            if ( true == m_discovery_manager.launch(m_dial_server, Configuration.JOYCAST) ) 
            {
                CastLogger.d(TAG, "Launched " + Configuration.JOYCAST + " on target device");
            }
        }
    }
    
    public static class ConnectRelayServer extends Action
    {
        public void action()
        {
            CastLogger.d(TAG, "Creating session ...");
            if ( m_current_castsession == null )
            {
                m_current_castsession = SessionManager.createSession();
                m_current_castsession.addEventListener(m_cast_session_listener);
                if ( false == m_current_castsession.start(
                        RELAY_WEBSOCKET_SERVER_IP, RELAY_WEBSOCKET_SERVER_PORT, RELAY_WEBSOCKET_SERVER_PROTOCOL) )
                {
                    CastLogger.d(TAG, "Fail to connect websocket relay server");
                }
            }
        }
    }
    
    public static class PlayWithSource1 extends Action
    {
        public void action()
        {
            m_av_controller.play("KungFu Panda", "http://10.0.14.169/thkang2/video/KungFuPanda-SecretAdmirer-720p.mp4");
        }
    }

    public static class PlayWithSource2 extends Action
    {
        public void action()
        {
            m_av_controller.play("jason bourne", "http://10.0.14.169/thkang2/video/jasonbourne.mkv");
        }
    }
    
    public static class PlayWithSource3 extends Action
    {
        public void action()
        {
            m_av_controller.play("voo", "http://10.0.14.169/thkang2/video/kbcore.mp4");
        }
    }
    
    public static class PlayWithSource4 extends Action
    {
        public void action()
        {
            m_av_controller.play("Me before U", "http://10.0.14.169/thkang2/video/mebeforeyou.mp4");
        }
    }
    
    public static class LoadPhoto extends Action
    {
        public void action()
        {
            m_photo_controller.load("http://10.0.14.169/thkang2/photo/1.jpg");
        }
    }
    
    public static class UnloadPhoto extends Action
    {
        public void action()
        {
            m_photo_controller.unload();
        }
    }
    
    public static class Play extends Action
    {
        public void action()
        {
            m_av_controller.play();
        }
    }
    
    /**
     * 1.0 is normal speed
     * 0.5 is half speed (slower)
     * 2.0 is double speed (faster)
     * -1.0 is backwards, normal speed
     * -0.5 is backwards, half speed
     */
    public static class PlayFasterDouble extends Action
    {
        public void action()
        {
            m_av_controller.playbackRate(2.0);
        }
    }

    public static class PlaySlowerHalf extends Action
    {
        public void action()
        {
            m_av_controller.playbackRate(0.5);
        }
    }
    
    public static class PlayFaster extends Action
    {
        public void action()
        {
            m_av_controller.playFaster();
        }
    }
    
    public static class PlaySlower extends Action
    {
        public void action()
        {
            m_av_controller.playSlower();
        }
    }
    
    public static class PlayNormal extends Action
    {
        public void action()
        {
            m_av_controller.playbackRate(1.0);
        }
    }

    public static class Stop extends Action
    {
        public void action()
        {
            m_av_controller.stop();
        }
    }

    public static class Pause extends Action
    {
        public void action()
        {
            m_av_controller.pause();
        }
    }

    public static class SeekToBegin extends Action
    {
        public void action()
        {
            m_av_controller.seek(0);
        }
    }
    
    public static class SeekToHalf extends Action
    {
        public void action()
        {
            m_av_controller.seek(m_current_duration/2);
        }
    }
    
    public static class SeekToEnd extends Action
    {
        public void action()
        {
            m_av_controller.seek(m_current_duration-10);
        }
    }

    public static class JumpBackward extends Action
    {
        public void action()
        {
            m_av_controller.jumpBackward(5.0);
        }
    }
    
    public static class JumpForward extends Action
    {
        public void action()
        {
            m_av_controller.jumpForward(5.0);
        }
    }
    
    public static class VolumeUp extends Action
    {
        public void action()
        {
            m_av_controller.volumeUp();
        }
    }

    public static class VolumeDown extends Action
    {
        public void action()
        {
            m_av_controller.volumeDown();
        }
    }

    public static class Mute extends Action
    {
        public void action()
        {
            m_av_controller.mute();
        }
    }

    public static class Unmute extends Action
    {
        public void action()
        {
            m_av_controller.unmute();
        }
    }

    public static class CustomMessage extends Action
    {
        public void action()
        {
            m_av_controller.sendCustomMessage("myName", "My name is tehokang");
        }
    }
    
    public static class MoveScreen320x240 extends Action
    {
        public void action()
        {
            m_av_controller.moveScreen(100, 100, 320, 240);
        }
    }
    
    public static class MoveScreen720x480 extends Action
    {
        public void action()
        {
            m_av_controller.moveScreen(0, 0, 720, 480);
        }
    }
   
    public static class MoveScreen1280x720 extends Action
    {
        public void action()
        {
            m_av_controller.moveScreen(0, 0, 1280, 720);
        }
    }
    
    public static class MoveScreen1920x1080 extends Action
    {
        public void action()
        {
            m_av_controller.moveScreen(0, 0, 1920, 1080);
        }
    }
    
    public static class FullScreen extends Action
    {
        public void action()
        {
            m_av_controller.fullScreen();
        }
    }
    
    public static class HealthCheck extends Action
    {
        public void action()
        {
            m_current_castsession.healthCheck(3000);
        }
    }
    
    public static class ExitTest extends Action
    {
        public void action()
        {
            System.exit(0);;
        }
    }
    
    public static class Menu
    {
        public Menu(int index, String title, Action act)
        {
            this.index = index;
            this.title = title;
            this.act = act;
        }
        
        public void action()
        {
            act.action();
        }
        
        private int index;
        private String title;
        private Action act;
    };
    
    public static void printMenu(List<Menu> menus)
    {
        CastLogger.i("=================================");
        CastLogger.i("     UnitTest Application");
        CastLogger.i("=================================");
        for ( int i=0; i<menus.size(); i++ ) 
        {
            CastLogger.i(menus.get(i).index + ". " + menus.get(i).title);
        }
    }
    
    public static void main(String [] args) throws IOException 
    {
        if ( args.length == 0 )
        {
            CastLogger.i("[Usage] UnitTestApplication <ip> <port> [true|false for logging]");
            CastLogger.i("Example.1 : ");
            CastLogger.i("\tjava -jar UnitTestApplication.jar 192.168.0.xxx 4434 true");
            CastLogger.i("Example.2 :");
            CastLogger.i("\tant -Darg0=192.168.0.30 -Darg1=4434 -Darg3=false UnitTestApplication");
            CastLogger.i("");
            CastLogger.i("You didn't pass parameter at all to use custom relay");
            CastLogger.i("\ttarget default server :" + 
                    RELAY_WEBSOCKET_SERVER_IP + ":" + 
                    RELAY_WEBSOCKET_SERVER_PORT + "/" +
                    RELAY_WEBSOCKET_SERVER_PROTOCOL);
            CastLogger.i("\tlogging : " + LOG_ENABLE);
        }
        else
        {
            CastLogger.i("arg[0] : " + args[0]);
            CastLogger.i("arg[1] : " + args[1]);
            CastLogger.i("arg[2] : " + args[2]);
            
            RELAY_WEBSOCKET_SERVER_IP = args[0];
            RELAY_WEBSOCKET_SERVER_PORT = Integer.valueOf(args[1]);
            LOG_ENABLE = Boolean.valueOf(args[2]);
        }
        
        CastLogger.DEBUG = LOG_ENABLE;
        
        List<Menu> menus = new ArrayList<Menu>();
        int index = 0;
        menus.add(new Menu(++index, "discover", new Discover()));
        menus.add(new Menu(++index, "stop discovering", new StopDiscovering()));
        menus.add(new Menu(++index, "launch", new Launch()));
        menus.add(new Menu(++index, "connect relay server", new ConnectRelayServer()));
        menus.add(new Menu(++index, "play with new source 1 (w3school)", new PlayWithSource1()));
        menus.add(new Menu(++index, "play with new source 2 (jason bourn)", new PlayWithSource2()));
        menus.add(new Menu(++index, "play with new source 3 (game demo) ", new PlayWithSource3()));
        menus.add(new Menu(++index, "play with new source 4 (me before you)", new PlayWithSource4()));
        menus.add(new Menu(++index, "play", new Play()));
        menus.add(new Menu(++index, "play faster double (x2.0)", new PlayFasterDouble()));
        menus.add(new Menu(++index, "play normal", new PlayNormal()));
        menus.add(new Menu(++index, "play slower half(x0.5)", new PlaySlowerHalf()));
        menus.add(new Menu(++index, "play faster", new PlayFaster()));
        menus.add(new Menu(++index, "play slower", new PlaySlower()));
        menus.add(new Menu(++index, "stop", new Stop()));
        menus.add(new Menu(++index, "pause", new Pause()));
        menus.add(new Menu(++index, "seek to begin", new SeekToBegin()));
        menus.add(new Menu(++index, "seek to half", new SeekToHalf()));
        menus.add(new Menu(++index, "seek to end", new SeekToEnd()));
        menus.add(new Menu(++index, "jump forward 5 seconds", new JumpForward()));
        menus.add(new Menu(++index, "jump backward 5 seconds", new JumpBackward()));
        menus.add(new Menu(++index, "volume up", new VolumeUp()));
        menus.add(new Menu(++index, "volume down", new VolumeDown()));
        menus.add(new Menu(++index, "mute", new Mute()));
        menus.add(new Menu(++index, "unmute", new Unmute()));
        menus.add(new Menu(++index, "custom message", new CustomMessage()));
        menus.add(new Menu(++index, "move screen (100,100,320,240)", new MoveScreen320x240()));
        menus.add(new Menu(++index, "move screen (0,0,720,480)", new MoveScreen720x480()));
        menus.add(new Menu(++index, "move screen (0,0,1280,720)", new MoveScreen1280x720()));
        menus.add(new Menu(++index, "move screen (0,0,1920,1080)", new MoveScreen1920x1080()));
        menus.add(new Menu(++index, "full screen", new FullScreen()));
        menus.add(new Menu(++index, "load photo", new LoadPhoto()));
        menus.add(new Menu(++index, "unload photo", new UnloadPhoto()));
        menus.add(new Menu(++index, "health check(3sec)", new HealthCheck()));
        menus.add(new Menu(++index, "exit", new ExitTest()));
        
        printMenu(menus);
        
        BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(System.in));
        while ( true )
        {
            CastLogger.i("Choose one of above (type ? to help) : ");
            String selected = buffer_reader.readLine();
            try
            {
                for ( int i=0; i<menus.size(); i++ ) 
                {
                    if (  menus.get(i).index == Integer.valueOf(selected) )
                    {
                        menus.get(i).action();
                    }
                }
            }
            catch(Exception e)
            {
                printMenu(menus);
            }
        }
    };
    
    public static double m_current_duration = 0;
    public static double m_current_position = 0;
    public static double m_current_volume = 0;
    public static double m_current_playrate = 0;
    
    public static final String TAG = "UnitTestApplication";
    public static String RELAY_WEBSOCKET_SERVER_IP = "10.0.12.157";
    public static int RELAY_WEBSOCKET_SERVER_PORT = 7755;
    public static String RELAY_WEBSOCKET_SERVER_PROTOCOL = "sender";
    public static boolean LOG_ENABLE = true;
}
