package sender.joycast.zexample;

import java.io.IOException;

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

public class AvControllerApplication 
{
    private static final String TAG = "AvControllerApplication";
    
    public static void main(String [] args) throws IOException 
    {
        final DiscoveryManager m_discovery_manager = DiscoveryManager.getInstance();
        
        if ( m_discovery_manager.discover() ) 
        {
            m_discovery_manager.addEventListener(new DiscoveryManager.Listener() 
            {
                @Override
                public void onSearched(DialServer server) 
                {
                    CastLogger.d(TAG, "DialServer onSearched");
                    CastLogger.d(TAG, "location : " + server.getLocation());
                    CastLogger.d(TAG, "ip address : " + server.getIpAddress().getHostAddress());
                    CastLogger.d(TAG, "port : " + server.getPort());
                    CastLogger.d(TAG, "appsUrl : " + server.getAppsUrl());
                    CastLogger.d(TAG, "friendlyName : " + server.getFriendlyName());
                    CastLogger.d(TAG, "uuid : " + server.getUuid());
                    CastLogger.d(TAG, "manufacturer : " + server.getManufacturer());
                    CastLogger.d(TAG, "modelName : " + server.getModelName());
                    CastLogger.d(TAG, "");
                    
                    m_discovery_manager.stopDiscovering();
                    
                    if ( true == m_discovery_manager.launch(server, Configuration.JOYCAST) ) 
                    {
                        CastLogger.d(TAG, "Launched " + Configuration.JOYCAST + " on target device");
                        CastSession cast_session = SessionManager.createSession();
                        cast_session.addEventListener(new CastSession.Listener()
                        {
                            @Override
                            public void onAddedSupportingAvController(CastSession session, Controller controller)
                            {
                                CastLogger.d("onAddedAvController");
                                
                                AVController av_controller = (AVController) controller;
                                av_controller.addEventListener(new AVController.Listener() 
                                {

                                    @Override
                                    public void onLoad(double duration)
                                    {
                                        
                                    }

                                    @Override
                                    public void onPlayed()
                                    {
                                        
                                    }

                                    @Override
                                    public void onPaused()
                                    {
                                        
                                    }

                                    @Override
                                    public void onStopped()
                                    {
                                        
                                    }

                                    @Override
                                    public void onVolumeChanged(double volume)
                                    {
                                        
                                    }

                                    @Override
                                    public void onEnded()
                                    {
                                        
                                    }

                                    @Override
                                    public void onError()
                                    {
                                        
                                    }

                                    @Override
                                    public void onPositionChanged(double duration, double position)
                                    {
                                        
                                    }

                                    @Override
                                    public void onPlaying()
                                    {
                                        
                                    }

                                    @Override
                                    public void onSeeking()
                                    {
                                        
                                    }

                                    @Override
                                    public void onSeeked()
                                    {
                                        
                                    }

                                    @Override
                                    public void onDurationChanged(double duration)
                                    {
                                        
                                    }

                                    @Override
                                    public void onMute()
                                    {
                                        
                                    }

                                    @Override
                                    public void onUnmute()
                                    {
                                        
                                    }

                                    @Override
                                    public void onReceivedCustomMessage(String name, String value)
                                    {
                                        
                                    }

                                    @Override
                                    public void onPlayRateChanged(double playrate)
                                    {
                                        
                                    }

                                    @Override
                                    public void onWaiting()
                                    {
                                        
                                    }
                                });
                                av_controller.play("https://www.youtube.com/tv#/watch/video/control?v=NUmrwvQ0reI&resume");
                                try { Thread.sleep(5*1000); } catch (InterruptedException e) { }
                            }

                            @Override
                            public void onAddedSupportingPhotoController(CastSession session, Controller controller)
                            {
                                CastLogger.d("onAddedPhotoController");
                                
                                PhotoController photo_controller = (PhotoController)controller;
                                photo_controller.addEventListener(new PhotoController.Listener()
                                {
                                    @Override
                                    public void onLoaded(String url)
                                    {
                                        
                                    }

                                    @Override
                                    public void onUnloaded()
                                    {
                                        
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
                                
                            }

                            @Override
                            public void onHummingNotAlived()
                            {
                                
                            }
                        });
                        
                        if ( false == cast_session.start(server, "") )
                        {
                            CastLogger.d(TAG, "Fail to connect websocket relay server");
                            return;
                        }
                    }
                }
            });
        }
    }
}
