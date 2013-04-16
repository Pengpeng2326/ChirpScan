package com.pk.chirpscan;

import org.hermit.dsp.FFTTransformer;
import org.hermit.dsp.Window;


public class AudioService {
	public AudioService() {
		
        
        // Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
//		thread = new HandlerThread("ServiceStartArguments",
//		        Process.THREAD_PRIORITY_BACKGROUND);
//		thread.start();
		
		// Get the HandlerThread's Looper and use it for our Handler 
//		mServiceLooper = thread.getLooper();
//		//mLooperThread = new LooperThread();
//		mServiceHandler = new ServiceHandler(mServiceLooper);
		
        
	    audioReader = new AudioReader();
	    
	    // Allocate the spectrum data.
	    spectrumData = new float[inputBlockSize / 2];
	    spectrumHist = new float[inputBlockSize / 2][historyLen];
	    spectrumIndex = 0;
	    
	    spectrumAnalyser = new FFTTransformer(inputBlockSize, windowFunction);
	    
	    synchronized (this) {
	        // Tell the subclass we're running.
	        try {
	            measureStart();
	        } catch (Exception e) {
	            
	        }
	    }



		
		//mLooperThread.start();
    
	}


//  @Override
//  public int onStartCommand(Intent intent, int flags, int startId) {
//      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//  // For each start request, send a message to start a job and deliver the
//  // start ID so we know which request we're stopping when we finish the job
//  Message msg = mServiceHandler.obtainMessage();
//  msg.arg1 = startId;
//  msg.what = AUDIO_READY;
//  mServiceHandler.sendMessage(msg);
//  
//  
//  //mLooperThread.start();
//  //Looper.loop();
//
//
//  
//  // If we get killed, after returning from here, restart
//      return START_STICKY;
//  }

//  @Override
//  public IBinder onBind(Intent intent) {
//      // We don't provide binding, so return null
//      return null;
//  }
//  
//  @Override
//  public boolean onUnbind(Intent intent) {
//      // All clients have unbound with unbindService()
//	  return mAllowRebind;
//  }
//  
//  @Override
//  public void onRebind(Intent intent) {
//       // A client is binding to the service with bindService(),
// // after onUnbind() has already been called
//  }
//  
//  @Override
  public void onDestroy() {
	measureStop();
//	threadReady = false;
    //Toast.makeText(ChirpScan.this, "service done", Toast.LENGTH_SHORT).show(); 
  }
  
  public float peakSpecRead(){
	  return peakSpec;
  }
  
  public final void doUpdate(long now) {
      short[] buffer = null;
      synchronized (this) {
          if (audioData != null && audioSequence > audioProcessed) {
              //parentSurface.statsCount(1, (int) (audioSequence - audioProcessed - 1));
              audioProcessed = audioSequence;
              buffer = audioData;
          }
      }

      // If we got data, process it without the lock.
      if (buffer != null)
          processAudio(buffer);
      
      if (readError != AudioReader.Listener.ERR_OK){}
          //processError(readError);
  }
  
//  // Handler that receives messages from the thread
//  public final class ServiceHandler extends Handler {
//      public ServiceHandler(Looper looper) {
//          super(looper);
//      }
//      @Override
//      public void handleMessage(Message msg) {
//	          // Normally we would do some work here, like download a file.
//  // For our sample, we just sleep for 5 seconds.
////  long endTime = System.currentTimeMillis() + 5*1000;
////  while (System.currentTimeMillis() < endTime) {
////      synchronized (this) {
////          try {
////              wait(endTime - System.currentTimeMillis());
////          } catch (Exception e) {
////          }
////      }
////  }
////  Toast.makeText(AudioService.this, "5secs after", Toast.LENGTH_SHORT).show(); 
//  // Stop the service using the startId, so that we don't stop
//  // the service in the middle of handling another job
//          //stopSelf(msg.arg1);
//    	  // Do the application's physics.
//    	  if(msg.what == AUDIO_UPDATE)
//    	  {
//    		  
//    		  
//    	  }
//    	  else if (msg.what == AUDIO_READY)
//    	  {
//    		  threadReady = true;
//    	  }
//          
//    	  //mServiceHandler.sendEmptyMessageDelayed(0, TICK_DELAY);
//    	  
//          
//      }
//  }
  
//  /* created to wait for the service thread is ready */
//  public synchronized boolean waitUntilReady() {
////	  synchronized (this) {
////		  while (!threadReady) {
////			  try{
////			     wait();
////			  } catch (InterruptedException e) {
////				  
////			  }
////			  
////		  }
////	  }
//	  return threadReady;
//  }
  
//  ServiceHandler mServiceHandler;
//  public final static int AUDIO_READY = 0;
//  public final static int AUDIO_UPDATE = 1;
  


//  private HandlerThread thread;
//  private Looper mServiceLooper;
//  
//  private boolean mAllowRebind = false;
//  private boolean threadReady = false;
  
