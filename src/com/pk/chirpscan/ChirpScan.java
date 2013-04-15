package com.pk.chirpscan;


//import org.hermit.android.core.MainActivity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


public class ChirpScan  extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		peakHzText = (TextView)findViewById(R.id.text_peak_hz);

        		
	}

    /**
     * Called after {@link #onCreate} or {@link #onStop} when the current
     * activity is now being displayed to the user.  It will
     * be followed by {@link #onRestart}.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        
        super.onStart();
        
        /* start the audio service and prepare the render thread */
        startRun();


            	
    }


    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     * 
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        /* start the render thread */
        renderThread.start();

        super.onResume();

        

    }


    /**
     * Called to retrieve per-instance state from an activity before being
     * killed so that the state can be restored in onCreate(Bundle) or
     * onRestoreInstanceState(Bundle) (the Bundle populated by this method
     * will be passed to both).
     * 
     * @param   outState        A Bundle in which to place any state
     *                          information you wish to save.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    
    /**
     * Called as part of the activity lifecycle when an activity is going
     * into the background, but has not (yet) been killed.  The counterpart
     * to onResume(). 
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        
        super.onPause();
 
    }


    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onStart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

        
        if(isServiceRun)
        {
        	stopService(intent);
        	
        }
        
        /* big switch to shut off */
        stopRun();


        
        
        
    }

    private TextView peakHzText = null;


    // Debugging tag.
    private static final String TAG = "ChirpScan";
    
    //AudioService mService;
    
    private boolean isServiceRun = false;
    
    private Intent intent;
    private RenderThread renderThread;
    /* a default delay to run between ticks */
    private final long TICK_DELAY = 1000;
    /* define a few tick msgs */
    private final int MSG_TICK = 0;
    private final int MSG_ABORT = 1;
    
    
	/**
	 * Looper-based ticker class.  This has the advantage that asynchronous
	 * updates can be scheduled by passing it a message.
	 */
	private class RenderThread extends Thread {
		// Constructor -- start at once.
	    private RenderThread() {
	        super("ChirpScan Activity");
	        Log.v(TAG, "Ticker: start");
	        //start();
	    }
	    
        public void run() {
            Looper.prepare();

      	  	if (!uiThreadHandler.hasMessages(MSG_TICK))
      	    {
           	    uiThreadHandler.sendEmptyMessageDelayed(MSG_TICK, TICK_DELAY);

      	  	}
           	
           	Looper.loop();
        }
        
	    // Stop this thread.  There will be no new calls to tick() after this.
		public void kill() {
	        Log.v(TAG, "RenderThread: kill");

	        synchronized (this) {
	        	if (uiThreadHandler == null)
	        		return;
	        	
	        	// Remove any delayed ticks.
	        	uiThreadHandler.removeMessages(MSG_TICK);

	        	// Do an abort right now.
	        	uiThreadHandler.sendEmptyMessage(MSG_ABORT);
	        }
	    }
		
	    // Stop this thread and wait for it to die.  When we return, it is
	    // guaranteed that tick() will never be called again.
	    // 
	    // Caution: if this is called from within tick(), deadlock is
	    // guaranteed.
		public void killAndWait() {
	        Log.v(TAG, "RenderThread: killAndWait");
	        
	        if (Thread.currentThread() == this)
	        	throw new IllegalStateException("RenderThread.killAndWait()" +
	        								    " called from render thread");
	        
	        synchronized (this) {
	        	if (uiThreadHandler == null)
	        		return;
	        	
	        	// Remove any delayed ticks.
	        	uiThreadHandler.removeMessages(MSG_TICK);

	        	// Do an abort right now.
	        	uiThreadHandler.sendEmptyMessage(MSG_ABORT);
	        }

	        // Wait for the thread to finish.  Ignore interrupts.
	        if (isAlive()) {
	            boolean retry = true;
	            while (retry) {
	                try {
	                    join();
	                    retry = false;
	                } catch (InterruptedException e) { }
	            }
	            Log.v(TAG, "RenderThread: killed");
	        } else {
	            Log.v(TAG, "RenderThread: was dead");
	        }
	    }
	};
	

    /**
     * Start the animation running.  All the conditions we need to
     * run are present (surface, size, resumed).
     */
    private void startRun() {
        synchronized (this) {
            // Tell the subclass we're running.
            try {
                //animStart();
            } catch (Exception e) {
                //errorReporter.reportException(e);
            }
       
            if (renderThread != null && renderThread.isAlive())
            	renderThread.kill();
            Log.i(TAG, "set running: start ticker");
            renderThread = new RenderThread();
        }
    }
	
    /**
     * Stop the animation running.  Our surface may have been destroyed, so
     * stop all accesses to it.  If the caller is not the ticker thread,
     * this method will only return when the ticker thread has died.
     */
    private void stopRun() {
        // Kill the thread if it's running, and wait for it to die.
        // This is important when the surface is destroyed, as we can't
        // touch the surface after we return.  But if I am the ticker
    	// thread, don't wait for myself to die.
    	RenderThread thread = renderThread;
        
        if (thread != null && thread.isAlive()) {
        	if (Thread.currentThread() == thread)
        		thread.kill();
        	else
        		thread.killAndWait();
        }
        synchronized (this) {
        	renderThread = null;
        }
        
        // Tell the subclass we've stopped.
        try {
            //animStop();
        } catch (Exception e) {
        }
    }
	
    
	/**
	 * Handler used in the UI thread
	 * updates every TICK_DELAY
	 */
    private Handler uiThreadHandler = new Handler() {
        public void handleMessage(Message msg) {

        	
            switch (msg.what) {
			case MSG_TICK:
				tick();
				
				if (!uiThreadHandler.hasMessages(MSG_TICK))
					uiThreadHandler.sendEmptyMessageDelayed(MSG_TICK, TICK_DELAY);
				break;
			case MSG_ABORT:
				Looper.myLooper().quit();
				break;
			}
            
        }
    };
    
	/**
	 * tick function
	 */
    /* a toy run... to be update  */
    private void tick() {

    	if (peakHzText != null) {
    		peakHzText.setText(String.valueOf(System.currentTimeMillis()));
    	}
		
    }
    
    


}
