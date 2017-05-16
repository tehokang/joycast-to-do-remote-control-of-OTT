import { Param } from '../codec/param'
import { Controller } from '../controller/controller'
import { AvController } from '../controller/av_controller'
import { PhotoController } from '../controller/photo_controller';

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'

export interface ICastSessionListener
{
    onSessionConnected(url:string, cast_session:CastSession) : void;
    onSessionDisconnected(url:string, cast_session:CastSession) : void;
}

export abstract class CastSession
{
    public abstract connect(websocket_relay_address?:string) : boolean;
    public abstract addEventListener(listener : ICastSessionListener ) : void;
    public abstract removeEventListener(listener : ICastSessionListener ) : void;
    public abstract getPhotoController() : PhotoController;
    public abstract getAvController() : AvController;

    onSessionConnected = (url:string, cast_session:CastSession) : void => {};
    onSessionDisconnected = (url:string, cast_session:CastSession) : void => {};
}