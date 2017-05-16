
export class CastLogger
{
    private CastLogger()
    {

    }

    public static d(tag="", msg:string) : void
    {
        if ("" != tag && this.DEBUG )
        {
            console.log(this.m_tag + "[" + tag + "]" + "[D] " +  msg);
        }
        else
        {
            if ( this.DEBUG ) console.log(this.m_tag + "[D] " + msg);
        }
    }

    public static i(tag="", msg:string) : void
    {
        if ("" != tag && this.INFO )
        {
            console.log(this.m_tag + "[" + tag + "]" + "[I] " +  msg);
        }
        else
        {
            if ( this.INFO ) console.log(this.m_tag + "[I] " + msg);
        }
    }

    public static w(tag="", msg:string) : void
    {
        if ("" != tag && this.WARN )
        {
            console.log(this.m_tag + "[" + tag + "]" + "[W] " +  msg);
        }
        else
        {
            if ( this.WARN ) console.log(this.m_tag + "[W] " + msg);
        }
    }

    public static e(tag="", msg:string) : void
    {
        if ("" != tag && this.ERROR )
        {
            console.log(this.m_tag + "[" + tag + "]" + "[E] " +  msg);
        }
        else
        {
            if ( this.ERROR ) console.log(this.m_tag + "[E] " + msg);
        }
    }

    public static DEBUG : boolean = true;
    public static INFO : boolean = true;
    public static WARN : boolean = true;
    public static ERROR : boolean = true;
    public static m_tag : string = "HCS_SDK";
}