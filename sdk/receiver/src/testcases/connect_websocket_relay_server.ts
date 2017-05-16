import { WebSocketClient, IWebSocketClientListener } from "../receiver/utils/websocket_client";

var websocket_client : WebSocketClient = new WebSocketClient({
    onConnected(url:string) : void {
    },
    onDisconnected(url:string) : void {
    },
    onReceived(url:string, message:any) : void {
        console.log('received ' + message.data);
    },
    onError(url:string, error:any) : void {
    }
} as IWebSocketClientListener);

websocket_client.connect("ws://192.168.0.30:4434");