package com.pk.chirpscan;


import org.hermit.android.core.MainActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class ChirpScan  extends MainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		peakHzText = (TextView)findViewById(R.id.text_peak_hz);
        
        // Create the application GUI.
        audioInstrument = new InstrumentPanel(this, peakHzText);
        
        audioInstrument.setInstruments(InstrumentPanel.Instruments.SPECTRUM);
        		
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
        audioInstrument.onStart();
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

        super.onResume();
        
        // First time round, show the EULA.
        showFirstEula();
//        
//        // Take the wake lock if we want it.
//        if (wakeLock != null && !wakeLock.isHeld())
//            wakeLock.acquire();

        audioInstrument.onResume();

        // Just start straight away.
        audioInstrument.surfaceStart();
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
        
        audioInstrument.onPause();
//
//        // Let go the wake lock if we have it.
//        if (wakeLock != null && wakeLock.isHeld())
//            wakeLock.release();
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

        audioInstrument.onStop();
    }

//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	   

    // The surface manager for the view.
    private InstrumentPanel audioInstrument = null;
    
    public TextView peakHzText = null;
    

    // Debugging tag.
    private static final String TAG = "ChirpScan";


}
