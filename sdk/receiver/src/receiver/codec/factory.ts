import { Param } from './param'

import * as ACK from './response'
import * as MSG from './request'

export class ParamName
{
    public static CONTROLL_TYPE = "controller_type";
    public static PLAYBACK_RATE = "playbackrate";
    public static POSITION = "position";
    public static INTERVAL = "interval";
    public static URL = "url";
    public static TITLE = "title";
    public static VIDEO_X = "x";
    public static VIDEO_Y = "y";
    public static VIDEO_W = "w";
    public static VIDEO_H = "h";
}

export class ControllerType
{
    public static AV = "av_controller";
    public static PHOTO = "photo_controller";
}

export class ResultCode
{
    /**
     * @note Generic returning value when succeed
     */
    public static OK = "200";

    public static UNKNOWN_PROTOCOL = "300";
    public static UNKNOWN_SESSION = "301";
    public static UNKNOWN_CONTROLLER = "302";

    public static FAIL_TO_CREATE_SESSION = "30001";
    public static FAIL_TO_CREATE_CONTROLLER = "30002";
    public static FAIL_TO_DESTROY_SESSION = "30003";
    public static FAIL_TO_DESTROY_CONTROLLER = "30004";

    /**
     * @note Error code from AvController
     */
    public static FAIL_TO_PLAY = "40001";
    public static FAIL_TO_PAUSE = "40002";
    public static FAIL_TO_STOP = "40003";
    public static FAIL_TO_SEEK = "40004";

    /**
     * @note Error code from PhotoController
     */
    public static FAIL_TO_LOAD = "40011";
    public static FAIL_TO_UNLOAD = "40012";
    public static FAIL_TO_ADD = "40013";
    public static FAIL_TO_REMOVE = "40014";
}

export class Factory
{
    private contructor()
    {
        /**
         * @note Do not call constructor by caller
         */
    }

    public static decode(message:string) : any
    {
        return JSON.parse(message);
    }

    public static encode(message:any) : string
    {
        /**
         * @note 3rd parameter is to set indent tab size
         */
        return JSON.stringify(message, null, 2);
    }

    public static findParam(paramList:Param[], name:string) : Param
    {
        for ( var i=0; i<paramList.length; i++ )
        {
            if ( paramList[i].name == name )
            {
                return paramList[i];
            }
        }
        return null;
    }

    public static hash() : string
    {
        var s : string = new Date().getTime().toString();
        /* Simple hash function. */
        var a = 1, c = 0, h, o;
        if (s) {
            a = 0;
            /*jshint plusplus:false bitwise:false*/
            for (h = s.length - 1; h >= 0; h--) {
                o = s.charCodeAt(h);
                a = (a<<6&268435455) + o + (o<<14);
                c = a & 266338304;
                a = c!==0?a^c>>21:a;
            }
        }
        return String(a);
    }
}