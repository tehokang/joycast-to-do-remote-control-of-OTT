import * as ACK from '../codec/response'
import * as MSG from '../codec/request'
import * as Codec from '../codec/factory'

import { Param } from '../codec/param'
import { PhotoController } from '../controller/photo_controller';
import { AvController } from '../controller/av_controller';

export interface IControllerListener
{
    onSendEvent(event:ACK.Event):void;
    onSendResponse(response:ACK.Response):void;
}

export abstract class Controller
{
    constructor(controller_id:string)
    {
        this.m_controller_id = controller_id;
        this.m_listeners = [];
    }

    public setHomepage(home_page:string) : void
    {
        this.m_home_page = home_page;
    }

    public getHomepage() : string
    {
        return this.m_home_page;
    }

    public getMediaElement = () : any =>
    {
        return this.m_html_element;
    }

    public getControllerId = () : string =>
    {
        return this.m_controller_id;
    }

    public getControllerType = () : string =>
    {
        return this.m_controller_type;
    }

    public addControllerListener(listener : IControllerListener ) : void
    {
        this.m_listeners.push(listener);
    }

    public removeControllerListener(listener : IControllerListener ) : void
    {
        for ( var i=0;i<this.m_listeners.length; i++ ) {
            if ( this.m_listeners[i] === listener )
            {
                 this.m_listeners.splice(i, 1);
            }
        }
    }

    public abstract receivedRequest(request:MSG.Request) : void;

    public sendCustomMessage(name:any, value:any) : void
    {
        var event = new ACK.Event();
        event.setControllerId(this.m_controller_id);
        event.setControllerType(this.m_controller_type);
        event.setHash(Codec.Factory.hash());
        event.setActionType(ACK.Event.EVENT);
        event.setActionName(ACK.Event.ON_CUSTOM_MESSAGE);

        var paramList = new Array<Param>();
        paramList.push(new Param("string", "name", name));
        paramList.push(new Param("string", "value", value));
        event.setParamList(paramList);
        this.sendEvent(event);
    }

    protected sendEvent = (event:ACK.Event) : void =>
    {
        for ( var i=0; i<this.m_listeners.length; i++)
        {
            this.m_listeners[i].onSendEvent(event);
        }
    }

    protected sendResponse = (response:ACK.Response) : void =>
    {
        for ( var i=0; i<this.m_listeners.length; i++)
        {
            this.m_listeners[i].onSendResponse(response);
        }
    }

    protected m_controller_id : string;
    protected m_controller_type : string;
    protected m_html_element : any;
    protected m_listeners : IControllerListener[];
    protected m_home_page : string;
}