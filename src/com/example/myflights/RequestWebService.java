package com.example.myflights;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RequestWebService {
	public static final String TAG = "WebService";
	public static final int CONNECTION_TIMEOUT = 30000;
	public static final int DATA_READ_TIMEOUT = 30000;
	
	
	
	public static JSONObject requestWebService(String url) {
		
		// set authentication for flightXML
		Authenticator.setDefault(new Authenticator() {
		     protected PasswordAuthentication getPasswordAuthentication() {
		       return new PasswordAuthentication("", "".toCharArray());
		     
		   }
		 });
		
		//disableConnectionReuseIfNecessary();
		HttpURLConnection urlconnection = null;
		try {
			
			//create connection
			URL urlToRequest = new URL(url);
			urlconnection = (HttpURLConnection) urlToRequest.openConnection();
			urlconnection.setConnectTimeout(CONNECTION_TIMEOUT);
			urlconnection.setReadTimeout(DATA_READ_TIMEOUT);
			
			int statusCode = urlconnection.getResponseCode();
			if(statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
				Log.d(TAG,"Not authorized to connect");
				return null;
			}
			else if(statusCode != HttpURLConnection.HTTP_OK){
				Log.d(TAG,"HTTP Error Code: " + statusCode);
				return null;
				
			}
			else
			{
			InputStream in = new BufferedInputStream(urlconnection.getInputStream());
			return new JSONObject(getResponseText(in));
			}
			
		} catch (MalformedURLException e) {
	        // URL is invalid
			e.printStackTrace();
	    } catch (SocketTimeoutException e) {
	        // data retrieval or connection timed out
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // could not read response body 
	        // (could not create input stream)
	    	e.printStackTrace();
	    } catch (JSONException e) {
	        // response body is no valid JSON string
	    	e.printStackTrace();
	    } finally {
	        if (urlconnection != null) {
	            urlconnection.disconnect();
	        }
	    }
		
		return null;

		
	}
	
	private static String getResponseText(InputStream in){
		return new Scanner(in).useDelimiter("\\A").next();
	}
	
	/**
	 * required in order to prevent issues in earlier Android version.
	 */
	/*private static void disableConnectionReuseIfNecessary() {
	    // see HttpURLConnection API doc
	    if ((Build.VERSION.SDK_INT) 
	            < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}*/

}
