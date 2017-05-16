import { Controller } from './controller'
import { VideoInfo, AvController, IAvControllerListener } from './av_controller'
import { Param } from '../codec/param'
import { CastLogger } from '../utils/logger'

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'

export class AvControllerImpl extends Controller implements AvController
{
    constructor(controller_id : string)
    {
        super(controller_id);
        this.m_controller_type = Codec.ControllerType.AV;
        this.m_current_video_info = new VideoInfo();
        this.m_event_listeners = [];
    }

    public setVideoElement(element:any) : void
    {
        try
        {
            this.m_html_element = element;
            this.m_html_element.preload = "auto";
            this.m_html_element.controls = false;
            this.m_html_element.autoplay = false;
            this.m_html_element.width = window.innerWidth || document.body.clientWidth;
            this.m_html_element.height = window.innerHeight || document.body.clientHeight;

            this.m_html_element.addEventListener('loadstart', this.onMediaLoadStart);
            this.m_html_element.addEventListener('durationchange', this.onMediaDurationChange);
            this.m_html_element.addEventListener('loadedmetadata', this.onMediaLoadedMetadata);
            this.m_html_element.addEventListener('loadeddata', this.onMediaLoadedData);
            this.m_html_element.addEventListener('progress', this.onMediaProgress);
            this.m_html_element.addEventListener('canplay', this.onMediaCanPlay);
            this.m_html_element.addEventListener('canplaythrough', this.onMediaCanPlayThrough);

            this.m_html_element.addEventListener('play', this.onMediaPlay);
            this.m_html_element.addEventListener('playing', this.onMediaPlaying);
            this.m_html_element.addEventListener('pause', this.onMediaPause);
            this.m_html_element.addEventListener('seeking', this.onMediaSeeking);
            this.m_html_element.addEventListener('seeked', this.onMediaSeeked);
            this.m_html_element.addEventListener('volumechange', this.onMediaVolumeChange);
            this.m_html_element.addEventListener('waiting', this.onMediaWaiting);
            this.m_html_element.addEventListener('stalled', this.onMediaStalled);
            this.m_html_element.addEventListener('suspend', this.onMediaSuspend);
            this.m_html_element.addEventListener('ratechange', this.onMediaRateChange);
            this.m_html_element.addEventListener('ended', this.onMediaEnded);

            /**
             * @note we will use setInterval and get position manually
             * since timeupdate will emit so much many event,
             */
            // this.m_html_element.addEventListener('timeupdate', this.onPositionChange);
            this.m_html_element.addEventListener('abort', this.onMediaAbort);
            this.m_html_element.addEventListener('error', this.onMediaError);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    public receivedRequest(request:MSG.Request) : void
    {
        switch ( request.getActionName() )
        {
            case MSG.Request.AV_PLAY:
                var url = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.URL);
                var title = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.TITLE);

                if ( title ) this.m_current_video_info.title = title.value;

                this.play(url==null?"":url.value);
                break;
            case MSG.Request.AV_PLAY_FASTER:
                this.playFaster();
                break;
            case MSG.Request.AV_PLAY_SLOWER:
                this.playSlower();
                break;
            case MSG.Request.AV_PLAYBACK_RATE:
                this.playRate(Codec.Factory.findParam(
                        request.getParamList(), Codec.ParamName.PLAYBACK_RATE).value);
                break;
            case MSG.Request.AV_PAUSE:
                this.pause();
                break;
            case MSG.Request.AV_SEEK:
                this.seek(Codec.Factory.findParam(
                        request.getParamList(), Codec.ParamName.POSITION).value);
                break;
            case MSG.Request.AV_JUMP_BACKWARD:
                this.jumpBackward(Codec.Factory.findParam(
                        request.getParamList(), Codec.ParamName.INTERVAL).value);
                break;
            case MSG.Request.AV_JUMP_FORWARD:
                this.jumpForward(Codec.Factory.findParam(
                        request.getParamList(), Codec.ParamName.INTERVAL).value);
                break;
            case MSG.Request.AV_STOP:
                this.stop();
                break;
            case MSG.Request.AV_VOLUME_UP:
                this.volumeUp();
                break;
            case MSG.Request.AV_VOLUME_DOWN:
                this.volumeDown();
                break;
            case MSG.Request.AV_MUTE:
                this.mute();
                break;
            case MSG.Request.AV_UNMUTE:
                this.unmute();
                break;
            case MSG.Request.AV_CUSTOM_MESSAGE:
                var name = request.getParamList()[0].name;
                var message = request.getParamList()[0].value;
                CastLogger.i(this.TAG, "Received CustomMessage : " + name + ", " + message);
                /**
                 * @note send event to event handler of typescript
                 */
                for ( var i=0; i<this.m_event_listeners.length; i++ )
                {
                    this.m_event_listeners[i].onCustomMessage(name, message);
                }
                /**
                 * @note send event to event handler of intricsic
                 */
                this.onCustomMessage(name, message);
                break;
            case MSG.Request.AV_MOVE_SCREEN:
                var x = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.VIDEO_X);
                var y = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.VIDEO_Y);
                var w = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.VIDEO_W);
                var h = Codec.Factory.findParam(request.getParamList(), Codec.ParamName.VIDEO_H);

                this.moveScreen(x.value, y.value, w.value, h.value);
                break;
            case MSG.Request.AV_FULL_SCREEN:
                this.fullScreen();
                break;
            default:
                var response = new ACK.Response(request);
                var result = new ACK.Result();
                result.code = Codec.ResultCode.UNKNOWN_PROTOCOL;
                result.description = 'unknown protocol you request';
                response.setResult(result);
                this.sendResponse(response);
                break;
        }
    }

    public addEventListener(listener : IAvControllerListener ) : void
    {
        this.m_event_listeners.push(listener);
    }

    public removeEventListener(listener : IAvControllerListener ) : void
    {
        for ( var i=0;i<this.m_event_listeners.length; i++ )
        {
            if ( this.m_event_listeners[i] === listener )
            {
                 this.m_event_listeners.splice(i, 1);
            }
        }
    }

    public getCurrentVideoInfo() : VideoInfo
    {
        return this.m_current_video_info;
    }

    play(url?:string) : void
    {
        CastLogger.i(this.TAG, 'play');
        try
        {
            if ( url )
            {
                this.m_html_element.src = url;
                this.m_html_element.autoplay = true;
            }

            /**
             * @todo add event listener of video element
             */
            this.m_html_element.playbackRate = 1.0;
            this.m_html_element.play();
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    playFaster() : void
    {
        CastLogger.i(this.TAG, 'playFaster');
        try
        {
            this.m_html_element.playbackRate += 0.1;
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    playSlower() : void
    {
        CastLogger.i(this.TAG, 'playSlower');
        try
        {
            this.m_html_element.playbackRate -= 0.1;
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    playRate(rate:any) : void
    {
        CastLogger.i(this.TAG, 'playRate' + parseFloat(rate));
        try
        {
            if ( rate )
            {
                this.m_html_element.playbackRate = parseFloat(rate);
                this.m_html_element.play();
            }
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    pause() : void
    {
        CastLogger.i(this.TAG, 'pause');
        try
        {
            this.m_html_element.pause();
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    stop() : void
    {
        CastLogger.i(this.TAG, 'stop');
        try
        {
            /**
             * @todo Select one of video.src or document.location.href
             */
            this.m_html_element.src = "";
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    seek(position:any) : void
    {
        CastLogger.i(this.TAG, 'seek');
        try
        {
            this.m_html_element.currentTime = position;
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    jumpForward(interval:any) : void
    {
        CastLogger.i(this.TAG, 'jumpForward');
        try
        {
            this.m_html_element.currentTime = this.m_html_element.currentTime+parseFloat(interval);
        }
        catch(e)
        {
            this.m_html_element.currentTime = this.m_html_element.duration;
            CastLogger.e(this.TAG, e.message);
        }
    }

    jumpBackward(interval:any) : void
    {
        CastLogger.i(this.TAG, 'jumpBackward');
        try
        {
            this.m_html_element.currentTime = this.m_html_element.currentTime-parseFloat(interval);
        }
        catch(e)
        {
            this.m_html_element.currentTime = 0;
            CastLogger.e(this.TAG, e.message);
        }
    }

    volumeUp() : void
    {
        CastLogger.i(this.TAG, 'volumeUp');
        try
        {
            this.m_html_element.muted = false;
            this.m_html_element.volume =  this.m_html_element.volume+0.1;
        }
        catch(e)
        {
            this.onMediaVolumeChange();
            CastLogger.e(this.TAG, e.message);
        }
    }

    volumeDown() : void
    {
        CastLogger.i(this.TAG, 'volumeDown');
        try
        {
            this.m_html_element.muted = false;
            this.m_html_element.volume =  this.m_html_element.volume-0.1;
        }
        catch(e)
        {
            this.onMediaVolumeChange();
            CastLogger.e(this.TAG, e.message);
        }
    }

    mute() : void
    {
        CastLogger.i(this.TAG, 'mute');
        try
        {
            this.m_html_element.muted = true;
            /**
             * @note send event to event handler of typescript
             */
            for ( var i=0; i<this.m_event_listeners.length; i++ )
            {
                this.m_event_listeners[i].onMute();
            }
            /**
             * @note send event to event handler of intricsic
             */
            this.onMute();

            var event = new ACK.Event();
            event.setControllerId(this.m_controller_id);
            event.setControllerType(this.m_controller_type);
            event.setHash(Codec.Factory.hash());
            event.setActionType(ACK.Event.EVENT);
            event.setActionName(ACK.Event.AV_ON_MUTE);
            this.sendEvent(event);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    unmute() : void
    {
        CastLogger.i(this.TAG, 'unmute');
        try
        {
            this.m_html_element.muted = false;
            /**
             * @note send event to event handler of typescript
             */
            for ( var i=0; i<this.m_event_listeners.length; i++ )
            {
                this.m_event_listeners[i].onUnmute(this.m_html_element.volume);
            }
            /**
             * @note send event to event handler of intricsic
             */
            this.onUnmute(this.m_html_element.volume);

            var event = new ACK.Event();
            event.setControllerId(this.m_controller_id);
            event.setControllerType(this.m_controller_type);
            event.setHash(Codec.Factory.hash());
            event.setActionType(ACK.Event.EVENT);
            event.setActionName(ACK.Event.AV_ON_UNMUTE);
            this.sendEvent(event);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    moveScreen(x:any, y:any, w:any, h:any) : void
    {
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onRequestMoveScreen(
                    parseInt(x), parseInt(y), parseInt(w), parseInt(h));
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onRequestMoveScreen(parseInt(x), parseInt(y), parseInt(w), parseInt(h));
    }

    fullScreen() : void
    {
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onRequestFullScreen();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onRequestFullScreen();
    }

    onMediaAbort = () =>
    {
        CastLogger.i(this.TAG, 'onAbort');
        clearInterval(this.m_position_change_timer);
        this.m_position_change_timer = null;

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_ABORT);
        this.sendEvent(event);
    }

    onMediaLoadStart = () =>
    {
        CastLogger.i(this.TAG, 'onLoadStart');
    }

    onMediaDurationChange = () =>
    {
        CastLogger.i(this.TAG, 'onDurationChange');

        this.m_current_video_info.duration = this.m_html_element.duration;

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_DURATIONCHANGE);

        var paramList = new Array<Param>();
        paramList.push(new Param("double", "duration",
                this.m_html_element.duration == null ? 0:this.m_html_element.duration));
        event.setParamList(paramList);
        this.sendEvent(event);
    }

    onMediaLoadedMetadata = () =>
    {
        CastLogger.i(this.TAG, 'onLoadedMetadata');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onLoad(this.m_html_element.duration);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onLoad(this.m_html_element.duration);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_LOAD);

        var paramList = new Array<Param>();
        paramList.push(new Param("double", "duration", this.m_html_element.duration));
        event.setParamList(paramList);

        this.sendEvent(event);
    }

    onMediaLoadedData = () =>
    {
        CastLogger.i(this.TAG, 'onLoadedData');
    }

    onMediaProgress = () =>
    {
        CastLogger.i(this.TAG, 'onProgress');
    }

    onMediaCanPlay = () =>
    {
        CastLogger.i(this.TAG, 'onCanPlay');
        /**
         * @note This event is received after calling play()
         */
    }

    onMediaCanPlayThrough = () =>
    {
        CastLogger.i(this.TAG, 'onCanPlayThrough');
    }

    onMediaPlay = () =>
    {
        if ( this.m_position_change_timer != null )
        {
            clearInterval(this.m_position_change_timer);
            this.m_position_change_timer = null;
        }
        this.m_position_change_timer = setInterval(this.onMediaPositionChange, 1000);

        CastLogger.i(this.TAG, 'onPlay');

        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onPlay(this.m_current_video_info);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onPlay(this.m_current_video_info);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_PLAY);
        this.sendEvent(event);
    }

    onMediaPlaying = () =>
    {
        CastLogger.i(this.TAG, 'onPlaying');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onPlaying();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onPlaying();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_PLAYING);
        this.sendEvent(event);
    }

    onMediaPause = () =>
    {
        clearInterval(this.m_position_change_timer);
        this.m_position_change_timer = null;

        CastLogger.i(this.TAG, 'onPause');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onPause();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onPause();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_PAUSE);
        this.sendEvent(event);
    }

    onMediaSeeking = () =>
    {
        CastLogger.i(this.TAG, 'onSeeking');
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onSeeking();
        }
        this.onSeeking();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_SEEKING);
        this.sendEvent(event);
    }

    onMediaSeeked = () =>
    {
        CastLogger.i(this.TAG, 'onSeeked');
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onSeeked();
        }
        this.onSeeked();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_SEEKED);
        this.sendEvent(event);
    }

    onMediaVolumeChange = () =>
    {
        CastLogger.i(this.TAG, 'onVolumeChange');

        if ( this.m_html_element.muted == true ) return;

        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onVolumeChange(
                    this.m_html_element.volume);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onVolumeChange(this.m_html_element.volume);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_VOLUME_CHANGE);

        var paramList = new Array<Param>();
        paramList.push(new Param("double", "volume", this.m_html_element.volume));
        event.setParamList(paramList);
        this.sendEvent(event);
    }

    onMediaStalled = () =>
    {
        CastLogger.i(this.TAG, 'onMediaStalled');
    }

    onMediaWaiting = () =>
    {
        CastLogger.i(this.TAG, 'onWaiting');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onWaiting();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onWaiting();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_WAITING);
        this.sendEvent(event);
    }

    onMediaSuspend = () =>
    {
        CastLogger.i(this.TAG, 'onSuspend');
    }

    onMediaRateChange = () =>
    {
        CastLogger.i(this.TAG, 'onRateChange');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onPlayRateChange(this.m_html_element.playbackRate);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onPlayRateChange(this.m_html_element.playbackRate);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_PLAYRATE_CHANGE);

        var paramList = new Array<Param>();
        paramList.push(new Param("double", "playrate", this.m_html_element.playbackRate));
        event.setParamList(paramList);
        this.sendEvent(event);
    }

    onMediaEnded = () =>
    {
        CastLogger.i(this.TAG, 'onEnded');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onEnded();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onEnded();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_ENDED);
        this.sendEvent(event);
    }

    onMediaError = () =>
    {
        CastLogger.i(this.TAG, 'onError');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            /**
             * @todo Please make error code to notify to application
             */
            this.m_event_listeners[i].onError();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onEnded();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_ERROR);
        this.sendEvent(event);
    }

    onMediaPositionChange = () =>
    {
        CastLogger.i(this.TAG, 'onPositionChange');
        this.m_current_video_info.currentTime = this.m_html_element.currentTime

        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            /**
             * @todo Please make error code to notify to application
             */
            this.m_event_listeners[i].onPositionChange(this.m_html_element.duration, this.m_html_element.currentTime);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onPositionChange(this.m_html_element.duration, this.m_html_element.currentTime);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);

        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.AV_ON_POSITIONCHANGE);

        var paramList = new Array<Param>();
        paramList.push(new Param("double", "duration",
                this.m_html_element.duration == null ? 0:this.m_html_element.duration));
        paramList.push(new Param("double", "currentTime", this.m_html_element.currentTime));
        event.setParamList(paramList);

        this.sendEvent(event);
    }

    /**
     * @note intricsic event handler to be overwritable from User
     */
    onLoad = (duration:any) : void => {};
    onPlay = (video_info:VideoInfo) : void => {};
    onPlayRateChange = (playrate:any) : void => {};
    onPause = () : void => {};
    onStop = () : void => {};
    onSeeking = () : void => {};
    onSeeked = () : void => {};
    onVolumeChange = (currentVolume:any) : void => {};
    onMute = () : void => {};
    onUnmute = (volume:any) : void => {};
    onEnded = () : void => {};
    onError = () : void => {};
    onPositionChange = (duration:any, position:any) : void => {};
    onCustomMessage = (name:any, message:any): void => {};
    onRequestMoveScreen = (x:any, y:any, w:any, h:any) : void => {};
    onRequestFullScreen = () : void => {};
    onWaiting = () : void => {};
    onPlaying = () : void => {};

    private m_current_video_info : VideoInfo;
    private m_position_change_timer : any = null;
    private m_event_listeners : IAvControllerListener[];
    private TAG : string = "AvController";
}