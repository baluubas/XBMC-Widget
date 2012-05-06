package com.anderspersson.xbmcwidget.common;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TimerCallbacks {
	private Context _ctx;
	private HashSet<String> _callbackTypes;
	private String PREF_KEY = "timerCallbacks_types";

	public TimerCallbacks(Context ctx) {
		this._ctx = ctx;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_ctx);
		_callbackTypes = new HashSet<String>();
		
		for(String callbackType : parseString(prefs.getString(PREF_KEY, "")))
		{
			if(callbackType.isEmpty())
				continue;
			
			_callbackTypes.add(callbackType);
		}
	}

	public void add(Class<? extends ITimerCallback> timerCallback) {
		add(timerCallback.getName());
	}
	
	public void remove(Class<? extends ITimerCallback> timerCallback) {
		remove(timerCallback.getName());
	}
	
	@SuppressWarnings("unchecked")
	public Collection<ITimerCallback> getCallbackInstances() {
		ArrayList<ITimerCallback> callbacks = new ArrayList<ITimerCallback>();
		
		for(String callbackType : _callbackTypes) {
            try {
				Class<ITimerCallback> cls = (Class<ITimerCallback>)Class.forName(callbackType);	
				Constructor<ITimerCallback> ctor = cls.getConstructor(Context.class);
				callbacks.add(ctor.newInstance(_ctx));
			} 
            catch (ClassNotFoundException e) {
				Log.e("TimerCallbacks", "Unknown timer callback", e);
				remove(callbackType);
            } catch (NoSuchMethodException e) {
            	Log.e("TimerCallbacks", "No matching constructor.", e);
				remove(callbackType);
			} catch (Exception e) {
				Log.e("TimerCallbacks", "Error instanciating timer callback.", e);
			}
		}
	
		return callbacks;
	}
	
	private void add(String timerCallback) {
		if(_callbackTypes.contains(timerCallback))
			return;
		
		_callbackTypes.add(timerCallback);
		store();
	}
	
	private void remove(String timerCallback) {
		if(_callbackTypes.contains(timerCallback) == false)
			return;
		
		_callbackTypes.remove(timerCallback);
		store();
	}

	private void store() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_ctx);

        String callbacks = serialize();
        
        prefs
          .edit()
          .putString(PREF_KEY, callbacks)
          .commit();
	}

	private String serialize() {
		return join(_callbackTypes, ";");	
	}
	
	private String[] parseString(String string) {
		return string.split(";");
	}
	
	static String join(Collection<?> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<?> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }
}
