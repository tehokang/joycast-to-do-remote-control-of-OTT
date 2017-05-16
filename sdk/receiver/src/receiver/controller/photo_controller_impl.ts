import { Controller } from './controller'
import { CastLogger } from '../utils/logger'
import { PhotoController, IPhotoControllerListener } from '../controller/photo_controller';
import { Param } from '../codec/param'

import * as Codec from '../codec/factory'
import * as ACK from '../codec/response'
import * as MSG from '../codec/request'


export class PhotoControllerImpl extends Controller implements PhotoController
{
    constructor(controller_id : string)
    {
        super(controller_id);
        this.m_controller_type = Codec.ControllerType.PHOTO;
        this.m_event_listeners = [];
    }

    public setImageElement(element:any) : void
    {
        try
        {
            this.m_html_element = element;

            /**
             * @note I couldn't find event handler that I should use.
             */
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
            case MSG.Request.PT_LOAD:
                this.load(Codec.Factory.findParam(
                        request.getParamList(), Codec.ParamName.URL).value);
                break;
            case MSG.Request.PT_UNLOAD:
                this.unload();
                break;
            case MSG.Request.PT_CUSTOM_MESSAGE:
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

    public addEventListener(listener : IPhotoControllerListener ) : void
    {
        this.m_event_listeners.push(listener);
    }

    public removeEventListener(listener : IPhotoControllerListener ) : void
    {
        for ( var i=0;i<this.m_event_listeners.length; i++ )
        {
            if ( this.m_event_listeners[i] === listener )
            {
                 this.m_event_listeners.splice(i, 1);
            }
        }
    }

    load(url:any) : void
    {
        try
        {
            this.m_html_element.src = url;

            this.onPhotoOnLoad();
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    unload() : void
    {
        try
        {
            this.m_html_element.src = "";

            this.onPhotoOnUnload();
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    onPhotoOnLoad = () =>
    {
        CastLogger.i(this.TAG, 'onPhotoOnLoad');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onLoad(this.m_html_element.src);
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onLoad(this.m_html_element.src);

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.PT_ON_LOAD);

        var paramList = new Array<Param>();
        paramList.push(new Param("string", "url", this.m_html_element.src));
        event.setParamList(paramList);

        this.sendEvent(event);
    }

    onPhotoOnUnload = () =>
    {
        CastLogger.i(this.TAG, 'onPhotoOnUnload');
        /**
         * @note send event to event handler of typescript
         */
        for ( var i=0; i<this.m_event_listeners.length; i++ )
        {
            this.m_event_listeners[i].onUnload();
        }
        /**
         * @note send event to event handler of intricsic
         */
        this.onUnload();

        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.PT_ON_UNLOAD);
        this.sendEvent(event);
    }

    onLoad = (url:any) : void => {};
    onUnload = () : void => {};
    onCustomMessage = (name:any, message:any): void => {};

    private m_event_listeners : IPhotoControllerListener[];
    private TAG : string = "PhotoController";
}