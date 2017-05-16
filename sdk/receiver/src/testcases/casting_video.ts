import { CastManager } from '../receiver/cast/cast_manager';
import { CastSession, ICastSessionListener } from '../receiver/cast/cast_session';

class CastSessionListener implements ICastSessionListener
{
    onSessionConnected(url:string) : void
    {

    }

    onSessionDisconnected(url:string) : void
    {

    }
}

var cast_manager = CastManager.getInstance();
var cast_session = cast_manager.createSession();
cast_session.addEventListener(new CastSessionListener());
if ( true == cast_session.connect('ws://192.168.199.129:4434/') )
{
    console.log("Succeed to run CastManager of HuCast");
}
else
{
    console.log("Failed to run CastManager of HuCast");
}