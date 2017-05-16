import { Param } from './param'
import * as MSG from '../codec/request'

export class Result
{
    constructor()
    {
        this.code = "";
        this.description = "";
        this.paramList = [];
    }

    public code : any;
    public description : any;
    public paramList : Param[];
}

/**
 * @warning Please don't change class name since Response is already in node ecosystem.
 */
export class Event extends MSG.Request
{
    constructor(request? : any)
    {
        super(request);
        this.body.actionType = Event.EVENT;
    }

    get() : any
    {
        return this;
    }

    public static EVENT = "event";

    /**
     * @note AV Events
     */
    public static AV_ON_POSITIONCHANGE = "on_position_change";
    public static AV_ON_DURATIONCHANGE = "on_duration_change";

    public static AV_ON_LOAD = "on_load";
    public static AV_ON_PLAY = "on_play";
    public static AV_ON_PLAYRATE_CHANGE = "on_playrate_change";
    public static AV_ON_WAITING = "on_waiting";
    public static AV_ON_PLAYING = "on_playing";
    public static AV_ON_PAUSE = "on_pause";
    public static AV_ON_SEEKING = "on_seeking";
    public static AV_ON_SEEKED = "on_seeked";
    public static AV_ON_VOLUME_CHANGE = "on_volume_change";
    public static AV_ON_MUTE = "on_mute";
    public static AV_ON_UNMUTE = "on_unmute";
    public static AV_ON_ENDED = "on_ended";
    public static AV_ON_ERROR = "on_error";
    public static AV_ON_ABORT = "on_abort";

    public static PT_ON_LOAD = "on_load";
    public static PT_ON_UNLOAD = "on_unload";

    /**
     * @note common event
     */
    public static ON_CUSTOM_MESSAGE = "on_custom_message";
}

export class Response extends Event
{
    constructor(request? : any)
    {
        super(request);
        this.result = new Result();
        this.body.actionType = Response.RESPONSE;
    }

    get() : any
    {
        return this;
    }

    getResult() : any
    {
        return this.result;
    }

    setResult(result:any) : Response
    {
        this.result = result;
        return this;
    }

    public static RESPONSE = "response";

    protected result : any;
}