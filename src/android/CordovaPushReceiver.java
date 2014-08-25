package com.infobip.push.cordova;

import org.json.JSONException;
import org.json.JSONObject;

import com.infobip.push.AbstractPushReceiver;
import com.infobip.push.PushNotification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class CordovaPushReceiver extends AbstractPushReceiver {

	public static final String IB_EVENT = "event";
	public static final String IB_NOTIFICATION_ID = "notificationId";
	public static final String IB_ON_NOTIFICATION_RECEIVED = "onNotificationReceived";
	public static final String IB_ON_NOTIFICATION_OPENED = "onNotificationOpened";
	public static final String IB_ON_UNREGISTER = "onUnregistered";
	public static final String IB_ON_INVISIBLE_NOTIFICATION_RECEIVED = "onInvisibleNotificationReceived";
	public static final String IB_ON_REGISTER = "onRegistered";

	@Override
	public void onRegistered(Context context) {

		Intent intent = new Intent(context, com.infobip.push.cordova.PushHandlerActivity.class);

		// TODO: set only required flags
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		JSONObject jsonPush = addEventToNotification(new PushNotification(), IB_ON_REGISTER);
		intent.putExtra("push", jsonPush.toString());

		// forward notification to PushHandlerActivity
		context.startActivity(intent);

		Log.d(Push.TAG, "Successfully registered");

	}

	@Override
	public void onNotificationReceived(PushNotification notification, Context context) {

		// If received notification doesn't appear in notification bar
		// set notification event to IB_ON_INVISIBLE_NOTIFICATION_RECEIVED
		JSONObject jsonPush = null;
		if (this.isOverridenMessageHandling(context)) {
			jsonPush = addEventToNotification(notification, IB_ON_INVISIBLE_NOTIFICATION_RECEIVED);
		} else {
			jsonPush = addEventToNotification(notification, IB_ON_NOTIFICATION_RECEIVED);
		}

		if (Push.isActive() && (!Push.inPause)) {
			Intent intent = new Intent(context, com.infobip.push.cordova.PushHandlerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.putExtra("push", jsonPush.toString());

			// forward notification to PushHandlerActivity
			context.startActivity(intent);
		}

		Log.d("Push", "Message recieved: " + notification.getMessage());
	}


	public void onNotificationOpened(PushNotification notification, Context context) {

		JSONObject jsonPush = addEventToNotification(notification, IB_ON_NOTIFICATION_OPENED);

		Intent intent = new Intent(context, com.infobip.push.cordova.PushHandlerActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		intent.putExtra("push", jsonPush.toString());

		// forward notification to PushHandlerActivity
		context.startActivity(intent);

		Log.d(Push.TAG, "Message opened: " + notification.getMessage());
	}


	@Override
	public void onUnregistered(Context context) {
		Intent intent = new Intent(context, com.infobip.push.cordova.PushHandlerActivity.class);

		// TODO: set only required flags
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		JSONObject jsonPush = addEventToNotification(new PushNotification(), IB_ON_UNREGISTER);
		intent.putExtra("push", jsonPush.toString());

		// forward notification to PushHandlerActivity
		context.startActivity(intent);

		Log.d(Push.TAG, "Successfuly unregistered");
	}

	@Override
	public void onError(int reason, Context context) {
		Log.w(Push.TAG, Integer.toString(reason));
	}

	/**
	 * Convert Notification to JSON and add event to object
	 *
	 * @param pn Push notification
	 * @param event event to add to JSON Object
	 * @return converted notification with event
	 */
	private JSONObject addEventToNotification(PushNotification pn, String event) {
		JSONObject jsonNotification = null;
		try {
			jsonNotification = PushHandlerActivity.convertNotificationToJson(pn);
			jsonNotification.put(IB_EVENT, event);
			if (jsonNotification.has("id")) {
				jsonNotification.put(IB_NOTIFICATION_ID, jsonNotification.get("id"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonNotification;
	}

	private boolean isOverridenMessageHandling(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("PREFS_PRIVATE",
				Context.MODE_PRIVATE);
		if (prefs == null) {
			return false;
		}

		return prefs.getBoolean("developerHandleMessage", false);

	}
}
