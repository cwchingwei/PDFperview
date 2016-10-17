package com.nkfust.pdfperview.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FileDownloadManager
{
	private String loacal_path = null;
    public static FileDownloadManager instance()
    {
        
        if( _instance == null )
        {
            _instance  = new FileDownloadManager();
        }

        return _instance;
    }
    
    public String doSyncDownloadURL
        (
            Context    aContext,
            String     aURL,
            String     aFolder,
            boolean    aExists,//false ÂÐ»\ true ¤£·|¤£»\
            CallBackListener aListener
        )
    {
        String file_name = null;
        String file_type = null;
        
        loacal_path = aContext.getExternalFilesDir( null ).getPath() + File.separator + "download" + File.separator;
        File pk_local_file = new File ( loacal_path, aFolder ); 
        
        file_name = aURL.substring( aURL.lastIndexOf( "/" ) + 1, aURL.lastIndexOf( "." ) );
        file_type = aURL.substring( aURL.lastIndexOf( "." ) + 1 );
        
        if( !pk_local_file.exists() )
        {
            pk_local_file.mkdirs();
        }
        
        loacal_path = pk_local_file.getPath() + File.separator + file_name + "." + file_type;
        
        if( aExists )
        {
            if( new File( loacal_path ).exists() )
            {
                aListener.onTaskCompleted(loacal_path);
                return null;
            }
        }
        
        DownloadSyncTask download = new DownloadSyncTask( aContext, aURL, aExists, loacal_path, aListener );
        download.execute();
        
        return loacal_path;
        
    }
    
    private class DownloadSyncTask extends AsyncTask< Void , Integer , String >
    {
        private String 		_url = null;
        private String 		_local_path = null;
        private boolean      _background = false;
        private Context    	_context = null;
        private CallBackListener    	_listener = null;

        private DownloadSyncTask
            (
            		Context    aContext,
                String     aURL,
                boolean    aBackground,
                String aLocalPath,
                CallBackListener aListener
             )
        {
            _url  			= aURL;
            _local_path   	= aLocalPath;
            _context 		= aContext;
            _listener 		= aListener;
            _background     = true;
        }
        @Override
        protected void onPreExecute() 
        {
            
        	super.onPreExecute();
        	if( _background )
        	{
        	    mProgressDialog = new ProgressDialog( _context );
                mProgressDialog.setMessage( "Downloading file.." );
                mProgressDialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
                mProgressDialog.setCancelable( false );
                mProgressDialog.show();
        	}
        }
        @ Override
        protected String doInBackground( Void... arg0 )
        {
            BufferedOutputStream bout = null;
            FileOutputStream file_out_stream = null;
            
//            try
//            {
//                HttpPost http_post = new HttpPost( _url );
//                System.out.println( "aURL: "  +  _url );
//                file_out_stream = new FileOutputStream( _local_path );
//                bout = new BufferedOutputStream( file_out_stream );
//                
//                HttpResponse response = new DefaultHttpClient().execute( http_post );
//                HttpEntity entity = response.getEntity();
//                entity.writeTo( bout );
//                bout.flush();
//                bout.close();
//            }
//            catch( Exception e )
//            {
//                e.printStackTrace();
//                return null;
//            }
            try {
            	URL url = new URL( _url);
            URLConnection conexion = url.openConnection();
        	conexion.connect();

        	int lenghtOfFile = conexion.getContentLength();
        	Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

        	InputStream input = new BufferedInputStream(url.openStream());
        	OutputStream output = new FileOutputStream( _local_path );

        	byte data[] = new byte[1024];

        	long total = 0;
        	int count = 0;
        		while (( count = input.read(data)) != -1) {
        			total += count;
        			publishProgress((int)((total*100)/lenghtOfFile));
        			output.write(data, 0, count);
        		}

        		output.flush();
        		output.close();
        		input.close();
        	} catch (Exception e) {}
            return _local_path;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
        	// TODO Auto-generated method stub
        	super.onProgressUpdate(progress);
        	if( mProgressDialog != null )
        	{
        	    mProgressDialog.setProgress(progress[0]);
        	}
        }
        @ Override
        protected void onPostExecute( String result )
        {
            if( mProgressDialog != null )
            {
                mProgressDialog.dismiss();
            }
        	_listener.onTaskCompleted(result);
        }
        
    }
    
    private static FileDownloadManager _instance = null;
    private ProgressDialog mProgressDialog = null;
    
}