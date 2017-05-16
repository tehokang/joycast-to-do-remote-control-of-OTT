import { Param } from '../codec/param'
import { ControllerType } from '../codec/factory';
import { CastSession, ICastSessionListener } from './cast_session'
import { AvController } from '../controller/av_controller'
import { PhotoController } from '../controller/photo_controller';
import { AvControllerImpl } from '../controller/av_controller_impl'
import { PhotoControllerImpl } from '../controller/photo_controller_impl';
import { Controller, IControllerListener } from '../controller/controller'
import { WebSocketClient, IWebSocketClientListener } from "../utils/websocket_client";

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'

export class CastSessionImpl extends CastSession implements IWebSocketClientListener, IControllerListener
{
    constructor(hash:string=Codec.Factory.hash())
    {
        super();

        this.m_session_id = hash;
        this.m_controllers = [];
        this.m_listeners = [];

        this.m_websocket_client = new WebSocketClient(this);

        var av_controller = new AvControllerImpl(Codec.Factory.hash());
        av_controller.addControllerListener(this);

        var photo_controller = new PhotoControllerImpl(Codec.Factory.hash());
        photo_controller.addControllerListener(this);

        this.m_controllers.push(av_controller);
        this.m_controllers.push(photo_controller);
    }

    public connect(websocket_relay_address?:string) : boolean
    {
        var target = websocket_relay_address;
        if ( target == null || target == "" )
        {
            target = 'ws://localhost:4434/';
        }
        return this.m_websocket_client.connect(target);
    }

    public addEventListener(listener : ICastSessionListener ) : void
    {
        this.m_listeners.push(listener);
    }

    public removeEventListener(listener : ICastSessionListener ) : void
    {
        for ( var i=0;i<this.m_listeners.length; i++ ) {
            if ( this.m_listeners[i] === listener )
            {
                 this.m_listeners.splice(i, 1);
            }
        }
    }

    public getPhotoController() : PhotoController
    {
        for ( var i=0; i<this.m_controllers.length; i++ )
        {
            if ( this.m_controllers[i].getControllerType() == Codec.ControllerType.PHOTO )
            {
                return <PhotoController>(<PhotoControllerImpl>(this.m_controllers[i]));
            }
        }
        return null;
    }

    public getAvController() : AvController
    {
        for ( var i=0; i<this.m_controllers.length; i++ )
        {
            if ( this.m_controllers[i].getControllerType() == Codec.ControllerType.AV )
            {
                return <AvController>(<AvControllerImpl>(this.m_controllers[i]));
            }
        }
        return null;
    }

    private __process_request_default__(request:MSG.Request) : void
    {
        var controller = <Controller> this.findControllerByType(request.getControllerType());
        if ( controller )
        {
            controller.receivedRequest(request);
        }
        else
        {
            var response = new ACK.Response(request);
            var result = new ACK.Result();
            result.code = Codec.ResultCode.UNKNOWN_PROTOCOL;
            result.description = 'unknown controller type and even not supporting';
            response.setResult(result);
            this.onSendResponse(response);
        }
    }

    private __process_request_health_check__(request:MSG.Request) : void
    {
        var response = new ACK.Response(request);
        var result = new ACK.Result();
        result.code = Codec.ResultCode.OK;
        result.description = "response of " + MSG.Request.HEALTH_CHECK;
        result.paramList.push(new Param('string', 'status', 'okay'));
        response.setResult(result);
        this.onSendResponse(response);
    }

    private __process_request_query_capability__(request:MSG.Request) : void
    {
        var response = new ACK.Response(request);
        var result = new ACK.Result();
        result.code = Codec.ResultCode.OK;
        result.description = "response of " + MSG.Request.QUERY_CAPABILITY;
        for ( var i=0; i<this.m_controllers.length; i++ )
        {
            result.paramList.push(
                    new Param('string',
                            this.m_controllers[i].getControllerType(),
                            this.m_controllers[i].getControllerId()));
        }
        response.setResult(result);
        this.onSendResponse(response);
    }

    private operate(request : MSG.Request) : void
    {
        switch ( request.getActionName() )
        {
            case MSG.Request.QUERY_CAPABILITY:
                this.__process_request_query_capability__(request);
                break;
            case MSG.Request.HEALTH_CHECK:
                this.__process_request_health_check__(request);
                break;
            default:
                this.__process_request_default__(request);
                break;
        }
    }

    onConnected (url:string) : void
    {
        /**
         * notify to typescript/DOM event handlers
         */
        for ( var i in this.m_listeners)
        {
            this.m_listeners[i].onSessionConnected(url, this);
        }

        /**
         * notify to intricsic event handler
         */
        this.onSessionConnected(url, this);
    }

    onDisconnected(url:string) : void
    {
        /**
         * notify to typescript/DOM event handlers
         */
        for ( var i in this.m_listeners)
        {
            this.m_listeners[i].onSessionDisconnected(url, this);
        }
        /**
         * notify to Intricsic event handler
         */
        this.onSessionDisconnected(url, this);
    }

    onReceived(url:string, message:any) : void
    {
        var request = new MSG.Request(Codec.Factory.decode(message.data));

        if ( this )
        {
            this.operate(request);
        }
        else
        {
            var response = new ACK.Response(request);
            var result = new ACK.Result();
            result.code = Codec.ResultCode.UNKNOWN_SESSION;
            result.description = 'unknown session you request, please first create your session';
            response.setResult(result);

            this.m_websocket_client.send(Codec.Factory.encode(response.get()));
        }
    }

    onError(url:string, error:any) : void
    {

    }

    onSendResponse = (response:any) : void =>
    {
        this.m_websocket_client.send(Codec.Factory.encode(response.get()));
    }

    onSendEvent(event:ACK.Event) : void
    {
        this.m_websocket_client.send(Codec.Factory.encode(event.get()));
    }

    private findControllerByType(controllerType:string) : Controller
    {
        for ( var i=0; i<this.m_controllers.length; i++ )
        {
            if ( this.m_controllers[i].getControllerType() == controllerType )
            {
                return this.m_controllers[i];
            }
        }
        return null;
    }

    private findControllerById(controllerId:string) : Controller
    {
        for ( var i=0; i<this.m_controllers.length; i++ )
        {
            if ( this.m_controllers[i].getControllerId() == controllerId )
            {
                return this.m_controllers[i];
            }
        }
        return null;
    }

    private removeController(controllerId:string) : boolean
    {
        for ( var i=0;i<this.m_controllers.length; i++ ) {
            if ( this.m_controllers[i].getControllerId() == controllerId )
            {
                this.m_controllers.slice(i, 1);
                return true;
            }
        }
        return false;
    }

    protected m_session_id : string;;
    protected m_controllers : Controller[];
    protected m_listeners : ICastSessionListener[];
    private m_websocket_client : WebSocketClient = null;
}