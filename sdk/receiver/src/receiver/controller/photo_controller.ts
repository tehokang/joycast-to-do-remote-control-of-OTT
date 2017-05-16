import { Controller } from './controller'

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'

export interface IPhotoControllerListener
{
    onLoad(url:any) : void;
    onUnload() : void;
    onCustomMessage(name:any, message:any): void;
}

export interface PhotoController
{
    setImageElement(element:any) : void;

    addEventListener(listener : IPhotoControllerListener ) : void;
    removeEventListener(listener : IPhotoControllerListener ) : void;

    onLoad(url:any) : void;
    onUnload() : void;
    onCustomMessage(name:any, message:any): void;
}