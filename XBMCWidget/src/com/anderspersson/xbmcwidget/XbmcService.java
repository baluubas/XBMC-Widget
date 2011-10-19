package com.anderspersson.xbmcwidget;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class XbmcService extends IntentService 
{
	private static final int ERROR_ID = 1;
	
	private SharedPreferences mPreferences;
	
	public XbmcService() {
		super("XBMCService");
	}

	@Override
	public void onHandleIntent(Intent intent) {
		Log.v("XBMC service", "Executing command");
		
		String action = intent.getAction();
		
		if(action == null)
			return;
		
		Log.v("XBMC service", action);

		
		try {
			if(action.equals(XbmcWidget.PLAYPAUSE_ACTION)) {
				sendActionCommand("12");
			}
			else if(action.equals(XbmcWidget.STOP_ACTION)) {
				sendActionCommand("229");
			}
			else if(action.equals(XbmcWidget.UP_ACTION)) {
				sendKeyCommand("270");
			}
			else if(action.equals(XbmcWidget.DOWN_ACTION)) {
				sendKeyCommand("271");
			}	
			else if(action.equals(XbmcWidget.RIGHT_ACTION)) {
				sendKeyCommand("273");
			}
			else if(action.equals(XbmcWidget.LEFT_ACTION)) {
				sendKeyCommand("272");
			}	
			else if(action.equals(XbmcWidget.BACK_ACTION)) {
				sendKeyCommand("275");
			}	
			else if(action.equals(XbmcWidget.SELECT_ACTION)) {
				sendKeyCommand("256");
			}	
			else if(action.equals(XbmcWidget.CONTEXT_ACTION)) {
				sendCommand("ExecBuiltIn(Action(ContextMenu))");
			}
			else if(action.equals(XbmcWidget.HOME_ACTION)) {
				sendCommand("ExecBuiltIn(ActivateWindow(10000))");
			}	
			else if(action.equals(XbmcWidget.TOGGLE_FULLSCREEN_ACTION)) {
				sendActionCommand("18");
			}	
			else if(action.equals(XbmcWidget.TOGGLE_OSD_ACTION)) {
				// Sending key 24 hangs for some reason
				sendKeyCommand("0xF04D");
			}	
		}
		catch(Exception e) 
		{
			Log.w("XBMC service", e.getMessage() == null ? "Unknown error." : e.getMessage());
			notifyError();	
		}		
	}

	private void notifyError() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(
				R.drawable.ic_notification, 
				getText(R.string.error_ticker_text),
				System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL; 
		
		Context context = getApplicationContext();
		
		Intent notificationIntent = new Intent(
				Intent.ACTION_VIEW, 
				null, 
				this, 
				ApplicationPreferenceActivity.class);
		notificationIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, "com.anderspersson.xbmcwidget.TroubleshootingPreferenceFragment");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(
				this, 
				getText(R.string.error_content_title), 
				getText(R.string.error_content_text), 
				contentIntent);
		
		notificationManager.notify(ERROR_ID, notification);
	}
	
	public void sendActionCommand(String actionKey) throws Exception  {
		
		sendCommand("Action("+actionKey + ")");
	}

	public void sendKeyCommand(String key) throws Exception  {
		
		sendCommand("SendKey("+ key + ")");
	}
	
	private void sendCommand(String command) throws Exception {
		
		int timeoutConnection = 3000;
		int timeoutSocket = 10000;
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String host = mPreferences.getString("hostname_preference", null);
		boolean useAuth = mPreferences.getBoolean("use_auth_preference", false);
		String username = mPreferences.getString("username_preference", "");
		String password = mPreferences.getString("password_preference", "");
		
		if(host == null) 
		{
			throw new Exception("No host is configured.");
		}

		if(host.toLowerCase().startsWith("http://") == false) { 
			host = "http://" + host;
		}
		
		Uri hostUri = Uri.parse(host + "/");
		int port = hostUri.getPort() == -1 ? 80 : hostUri.getPort(); 
		
		if(useAuth) 
		{
			String hostname = hostUri.getHost();
			((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
                new AuthScope(hostname, port), 
                new UsernamePasswordCredentials(username, password));
		}
		HttpGet httpget = new HttpGet(host + "/xbmcCmds/xbmcHttp?command="+ Uri.encode(command));
		HttpResponse response = null;
		
		response = httpclient.execute(httpget);
		
		HttpEntity entity = response.getEntity();
		
		if(entity != null)  {
			InputStream instream = entity.getContent();
			String result= convertStreamToString(instream);
			Log.v("XBMC service", result);
		}
		
		Log.v("XBMC service", response.getStatusLine().toString());
	}
	
   public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	
}
