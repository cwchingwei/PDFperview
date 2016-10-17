package com.nkfust.pdfperview.def;

import android.util.Log;


public class Debug
{
    public static void println( String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            System.out.println( aStr );
        }
    }
    
    public static void logVerbose( String aTag, String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            Log.v( aTag, aStr );
        }
    }
    
    public static void logDebug( String aTag, String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            Log.d( aTag, aStr );
        }
    }
    
    public static void logInfo( String aTag, String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            Log.i( aTag, aStr );
        }
    }

    public static void logWarn( String aTag, String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            Log.w( aTag, aStr );
        }
    }
    
    public static void logError( String aTag, String aStr )
    {
        if( DemoSwitch.DEBUG )
        {
            Log.e( aTag, aStr );
        }
    }
}