  /* a default delay to run between ticks */
  //private final long TICK_DELAY = 100;
  // The desired sampling rate for this analyser, in samples/sec.
  private int sampleRate = 8000;

  // Audio input block size, in samples.
  private int inputBlockSize = 256;
  

  // The desired decimation rate for this analyser.  Only 1 in
  // sampleDecimate blocks will actually be processed.
  private int sampleDecimate = 1;
  
  // Buffered audio data, and sequence number of the latest block.
  private short[] audioData;
  private long audioSequence = 0;
  
  // If we got a read error, the error code.
  private int readError = AudioReader.Listener.ERR_OK;
  
  // Sequence number of the last block we processed.
  private long audioProcessed = 0;
   
  // The desired histogram averaging window.  1 means no averaging.
  private int historyLen = 4;

  // Our audio input device.
  private AudioReader audioReader;
  // Analysed audio spectrum data; history data for each frequency
  // in the spectrum; index into the history data; and buffer for
  // peak frequencies.
  private float[] spectrumData;
  private float[][] spectrumHist;
  private int spectrumIndex;

  
  // The selected windowing function.
  private Window.Function windowFunction = Window.Function.BLACKMAN_HARRIS;
  
  // Fourier Transform calculator we use for calculating the spectrum
  // and sonagram.
  private FFTTransformer spectrumAnalyser;
  
  private double chirpEnergy = 0;
  private double chirpEnergyNow = 0;
  private boolean raiseDetected = false;
  // The Nyquist frequency -- the highest frequency
  // represented in the spectrum data we will be plotting.
  private final int nyquistFreq = sampleRate/2;
  // spectrum at peak psd: public, to be changed to private
  private float peakSpec = 100;
  // the switch that if we want to detect wind direction
  private final boolean windDirect = false;
  

  // ******************************************************************** //
  // Audio Processing.
  // ******************************************************************** //

  /**
   * Handle audio input.  This is called on the thread of the audio
   * reader.
   * 
   * @param   buffer      Audio data that was just read.
   */
  private final void receiveAudio(short[] buffer) {
      // Lock to protect updates to these local variables.  See run().
      synchronized (this) {
          audioData = buffer;
          ++audioSequence;
      }
  }
  
  /**
   * An error has occurred.  The reader has been terminated.
   * 
   * @param   error       ERR_XXX code describing the error.
   */
  private void handleError(int error) {
      synchronized (this) {
          readError = error;
      }
  }
  
  private void measureStart() {
      audioProcessed = audioSequence = 0;
      readError = AudioReader.Listener.ERR_OK;
      
      audioReader.startReader(sampleRate, inputBlockSize * sampleDecimate, new AudioReader.Listener() {
          @Override
          public final void onReadComplete(short[] buffer) {
              receiveAudio(buffer);
          }
          @Override
          public void onReadError(int error) {
              handleError(error);
          }
      });
  }
  private void measureStop() {
	  if (audioReader != null)
      audioReader.stopReader();
  }
  

  
 

