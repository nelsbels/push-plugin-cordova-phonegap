package com.infobip.push.cordova;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.infobip.push.ChannelObtainListener;
import com.infobip.push.ChannelRegistrationListener;
import com.infobip.push.NotificationOpenedListener;
import com.infobip.push.PushConfigurationException;
import com.infobip.push.PushNotification;
import com.infobip.push.PushNotificationManager;
import com.infobip.push.RegistrationData;
import com.infobip.push.UnreceivedNotificationsListener;
import com.infobip.push.UserDataListener;
import com.infobip.push.media.MediaActivity;

public class Push extends CordovaPlugin {
	private static CallbackContext clbContext;
	PushNotificationManager manager;
	public static boolean inPause;
	private static String notificationEventListener;
	private static CordovaWebView cordovaWebView;
	private static List<JSONObject> cachedNnotifications = new ArrayList<JSONObject>();

	public static final String TAG = "Push";

	public static final String ACTION_PUSH_INITIALIZE = "initialize";
	public static final String ACTION_PUSH_IS_INITIALIZED = "isLibraryInitialized";
	public static final String ACTION_PUSH_REGISTER = "registerOnPushService";
	public static final String ACTION_PUSH_UNREGISTER = "unregister";
	public static final String ACTION_PUSH_IS_REGISTERED = "isRegistered";
	public static final String ACTION_PUSH_DEBUG_MODE = "setDebugModeEnabled";
	public static final String ACTION_PUSH_IS_DEBUG_MODE = "isDebugModeEnabled";
	public static final String ACTION_PUSH_REGISTER_CHANNELS = "registerToChannels";
	public static final String ACTION_PUSH_GET_REGISTERED_CHANNELS = "getRegisteredChannels";
	public static final String ACTION_PUSH_CHECK_MANIFEST = "checkManifest";
	public static final String ACTION_PUSH_GET_UNRECEIVED_NOTIF = "getUnreceivedNotifications";
	public static final String ACTION_PUSH_GET_REGISTRATION_DATA = "getRegistrationData";
	public static final String ACTION_PUSH_GET_APPLICATION_DATA = "getApplicationData";
	public static final String ACTION_PUSH_OVERRIDE_MSG_HANDLING = "overrideDefaultMessageHandling";
	public static final String ACTION_PUSH_IS_OVERRIDEN_MSG_HANDLING = "isDefaultMessageHandlingOverriden";
	public static final String ACTION_PUSH_NOTIFY_NOTIFICATION_OPENED = "notifyNotificationOpened";
	public static final String ACTION_PUSH_SET_TIMEZONE_OFFSET_IN_MINUTES = "setTimezoneOffsetInMinutes";
  public static final String ACTION_PUSH_SET_TIMEZONE_OFFSET_AUTOMATIC_UPDATE_ENABLED = "setTimezoneOffsetAutomaticUpdateEnabled";
  public static final String ACTION_PUSH_GET_DEVICE_ID = "getDeviceId";
  public static final String ACTION_PUSH_SET_USER_ID = "setUserId";
  public static final String ACTION_PUSH_GET_USER_ID = "getUserId";
  public static final String ACTION_PUSH_ADD_MEDIA_VIEW = "addMediaView";


	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		try {
			clbContext = callbackContext;
			JSONObject arg_object = args.getJSONObject(0);

			cordovaWebView = this.webView;
			this.manager = new PushNotificationManager(cordova.getActivity());

			// ############# INITIALIZE #############
			if (ACTION_PUSH_INITIALIZE.equals(action)) {

				// set event listener
				notificationEventListener = arg_object.getString("notificationListener");

				// Check if there is cached notifications
				if (!cachedNnotifications.isEmpty()) {
					for (JSONObject notification : cachedNnotifications) {
						Log.d(this.TAG, "Proceed push: " + notification.toString());
						proceedNotification(notification);
					}
					cachedNnotifications.clear();
				}

				clbContext.success();
				return true;
			}

			// ############# REGISTER #############
			if (ACTION_PUSH_REGISTER.equals(action)) {

				// manager initialization
				this.setupPushManager(arg_object);

				// Check if there is cached notifications
				if (!cachedNnotifications.isEmpty()) {
					for (JSONObject notification : cachedNnotifications) {
						Log.d(this.TAG, "Proceed push: " + notification.toString());
						proceedNotification(notification);
					}
					cachedNnotifications.clear();
				}

				clbContext.success();
				return true;
			}

			// ############# UNREGISTER #############
			if (ACTION_PUSH_UNREGISTER.equals(action)) {
				this.manager.unregister();

				clbContext.success();
				return true;
			}

			// ############# IS REGISTERED #############
			if (ACTION_PUSH_IS_REGISTERED.equals(action)) {
				boolean registered = this.manager.isRegistered();

				clbContext.success(new JSONObject("{\"isRegistered\": "+ registered +"}"));
				return true;
			}

			// ############# DEBUG_MODE #############
			if (ACTION_PUSH_DEBUG_MODE.equals(action)) {
				Log.d(TAG, "MM--- Debug mode event fired. Action: " + action);
				this.setDebugModeEnabled(arg_object.getBoolean("debug"));

				clbContext.success();
				return true;
			}

			// ############# IS_DEBUG_MODE #############
			if (ACTION_PUSH_IS_DEBUG_MODE.equals(action)) {
				boolean debug = this.manager.isDebugModeEnabled();
				clbContext.success(new JSONObject("{\"debugMode\": "+ debug +"}"));
				return true;
			}

			// ############# REGISTER_CHANNELS #############
			if (ACTION_PUSH_REGISTER_CHANNELS.equals(action)) {
				JSONArray channels = arg_object.getJSONArray("channels");
				boolean removeExistingChannels = arg_object.getBoolean("removeExistingChannels");
				String registrationCallback = arg_object.getString("registrationCallback");
				this.registerToChannels(channels, removeExistingChannels, registrationCallback);

				clbContext.success();
				return true;
			}

			// ############# GET_REGISTERED_CHANNELS #############
			if (ACTION_PUSH_GET_REGISTERED_CHANNELS.equals(action)) {
				String channelsCallback = arg_object.getString("registeredChannelsCallback");
				this.getRegisteredChannels(channelsCallback);

				clbContext.success();
				return true;
			}

			// ############# CHECK_MANIFEST #############
			if (ACTION_PUSH_CHECK_MANIFEST.equals(action)) {
				this.manager.checkManifest();

				clbContext.success();
				return true;
			}

			// ########## GET_UNRECEIVED_NOTIFICATIONS ##########
			if (ACTION_PUSH_GET_UNRECEIVED_NOTIF.equals(action)) {
				this.getUnreceivedNotifications(arg_object
						.getString("unreceivedNotificationCallback"));

				clbContext.success();
				return true;
			}

			// ############# GET_REGISTRATION_DATA #############
			if (ACTION_PUSH_GET_REGISTRATION_DATA.equals(action)) {
				JSONObject regData = this.convertRegistrationDataToJson(this.manager.getRegistrationData());
				Log.d(Push.TAG, "Registration data: " + regData.toString());
				clbContext.success(regData);
				return true;
			}

			// ############# GET_APPLICATION_DATA #############
			if (ACTION_PUSH_GET_APPLICATION_DATA.equals(action)) {
				JSONObject appData = new JSONObject();
				appData.putOpt("senderId", this.manager.getSenderId());
				appData.putOpt("applicationId", this.manager.getApplicationId());
				appData.putOpt("applicationSecret", this.manager.getApplicationSecret());
				appData.putOpt("deviceId", this.manager.getDeviceId());

				clbContext.success(appData);
				return true;
			}

			// ############# IS_INITIALIZED #############
			if (ACTION_PUSH_IS_INITIALIZED.equals(action)) {
				JSONObject initialized = new JSONObject();
				initialized.put("isLibraryInitialized", this.manager.isLibraryInitialized());

				clbContext.success(initialized);
				return true;
			}

			// ############# OVERRIDE_MSG_HANDLING #############
			if (ACTION_PUSH_OVERRIDE_MSG_HANDLING.equals(action)) {
				boolean developerHandleMessage = arg_object.getBoolean("developerHandleMessage");
				this.manager.overrideDefaultMessageHandling(developerHandleMessage);

				clbContext.success();
				return true;
			}

			// ############# IS_OVERRIDEN_MSG_HANDLING #############
			if (ACTION_PUSH_IS_OVERRIDEN_MSG_HANDLING.equals(action)) {
				boolean developerHandleMessage = this.manager.isDefaultMessageHandlingOverriden();
				JSONObject response = new JSONObject();
				response.put("developerHandleMessage", developerHandleMessage);

				clbContext.success(response);
				return true;
			}

			// ############# NOTIFY_NOTIFICATION_OPENED #############
			if (ACTION_PUSH_NOTIFY_NOTIFICATION_OPENED.equals(action)) {
				this.notifyNotificationOpened(arg_object);

				clbContext.success();
				return true;
			}

			// ############# GET_DEVICE_ID #############
			if (ACTION_PUSH_GET_DEVICE_ID.equals(action)) {
				String deviceId = this.manager.getDeviceId();
				clbContext.success(new JSONObject("{\"deviceId\": " + deviceId + "}"));
				return true;
			}

			// ############# SET_TIMEZONE_OFFSET_IN_MINUTES #############
			if (ACTION_PUSH_SET_TIMEZONE_OFFSET_IN_MINUTES.equals(action)) {
				this.manager.setTimeZoneOffset(arg_object.getInt("offsetMinutes"));
				clbContext.success();
				return true;
			}

			// ############# SET_TIMEZONE_OFFSET_AUTOMATIC_UPDATE_ENABLED #############
			if (ACTION_PUSH_SET_TIMEZONE_OFFSET_AUTOMATIC_UPDATE_ENABLED.equals(action)) {
				this.manager.setTimeZoneAutomaticUpdateEnabled(arg_object.getBoolean("updataEnable"));
				clbContext.success();
				return true;
			}

			// ############# SET_USER_ID #############
			if (ACTION_PUSH_SET_USER_ID.equals(action)) {
				String userId = arg_object.getString("userId");
				this.manager.saveNewUserId(userId, new UserDataListener() {

					@Override
					public void onUserDataSavingFailed(int reason) {
						clbContext.error(reason);
					}

					@Override
					public void onUserDataSaved() {
						clbContext.success();
					}
				});

				return true;
			}

			// ############# GET_USER_ID #############
			if (ACTION_PUSH_GET_USER_ID.equals(action)) {
				String userId = this.manager.getRegistrationData().getUserId();
				clbContext.success("\"" + userId + "\"");
				return true;
			}

			// ############# ADD_MEDIA_VIEW  #############
			if (ACTION_PUSH_ADD_MEDIA_VIEW.equals(action)) {
				JSONObject notification = arg_object.getJSONObject("notification");

				String mediaData = notification.getString("mediaData");
				if (!TextUtils.isEmpty(mediaData)) {
					// setting up Intent for starting MediaActivity
					Context context=this.cordova.getActivity().getApplicationContext();
					Intent intent = new Intent(context, MediaActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(MediaActivity.START_WITH_PROGRESS);
					intent.putExtra(MediaActivity.EXTRA_DATA, mediaData);
					// starting MediaActivity
					context.startActivity(intent);

				}
				clbContext.success();
				return true;
			}

			return false;

		} catch (PushConfigurationException e) {
			// CHECK_MANIFEST EXCETION
			Log.d(TAG, "Exception manifest: " + e.toString());
			clbContext.error(e.toString());
			return false;
		}
		catch ( JSONException e) {
			System.err.println("Exception JSON: " + e.getMessage());
			clbContext.error(e.getMessage());
			return false;
		}
		catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			clbContext.error(this.makeErrorObject(e));
			return false;
		}

	}


	private JSONObject makeErrorObject(Exception e) {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "error");
			json.put("message", e.getMessage());
			json.put("stackTrace", e.toString());
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean isActive() {

		if (cordovaWebView != null) {
			return true;
		}
		return false;
	}

	@Override
	public void onPause(boolean multitasking) {
		super.onPause(multitasking);
		this.inPause = true;
	}

	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
		this.inPause = false;
	}

	public static Class getContext() {
		return clbContext.getClass();
	}

	public static void sendJavascript(String js) {
		if (null != cordovaWebView) {
			Log.d(TAG, "JS" + js);
			cordovaWebView.sendJavascript(js);
		}
	}

	public static void proceedNotification(JSONObject extras) {
		if (null != extras) {
			if (null != cordovaWebView) {
				try {
					String js = notificationEventListener + "(\""
							+ extras.getString(CordovaPushReceiver.IB_EVENT) + "\", "
							+ extras.toString() + ")";
					Log.d(TAG, js);
					sendJavascript(js);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.v(TAG, "proceedNotification: caching extras to proceed at a later time.");
				cachedNnotifications.add(extras);
			}
		}
	}

	/**
	 * Initialize Push Notification Manager
	 *
	 * @param args
	 * @throws JSONException
	 */
	private void setupPushManager(JSONObject args) throws JSONException {
		RegistrationData regData = new RegistrationData();
		List<String> channelList = null;

		Log.d(TAG, "MM--- Initialize");
		String senderId = args.getString("senderId");
		String applicationId = args.getString("applicationId");
		String applicationSecret = args.getString("applicationSecret");

		this.manager.initialize(senderId, applicationId, applicationSecret);

		JSONObject regJson = args.getJSONObject("registrationData");
		if (null != regJson) {

			String userId = regJson.getString("userId");

			JSONArray channels = regJson.getJSONArray("channels");
			channelList = this.getChannelsFromJsonArray(channels);

			regData.setUserId(userId);
			regData.setChannels(channelList);
		}

		// do not register if push is already registered
		if(!this.manager.isRegistered()){
			Log.d(TAG, "Not registered. Will register...");
			this.manager.register(regData);
			Log.d(TAG, "Registering on service...");
		} else {
			Log.d(TAG, "Is already registered");
		}

	}

	/**
	 * Create list of chanels from Channel JSON array
	 * @param array
	 * @return List<String> List of channels
	 * @throws JSONException
	 */
	private List<String> getChannelsFromJsonArray(JSONArray array) throws JSONException{
		List<String> channels = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			channels.add(array.getString(i));
		}

		return channels;
	}

	private void registerToChannels(JSONArray arrayOfChannels, boolean removeExistingChannels, final String channelRegistrationEventHandler) throws JSONException{
		List<String> channels = getChannelsFromJsonArray(arrayOfChannels);
		manager.registerToChannels(channels, removeExistingChannels, new ChannelRegistrationListener() {

			@Override
			public void onChannelsRegistered() {
				if(!channelRegistrationEventHandler.isEmpty()){
					String js = channelRegistrationEventHandler + "(\"onChannelsRegistered\", null)";
					sendJavascript(js);
				}
			}

			@Override
			public void onChannelRegistrationFailed(int reason) {
				if(!channelRegistrationEventHandler.isEmpty()){
					String js = channelRegistrationEventHandler + "(\"onChannelRegistrationFailed\", "+ reason +")";
					sendJavascript(js);
				}
			}
		});
	}

	private void getRegisteredChannels(final String registeredChannelsCallback){
		this.manager.getRegisteredChannels(new ChannelObtainListener() {

			@Override
			public void onChannelsObtained(String[] channels) {
				JSONArray jsonChannels = new JSONArray();
				for (String channel : channels) {
            jsonChannels.put(channel);
        }

				String js = registeredChannelsCallback + "(\"onChannelsObtained\", "
						+ jsonChannels.toString() + ")";
				sendJavascript(js);
			}

			@Override
			public void onChannelObtainFailed(int reason) {
				String js = registeredChannelsCallback + "(\"onChannelObtainFailed\", " + reason + ")";
				sendJavascript(js);
			}
		});
	}

	/**
	 * Set debug mode for Push Notifications
	 *
	 * @param ind  by default it is false
	 */
	private void setDebugModeEnabled(boolean ind) {
		Log.d(TAG, "MM--- Debug is: " + ind);
		this.manager.setDebugModeEnabled(ind);
	}

	private void getUnreceivedNotifications(final String unreceivedNotificationCallback) {
		this.manager.getUnreceivedNotifications(new UnreceivedNotificationsListener() {

			@Override
			public void onUnreceivedNotificationsObtained(List<PushNotification> notifications) {
					JSONArray notifArray = new JSONArray();
					for (PushNotification push : notifications) {
						notifArray.put(PushHandlerActivity.convertNotificationToJson(push));
					}

					String js = unreceivedNotificationCallback
							+ "(\"onUnreceivedNotificationsObtained\", " + notifArray.toString() + ")";
					sendJavascript(js);
			}

			@Override
				public void onUnreceivedNotificationsObtainFailed(int reason) {
					String js = unreceivedNotificationCallback
							+ "(\"onUnreceivedNotificationsObtainFailed\", " + reason + ")";
					sendJavascript(js);
			}
		});
	}

	private JSONObject convertRegistrationDataToJson(RegistrationData rd) throws JSONException{
		JSONObject registrationDataJson = new JSONObject();

		registrationDataJson.put("userId", rd.getUserId());
		registrationDataJson.put("channels", new JSONArray(rd.getChannels()));
		registrationDataJson.put("additionalInfo", new JSONObject(rd.getAdditionalInfo()));

		return registrationDataJson;
	}

	private void notifyNotificationOpened(JSONObject args) throws JSONException{
		final String pushId = args.getString("pushId");
		final String successClb = args.getString("successCallback");
		final String errorClb = args.getString("errorCallback");

		this.manager.notifyNotificationOpened(pushId, new NotificationOpenedListener() {

			@Override
			public void onNotifyNotificationOpenedSuccess() {
				String js = successClb + "()";
				sendJavascript(js);
			}

			@Override
			public void onNotifyNotificationOpenedFailed(int reason) {
				String js = errorClb + "("+ reason +")";
				sendJavascript(js);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cordovaWebView = null;
	}

	@Override
	public boolean onOverrideUrlLoading(String url) {
		return false;
	}
}
