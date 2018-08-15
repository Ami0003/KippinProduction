package com.kippinretail.ApplicationuUlity;

/**
 * Custom log class overrides Android Log
 *
 * @author Raman
 */
public class Log {

    private static final boolean PRINT = true; // true for printing and false
    // for not

    public Log() {
    }

    /**
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (PRINT) {
            android.util.Log.i(tag, message);

        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (PRINT) {


        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        if (PRINT) {
            android.util.Log.e(tag+" ", message+" ");
        }
    }

    /**
     * @param tag
     * @param message
     */
    public static void v(String tag, String message) {
        if (PRINT) {
            android.util.Log.v(tag, message);

        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void w(String tag, String message) {
        if (PRINT) {
            android.util.Log.w(tag, message);

        }
    }
}