  /**
   * Handle audio input.  This is called on the thread of the
   * parent surface.
   * 
   * @param   buffer      Audio data that was just read.
   */
  private final void processAudio(short[] buffer) {
      // Process the buffer.  While reading it, it needs to be locked.
      synchronized (buffer) {
          // Calculate the power now, while we have the input
          // buffer; this is pretty cheap.
          final int len = buffer.length;

  
          spectrumAnalyser.setInput(buffer, len - inputBlockSize, inputBlockSize);

          // Tell the reader we're done with the buffer.
          buffer.notify();
      }


      // Do the (expensive) transformation.
      // The transformer has its own state, no need to lock here.
      //long specStart = System.currentTimeMillis();
      spectrumAnalyser.transform();
      //long specEnd = System.currentTimeMillis();
      //parentSurface.statsTime(0, (specEnd - specStart) * 1000);

      // Get the FFT output.
      // Get the FFT output.
      if (historyLen <= 1)
          spectrumAnalyser.getResults(spectrumData);
      else
          spectrumIndex = spectrumAnalyser.getResults(spectrumData,
                                                      spectrumHist,
                                                      spectrumIndex);

      
          specPeakSearch(spectrumData);
      
    
  }

  /**
   * find the peak from the spectrum.
   * 
   * @param   data        An array of floats defining the signal power
   *                      at each frequency in the spectrum.
   * @param  peakSpec       Peak spectrum.
   * @param  peakPower      Peak power.
   */
  private void specPeakSearch(float[] data) {

    final int len = data.length;

    // window averaged psd data 
    double z = 0;
    // index of peak spec
    int peakSpecIndex = 0;
    int dataCounter = 0;

    // init maxSpec and maxSpecPower
    double maxSpecPower = -100000.0f;
    double averageSpecPower = 0.0f;
    // half of window width of spec peak search in Hz
    final float specWinWidthHz = 100;
    
    //threshold used to detect a peak in spectrum in dB
    //final float  specPeakDetThredDb= 3;
    
    int specWinWidth = (int)(Math.floor(specWinWidthHz / nyquistFreq) * len);
    
    if (2*specWinWidth+1 > len - 1)
    	specWinWidth = (int)Math.floor((len-1)/2) - 1;
    	
    // Element 0 isn't a frequency bucket; skip it.
    for (int j = 1; j <= 2*specWinWidth+1; j++) {
        //init z: half of the window size
        z += (double) (Math.log10(data[j]));
    } 
    
    // Element 0 isn't a frequency bucket; skip it.
    for (int i = 1+specWinWidth; i < (len-specWinWidth-1); ++i) {     
    	z -= (double)(Math.log10(data[i - specWinWidth]));
    	z += (double)(Math.log10(data[i + specWinWidth + 1]));
        if (z > maxSpecPower) {
        	maxSpecPower = z;
        	peakSpecIndex = i;
        }
        averageSpecPower +=z;
        dataCounter ++;
        

    }
    
    averageSpecPower = (averageSpecPower -maxSpecPower)/ (dataCounter-1);
    if (maxSpecPower > averageSpecPower + 1) {
        chirpEnergy = 0.001*maxSpecPower + chirpEnergy*0.999;
        chirpEnergyNow = 0.1*maxSpecPower + chirpEnergyNow*0.9;
        
        raiseDetected = (!windDirect) || (chirpEnergyNow > chirpEnergy);
        
        peakSpec =  (raiseDetected ) ? ((float)peakSpecIndex / len) * nyquistFreq : 0; 
    	
    }
    else
    {
    	peakSpec = 0;
    	chirpEnergy = 0;
    	chirpEnergyNow = 0;
    			
    	
    }
    
    

    //for debug
    //peakSpec = 10;
}

	        

	  

	  
}