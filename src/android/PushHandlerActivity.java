package com.infobip.push.cordova;

import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import com.infobip.push.PushNotification;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class PushHandlerActivity extends Activity {

	/*
	 * this activity will be started if we receive/open push notification
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			boolean isCordovaWebViewActive = Push.isActive();
			processPushBundle(isCordovaWebViewActive);

			Log.d(Push.TAG, "MM--- isCordovaWebViewActive = " + isCordovaWebViewActive);
			if (!isCordovaWebViewActive) {
				forceMainActivityReload();
			}

			finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Forces the main activity to re-launch if it's unloaded.
	 */
	public void forceMainActivityReload() {
		PackageManager pm = getPackageManager();
		Intent launch = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
		startActivity(launch);
	}

	/**
	 * Takes the push extras from the intent, and sends it through to the
	 * Push for processing.
	 *
	 * @throws JSONException
	 */
	private void processPushBundle(boolean isPushPluginActive) throws JSONException {
		Bundle extras = getIntent().getExtras();

		if (null != extras) {
			JSONObject push = new JSONObject(extras.getString("push"));
			push.put("foreground", false);
			push.put("coldstart", !isPushPluginActive);
			Push.proceedNotification(push);

			LOG.d(Push.TAG, push.toString());
		}
	}

	@Override
	public void onConfigurationChanged(Configuration arg0) {
		super.onConfigurationChanged(arg0);
	}

	/**
	 * Convert push notification to JSON Object
	 *
	 * @param pn
	 *            Push Notification
	 * @return JSONObject
	 */
	public static JSONObject convertNotificationToJson(PushNotification pn) {
		JSONObject json = new JSONObject();
		try {
			json.put("notificationId", pn.getNotificationId());
			json.put("id", pn.getId());
			json.put("mimeType", pn.getMimeType());
			json.put("title", pn.getTitle());
			json.put("message", pn.getMessage());
			json.put("url", pn.getUrl());
			json.put("sound", pn.makeSound());
			json.put("vibrate", pn.vibrate());
			json.put("lights", pn.showLights());
			json.put("additionalInfo", pn.getAdditionalInfo());
			json.put("mediaData", pn.getMediaData());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json;
	}

}
