import { CastSession } from './cast_session';
import { CastSessionImpl } from './cast_session_impl';
import { CastLogger } from '../utils/logger';

import * as Codec from '../codec/factory'

export class CastManager
{
    constructor()
    {
        this.m_cast_sessions = [];

        try
        {
            window.addEventListener("unload", this.destructor);
        }
        catch(e)
        {
            CastLogger.e(this.TAG, e.message);
        }
    }

    public static getInstance() : CastManager
    {
        if ( this.m_instance == null )
        {
            this.m_instance = new CastManager();
        }
        return this.m_instance;
    }

    public createSession() : CastSession
    {
        var cast_session = new CastSessionImpl();
        this.m_cast_sessions.push(cast_session);

        return cast_session;
    }

    public destroySession(session) : void
    {
        for ( var i=0;i<this.m_cast_sessions.length; i++ )
        {
            if ( this.m_cast_sessions[i] === session )
            {
                this.m_cast_sessions.slice(i, 1);
            }
        }
    }

    private destructor = () =>
    {
        CastManager.m_instance = null;
        this.m_cast_sessions = [];
    }

    protected m_cast_sessions : CastSessionImpl[] = null;
    protected TAG : string = "CastManager";
    protected static m_instance : CastManager = null;
};

/**
 * @note module.exports can be readable by js application
 */
module.exports = CastManager;