package com.eispala;

public class Logger
{
    public static boolean TestLoggingEnabled = false;
    public static boolean DebugLoggingEnabled = false;

    private static void Log(Object message)
    {
        System.out.println(message.toString());
    }

    public static void DebugLog(Object message)
    {
        if (DebugLoggingEnabled)
        {
            Log(message);
        }

    }

    public static void TestLog(Object message)
    {
        if (TestLoggingEnabled)
        {
            Log(message);
        }
    }
}
