package com.nkfust.pdfperview.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.nkfust.pdfperview.MainActivity;
import com.nkfust.pdfperview.R;
import com.nkfust.pdfperview.def.Debug;
import com.nkfust.pdfperview.util.URLSetting;

import cx.hell.android.pdfview.OpenFileActivity;
public class AppService extends Service
{
    public AppService getService()
    {
        return AppService.this;
    }
    
    public class ServiceBinder extends Binder
    {
        
        public AppService getService()
        {
            return AppService.this;
        }
    }
    
    private final IBinder mBinder  = new ServiceBinder();
    private Handler       handler  = new Handler();
    private Runnable      showTime = new Runnable()
                                   {
                                       
                                       public void run()
                                       {
                                           SimpleDateFormat formatter = new SimpleDateFormat("H:m:s");
                                           //formatter.format( new Date() );
                                           
                                           if( formatter.format( new Date() ).equalsIgnoreCase( "1:33:0" ) )
                                           {
                                               startRefresh();
                                           }
                                           // log�ثe�ɶ�
                                           Log.i( "time:", formatter.format( new Date() ) );
                                           handler.postDelayed( this, 1000 );
                                       }
                                   };
    
    public void custom()
    {
        handler.postDelayed( showTime, 1000 );
    }
    public void startRefresh()
    {
        new FileDownloadManager().doSyncDownloadURL( 
                this, 
                URLSetting.PDFURL,
                "per_pdf",
                false, 
                new CallBackListener() {

                    @ Override
                    public void onTaskCompleted( String response )
                    {
                        Log.e( "refresh:", "��s���\" );
                    }
                } );
    }
    @ Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        // TODO Auto-generated method stub
        Debug.logInfo( "�A��", "�I�sonStartCommand��k" );
        
        if( intent == null )
        {
            return super.onStartCommand( intent, flags, startId );
        }
        
        custom();
        Bundle bundle = intent.getExtras();
        
//        String service_type = bundle.getString( "intent_service_type" );
//        String service_message = bundle.getString( "intent_service_message" );
//        if( service_type.equalsIgnoreCase( "gcm" ) )
//        {
//            showDialog( service_message );
//        }
        //handler.postDelayed( showTime, 1000 );
        return super.onStartCommand( intent, flags, startId );
    }
    
    @ Override
    public IBinder onBind( Intent arg0 )
    {
        // TODO Auto-generated method stub
        Debug.logInfo( "�A��", "�I�sonBind��k" );
        return mBinder;
    }
    
    @ Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        Debug.logInfo( "�A��", "�إ�" );
    }
    
    @ Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        Debug.logInfo( "�A��", "�P��" );
        handler.removeCallbacks( showTime );
    }
    
    public void showDialog( String aMessage )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "�s����!!" );
        builder.setIcon( R.drawable.ic_launcher );
        builder.setMessage( aMessage );
        // nid=notification_id;
        builder.setPositiveButton( "����", new DialogInterface.OnClickListener()
        {
            
            public void onClick( DialogInterface dialog, int whichButton )
            {
                dialog.dismiss();
            }
        } );
        builder.setNegativeButton( "�}��",
                new DialogInterface.OnClickListener()
                {
                    
                    public void onClick( DialogInterface dialog, int whichButton )
                    {
                        dialog.dismiss();
                        Intent intent = new Intent( AppService.this, MainActivity.class );
                        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity( intent);
                    }
                } );
        AlertDialog alert = builder.create();
        alert.getWindow().setType( WindowManager.LayoutParams.TYPE_SYSTEM_ALERT );
        alert.show();
    }
}
