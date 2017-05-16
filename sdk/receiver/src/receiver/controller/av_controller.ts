import { Controller } from './controller'
import { Param } from '../codec/param'
import { CastLogger } from '../utils/logger'

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'

export interface IAvControllerListener
{
    onLoad(duration:any) : void;
    onPlay(video_info:VideoInfo) : void;
    onPlayRateChange(playrate:any) : void;
    onPlaying() : void;
    onPause() : void;
    onStop() : void;
    onSeeking() : void;
    onSeeked() : void;
    onVolumeChange(currentVolume:any) : void;
    onMute() : void;
    onUnmute(volume:any) : void;
    onEnded() : void;
    onError() : void;
    onRequestMoveScreen(x:any, y:any, w:any, h:any) : void;
    onRequestFullScreen() : void;
    onPositionChange(duration:any, position:any) : void;
    onCustomMessage(name:any, message:any): void;
    onWaiting() : void;
}

export class VideoInfo
{
    VideoInfo()
    {
        this.title = "";
        this.duration = 0.0;
        this.currentTime = 0.0;
    }

    public title : any;
    public duration : any;
    public currentTime : any;
}

export interface AvController
{
    setVideoElement(element:any) : void;
    addEventListener(listener : IAvControllerListener ) : void;
    removeEventListener(listener : IAvControllerListener ) : void;
    getCurrentVideoInfo() : VideoInfo;
    sendCustomMessage(name:any, value:any) : void;

    /**
     * @note intricsic event handler to be overwritable from User
     */
    onLoad(duration:any) : void;
    onPlay(video_info:VideoInfo) : void;
    onPlayRateChange(playrate:any) : void;
    onPause() : void;
    onStop() : void;
    onSeeking() : void;
    onSeeked() : void;
    onVolumeChange(currentVolume:any) : void;
    onMute() : void;
    onUnmute(volume:any) : void;
    onEnded() : void;
    onError() : void;
    onPositionChange(duration:any, position:any) : void;
    onCustomMessage(name:any, message:any): void;
    onRequestMoveScreen(x:any, y:any, w:any, h:any) : void;
    onRequestFullScreen() : void;
    onWaiting() : void;
    onPlaying() : void;
}