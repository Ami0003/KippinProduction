package com.kippin.superviews;

import android.util.Log;

/**
 * Created by agnihotri on 26/08/17.
 */

public class Waiter extends Thread
{
    private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop;

    public Waiter(long period)
    {
        this.period=period;
        stop=false;
    }

    public void run()
    {
        long idle=0;
        this.touch();
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
          //  Log.e(TAG, "Application is idle for "+idle +" ms");
            try
            {
                Thread.sleep(1000); //check every 5 seconds
            }
            catch (InterruptedException e)
            {
               // Log.d(TAG, "Waiter interrupted!");
            }
            if(idle > period)
            {
                idle=0;
                Log.e(TAG, "Application is idle for DONE DONE");
                //do something here - e.g. call popup or so
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void touch()
    {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

    //soft stopping of thread
    public synchronized void stopT()
    {
        stop=true;
    }

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

}
