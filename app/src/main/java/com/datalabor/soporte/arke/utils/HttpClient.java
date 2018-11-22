package com.datalabor.soporte.arke.utils;

/**
 * Created by Enrique on 18/11/2017.
 */

import android.content.ContentValues;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by rczuart on 1/29/2016.
 */
public class HttpClient
{
    public static HttpResponse post( String url, ContentValues params )
    {
        HttpResponse response = new HttpResponse();
        try
        {
            String requestBody = getQuery( params );
            HttpURLConnection conn = (HttpURLConnection) new URL( url ).openConnection();
            conn.setDoOutput( true );
            conn.setDoInput(true);
            conn.setUseCaches( false );
            conn.setFixedLengthStreamingMode( requestBody.getBytes().length );

            conn.setRequestMethod( "POST" );

            OutputStream out = null;
            try
            {
                out = conn.getOutputStream();
                out.write( requestBody.getBytes() );
            }
            finally
            {
                if( out != null )
                {
                    try
                    {
                        out.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            response.setCode( conn.getResponseCode() );

            InputStream inputStream = null;
            try
            {
                if( response.getCode() == 200 || response.getCode() == 201)
                {
                    inputStream = conn.getInputStream();
                }
                else
                {
                    inputStream = conn.getErrorStream();
                }
                response.setResponse( getString( inputStream ) );
            }
            finally
            {
                if( inputStream != null )
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            conn.disconnect();
        }
        catch( Exception e )
        {
            android.util.Log.e( "HTTPREQ", "Error sending request: " + e.getMessage() );
        }

        return response;
    }


    public static HttpResponse postJson( String url, JSONObject jsonParam )
    {
        HttpResponse response = new HttpResponse();
        try
        {
            //String requestBody = getQuery( params );
            HttpURLConnection conn = (HttpURLConnection) new URL( url ).openConnection();
            conn.setDoOutput( true );
            conn.setDoInput(true);
            conn.setUseCaches( false );
            //conn.setFixedLengthStreamingMode( requestBody.getBytes().length );

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(jsonParam.toString().getBytes().length));

            conn.setRequestMethod( "POST" );




            DataOutputStream out = new DataOutputStream(conn.getOutputStream ());
            try
            {
                //out = conn.getOutputStream();
                out.writeBytes(jsonParam.toString());

            }
            finally
            {
                if( out != null )
                {
                    try
                    {
                        out.flush();
                        out.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            response.setCode( conn.getResponseCode() );

            InputStream inputStream = null;
            try
            {
                if( response.getCode() == 200 || response.getCode() == 201)
                {
                    inputStream = conn.getInputStream();
                }
                else
                {
                    inputStream = conn.getErrorStream();
                }
                response.setResponse( getString( inputStream ) );
            }
            finally
            {
                if( inputStream != null )
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            conn.disconnect();
        }
        catch( Exception e )
        {
            android.util.Log.e( "HTTPREQ", "Error sending request: " + e.getMessage() );
        }

        return response;
    }

    public static HttpResponse get( String url, ContentValues params )
    {
        HttpResponse response = new HttpResponse();
        try
        {
            String requestBody = getQuery( params );
            HttpURLConnection conn = (HttpURLConnection) new URL( url + "?" + requestBody ).openConnection();
            conn.setUseCaches( false );
            conn.setRequestMethod( "GET" );

            response.setCode( conn.getResponseCode() );

            InputStream inputStream = null;
            try
            {
                if( response.getCode() == 200 )
                {
                    inputStream = conn.getInputStream();
                }
                else
                {
                    inputStream = conn.getErrorStream();
                }
                response.setResponse( getString( inputStream ) );
            }
            finally
            {
                if( inputStream != null )
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            conn.disconnect();
        }
        catch( Exception e )
        {
            android.util.Log.e( "HTTPREQ", "Error sending request: " + e.getMessage() );
        }

        return response;
    }

    public static boolean download( File file, String url )
    {
        boolean result = false;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) new URL( url ).openConnection();
            conn.setUseCaches( false );

            conn.connect();

            int response = conn.getResponseCode();

            InputStream inputStream = null;
            try
            {
                if( response == 200 )
                {
                    inputStream = conn.getInputStream();
                    FileOutputStream fileOutput = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int bufferLength;

                    while ( (bufferLength = inputStream.read(buffer)) > 0 )
                    {
                        fileOutput.write(buffer, 0, bufferLength);
                    }
                    //close the output stream when done
                    fileOutput.close();

                    result = true;
                }
                else
                {
                    inputStream = conn.getErrorStream();
                    android.util.Log.d( "HTTPCLIENT", "Cannot download file. ERROR: " + getString( inputStream ) );
                }
            }
            catch( Exception e )
            {
                android.util.Log.d( "HTTPCLIENT", "Cannot download file. EXCEPTION: " + e.getMessage() );
            }
            finally
            {
                if( inputStream != null )
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch( IOException e )
                    {
                        // Ignore.
                    }
                }
            }

            conn.disconnect();
        }
        catch( Exception e )
        {
            android.util.Log.e( "HTTPREQ", "Error sending request: " + e.getMessage() );
        }

        return result;
    }

    public static String getString( InputStream stream ) throws IOException
    {
        if( stream == null )
        {
            return "";
        }
        BufferedReader reader = new BufferedReader( new InputStreamReader( stream ) );
        StringBuilder content = new StringBuilder();
        String newLine;
        do
        {
            newLine = reader.readLine();
            if( newLine != null )
            {
                content.append( newLine ).append( '\n' );
            }
        }
        while( newLine != null );
        if( content.length() > 0 )
        {
            // strip last newline
            content.setLength( content.length() - 1 );
        }
        return content.toString();
    }

    private static String getQuery( ContentValues params )
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        try
        {
            for( Map.Entry<String, Object> entry : params.valueSet() )
            {
                if( first )
                {
                    first = false;
                }
                else
                {
                    result.append( "&" );
                }

                result.append( URLEncoder.encode( entry.getKey(), "UTF-8" ) );
                result.append( "=" );
                result.append( URLEncoder.encode( entry.getValue().toString(), "UTF-8" ) );
            }
        }
        catch( Exception e )
        {
            android.util.Log.e( "SERVICE", String.format( "Cant create querystring. Error: %s", e.getMessage() ) );
            return null;
        }

        return result.toString();
    }

    public static class HttpResponse
    {
        private int _code;
        private String _response;

        public HttpResponse()
        {
            _code = 0;
            _response = "";
        }

        public int getCode()
        {
            return _code;
        }

        public void setCode( int code )
        {
            _code = code;
        }

        public String getResponse()
        {
            return _response;
        }

        public void setResponse( String response )
        {
            _response = response;
        }
    }

}
