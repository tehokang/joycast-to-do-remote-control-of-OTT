import { CastLogger } from './logger'

export interface IWebSocketClientListener {
    onConnected(url:string) : void;
    onDisconnected(url:string) : void;
    onReceived(url:string, message:any) : void;
    onError(url:string, error:any) : void;
}

export class WebSocketClient
{
    constructor(listener : IWebSocketClientListener)
    {
        this.m_listener = listener;
        this.m_events_to_send = [];
        setInterval(this.__send__, this.EVENT_THREAD_INTERVAL);
    }

    public connect(server_url : string ) : boolean
    {
        this.m_server_url = server_url;

        try
        {
            /**
             * @note To support browser like Chrome \n
             * else then support runtime of nodejs
             */
            this.m_websocket = new WebSocket(server_url);
            this.m_websocket.onopen = this.onConnected;
            this.m_websocket.onclose = this.onDisconnected;
            this.m_websocket.onmessage = this.onReceived;
            this.m_websocket.onerror = this.onError;
        }
        catch(e)
        {
            try
            {
                /**
                 * @note Below is to support runtime of nodejs
                 * Please enable below a line if you want to run on Nodejs
                 */
                const WebSocket = require('ws');
                this.m_websocket = new WebSocket(server_url);
                this.m_websocket.onopen = this.onConnected;
                this.m_websocket.onclose = this.onDisconnected;
                this.m_websocket.onmessage = this.onReceived;
                this.m_websocket.onerror = this.onError;
            }
            catch(e)
            {
                return false;
            }
        }
        return true;
    }

    public send = (event:any) : void =>
    {
        try
        {
            this.m_events_to_send.push(event);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    private __send__ = () : void =>
    {
        var event = null;
        while( (event = this.m_events_to_send.shift()) != null )
        {
            if ( this.m_websocket.bufferedAmount == 0 )
            {
                this.m_websocket.send(event);
                CastLogger.d(this.TAG, "Sent ↓↓↓↓↓↓");
                CastLogger.d(this.TAG, event);
            }
            else
            {
                setTimeout(this.send, this.EVENT_THREAD_INTERVAL, event);
            }
        }
    }

    private onConnected = () =>
    {
        CastLogger.i(this.TAG, "Connected to " + this.m_server_url);
        this.m_listener.onConnected(this.m_server_url);
    }

    private onDisconnected = () =>
    {
        CastLogger.i(this.TAG, "Disconnected from " + this.m_server_url);
        this.m_listener.onDisconnected(this.m_server_url);
    }

    private onReceived = (message:any) =>
    {
        CastLogger.d(this.TAG, "onReceived ↓↓↓↓↓↓ ");
        CastLogger.d(this.TAG, message.data);
        try
        {
            this.m_listener.onReceived(this.m_server_url, message);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    private onError = (error:any) =>
    {
        CastLogger.d(this.TAG, "Error from " + this.m_server_url);
        CastLogger.d(this.TAG, error.data);
        this.m_listener.onError(this.m_server_url, error);
    }

    private m_server_url : string;
    private m_websocket : WebSocket;
    private m_listener : IWebSocketClientListener;
    private m_events_to_send : any[];
    private EVENT_THREAD_INTERVAL = 100;
    private TAG : string = "WebSocketClient";
};