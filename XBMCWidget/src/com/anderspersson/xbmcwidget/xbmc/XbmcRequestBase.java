package com.anderspersson.xbmcwidget.xbmc;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

public abstract class XbmcRequestBase<T> implements XbmcRequest {
	
	private SharedPreferences preferences;
	
	public XbmcRequestBase(SharedPreferences preferences) {
		this.preferences = preferences;
	}	
	
	public abstract T execute() throws Exception;
	
	public Object executeObject() throws Exception {
		return execute();
	}
	
	protected HttpEntity getRequest(String relativeUri) throws Exception {
		
		Uri hostUri = getHostUri(relativeUri);
		DefaultHttpClient httpclient = createHttpClient(hostUri);
		
		HttpGet httpGet = new HttpGet(hostUri.toString());
		
		if(Log.isLoggable(XbmcService.class.getSimpleName(), Log.DEBUG)) {
			Log.v(XbmcService.class.getSimpleName(), "Getting " + httpGet.getURI().toString());
		}
		
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		
		if(entity != null)  {
			if(Log.isLoggable(XbmcService.class.getSimpleName(), Log.DEBUG)) {
				String result= EntityUtils.toString(entity);
				Log.v(XbmcService.class.getSimpleName(), result);
			}
		}
		
		return entity;
	}
	
	protected JSONObject jsonRequest(JSONObject content) throws Exception {
		
		Uri hostUri = getHostUri("/jsonrpc");
		DefaultHttpClient httpclient = createHttpClient(hostUri);
		
		HttpPost httpPost = new HttpPost(hostUri.toString());
		StringEntity se = new StringEntity(content.toString());
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(se);
		httpPost.addHeader("Content-Type", "application/json");
		
		HttpResponse response = httpclient.execute(httpPost);
		
		HttpEntity entity = response.getEntity();
		
		if(entity != null)  {
			String responseContent = EntityUtils.toString(entity);
			JSONObject jsonResult = new JSONObject(responseContent);
			
			if(Log.isLoggable(XbmcRequestBase.class.getSimpleName(), Log.DEBUG)) {
				Log.v(XbmcRequestBase.class.getSimpleName(), responseContent);
			}
			
			return jsonResult;
		}
		
		if(Log.isLoggable(XbmcRequestBase.class.getSimpleName(), Log.DEBUG)) {
			Log.v(XbmcRequestBase.class.getSimpleName(), response.getStatusLine().toString());
		}
		
		return null;
	}

	protected void setAuthentication(DefaultHttpClient httpclient, Uri hostUri) {
		boolean useAuth = preferences.getBoolean("use_auth_preference", false);
		String username = preferences.getString("username_preference", "");
		String password = preferences.getString("password_preference", "");
		
		int port = hostUri.getPort() == -1 ? 80 : hostUri.getPort(); 
		
		if(useAuth) 
		{
			String hostname = hostUri.getHost();
			((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
                new AuthScope(hostname, port), 
                new UsernamePasswordCredentials(username, password));
		}
	}

	protected Uri getHostUri(String path) throws Exception {
		String host = preferences.getString("hostname_preference", null);
		
		if(host == null) 
		{
			throw new Exception("No host is configured.");
		}
		
		if(host.toLowerCase().startsWith("http://") == false) { 
			host = "http://" + host;
		}
		
		return Uri.parse(host + path);
	}

	protected DefaultHttpClient createHttpClient(Uri hostUri) {
		int timeoutConnection = 3000;
		int timeoutSocket = 10000;
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		setAuthentication(httpclient, hostUri);
		return httpclient;
	}
}