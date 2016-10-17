package com.nkfust.pdfperview;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nkfust.pdfperview.service.AppService;
import com.nkfust.pdfperview.service.CallBackListener;
import com.nkfust.pdfperview.service.FileDownloadManager;
import com.nkfust.pdfperview.util.URLSetting;

import cx.hell.android.pdfview.OpenFileActivity;
public class MainActivity extends Activity
{

    @ Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        //System.out.println(getExternalFilesDir( null ).getPath());
        
//        Intent intent = new Intent( this, AppService.class );
//        startService( intent );
        
        
        
        new FileDownloadManager().doSyncDownloadURL(this, URLSetting.PDFURL, "per_pdf", true,
                new CallBackListener() 
        {
                    
                    @Override
                    public void onTaskCompleted(String response) 
                    {
                        System.out.println(response);
                        Intent intent = null;
                        intent = new Intent();
                        File file = new File( response );
                        intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
                        intent.setClass( MainActivity.this, OpenFileActivity.class );
                        intent.setAction( "android.intent.action.VIEW" );
                        
                        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM");
                        
                        String file_date = tf.format( file.lastModified() );
                        String now_date = tf.format( new Date() );
                        System.out.println("--------------------------------------");
                        
                        if( !file_date.equalsIgnoreCase( now_date ) )
                        {
                            System.out.println("-------------------file do download-------------------");
                            doFileDownload();
                        }
                        else
                        {
                            System.out.println("-------------------file clearly no fresh!---------------");
                            startActivity( intent );
                            finish();
                        }
                        
                    }
                });
    }

    private void doFileDownload()
    {
        new FileDownloadManager().doSyncDownloadURL(this, URLSetting.PDFURL, "per_pdf", false,
                new CallBackListener() 
        {
                    
                    @Override
                    public void onTaskCompleted(String response) 
                    {
                        System.out.println(response);
                        Intent intent = null;
                        intent = new Intent();
                        File file = new File( response );
                        intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
                        intent.setClass( MainActivity.this, OpenFileActivity.class );
                        intent.setAction( "android.intent.action.VIEW" );
                        
                        startActivity( intent );
                        finish();
                    }
                });
    }
    @ Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @ Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if( id == R.id.action_settings )
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
