export class Request
{
    constructor(request?:any)
    {
        this.header = request && request.header || {};
        this.body = request && request.body || {};
    }

    getHeader() : any
    {
        return this.header;
    }

    getBody() : any
    {
        return this.body;
    }

    /**
     * @brief sessionId, controllerId, controllerType in header
     */
    getSessionId() : any
    {
        return this.header.sessionId;
    }

    setSessionId(sessionId : any) : void
    {
        this.header.sessionId = sessionId;
    }

    getControllerId() : any
    {
        return this.header.controllerId;
    }

    setControllerId(controllerId : any) : void
    {
        this.header.controllerId = controllerId;
    }

    getControllerType() : any
    {
        return this.header.controllerType;
    }

    setControllerType(controllerType:any) : void
    {
        this.header.controllerType = controllerType;
    }

    /**
     * @brief actionName, actionType, hash, paramList in body
     */
    getActionName() : any
    {
        return this.body.actionName;
    }

    setActionName(actionName:any) : void
    {
        this.body.actionName = actionName;
    }

    getActionType() : any
    {
        return this.body.actionType;
    }

    setActionType(actionType:any) : void
    {
        this.body.actionType = actionType;
    }

    getHash() : any
    {
        return this.body.hash;
    }

    setHash(hash:any) : void
    {
        this.body.hash = hash;
    }

    getParamList() : any[]
    {
        return this.body.paramList;
    }

    setParamList(params:any[]) : void
    {
        this.body.paramList = params;
    }

    public static REQUEST = "request";

    public static QUERY_CAPABILITY = "query_capability";
    public static HEALTH_CHECK = "health_check";

    /**
     * @note Action name of AV Request messages
     */
    public static AV_PLAY = "play"
    public static AV_PLAY_FASTER = "play_faster";
    public static AV_PLAY_SLOWER = "play_slower";
    public static AV_PLAYBACK_RATE = "playbackrate";
    public static AV_PAUSE = "pause";
    public static AV_SEEK = "seek";
    public static AV_JUMP_BACKWARD = "jump_backward";
    public static AV_JUMP_FORWARD = "jump_forward";
    public static AV_STOP = "stop";
    public static AV_VOLUME_UP = "volumeup";
    public static AV_VOLUME_DOWN = "volumedown";
    public static AV_MUTE = "mute";
    public static AV_UNMUTE = "unmute";
    public static AV_MOVE_SCREEN = "move_screen";
    public static AV_FULL_SCREEN = "full_screen";
    public static AV_CUSTOM_MESSAGE = "custom";

    /**
     * @note Action name of Photo Request messages
     */
    public static PT_LOAD = "load";
    public static PT_UNLOAD = "unload";
    public static PT_CUSTOM_MESSAGE = "custom";

    protected header : any;
    protected body : any;
}