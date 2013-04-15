package com.pk.chirpscan;

//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.os.Process;
//import android.widget.Toast;
//
//
//public class AudioService extends Service {
//	@Override
//	  public void onCreate() {
//	    // Start up the thread running the service.  Note that we create a
//	    // separate thread because the service normally runs in the process's
//	    // main thread, which we don't want to block.  We also make it
//	    // background priority so CPU-intensive work will not disrupt our UI.
////	    thread = new HandlerThread("ServiceStartArguments",
////	            Process.THREAD_PRIORITY_BACKGROUND);
////	    thread.start();
//	    
//	    // Get the HandlerThread's Looper and use it for our Handler 
//	   // mServiceLooper = thread.getLooper();
//		mLooperThread = new LooperThread();
//	    mServiceHandler = new ServiceHandler();
//		
//		//mLooperThread.start();
//	    
//	  }
//	  public void showText() {
//		  Toast.makeText(this, "5secs after", Toast.LENGTH_SHORT).show(); 
//	  }
//
//	  @Override
//	  public int onStartCommand(Intent intent, int flags, int startId) {
//	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//	      // For each start request, send a message to start a job and deliver the
//	      // start ID so we know which request we're stopping when we finish the job
//	      Message msg = mServiceHandler.obtainMessage();
//	      msg.arg1 = startId;
//	      
//	      
//	      //mLooperThread.start();
//  	      //Looper.loop();
//
//
//	      
//	      // If we get killed, after returning from here, restart
//	      return START_REDELIVER_INTENT;
//	  }
//
//	  @Override
//	  public IBinder onBind(Intent intent) {
//	      // We don't provide binding, so return null
//	      return null;
//	  }
//	  
//	  @Override
//	  public boolean onUnbind(Intent intent) {
//	      // All clients have unbound with unbindService()
//		  return mAllowRebind;
//	  }
//	  
//	  @Override
//	  public void onRebind(Intent intent) {
//	       // A client is binding to the service with bindService(),
//         // after onUnbind() has already been called
//      }
//	  
//	  @Override
//	  public void onDestroy() {
//	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
//	  }
//
//
//	  private HandlerThread thread;
//	  private Looper mServiceLooper;
//	  private LooperThread mLooperThread;
//	  private ServiceHandler mServiceHandler;
//	  boolean mAllowRebind = false;
//	  private int counter = 3;
//	  
//	  // Handler that receives messages from the thread
//	  private final class ServiceHandler extends Handler {
////	      public ServiceHandler(Looper looper) {
////	          super(looper);
////	      }
//	      @Override
//	      public void handleMessage(Message msg) {
//	          // Normally we would do some work here, like download a file.
//	          // For our sample, we just sleep for 5 seconds.
//	          long endTime = System.currentTimeMillis() + 5*1000;
//	          while (System.currentTimeMillis() < endTime) {
//	              synchronized (this) {
//	                  try {
//	                      wait(endTime - System.currentTimeMillis());
//	                  } catch (Exception e) {
//	                  }
//	              }
//	          }
//	          Toast.makeText(AudioService.this, "5secs after", Toast.LENGTH_SHORT).show(); 
//	          // Stop the service using the startId, so that we don't stop
//	          // the service in the middle of handling another job
//	          //stopSelf(msg.arg1);
//	          
//	      }
//	  }
//	  
//	  private class LooperThread extends Thread {
//		  //public LooperThread(String s, int in) {
//			  
//		  //}
//		  
//		  public void run() {
//			  Looper.prepare();
//			 
//			  //while((counter)>0)
//		      {
//		    	  
//		    	  
//	          try{
//	          Thread.sleep(5000);
//	          Message msg1 = new Message();
//		      //mServiceHandler.handleMessage(msg1);
//	          showText();
//	          }
//	          catch (InterruptedException e) {
//	              // TODO Auto-generated catch block
//	              e.printStackTrace();
//	          }
//	          counter--;
//	          
//		      }
//			Looper.loop();
//			  
//			  
//		  }
//		  
//		  
//	  }
//	  
//}