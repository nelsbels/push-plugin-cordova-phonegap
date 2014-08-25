package com.infobip.push.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.infobip.push.PushNotificationBuilder;

public class Builder extends CordovaPlugin {
	private static CallbackContext clbContext;
	private static PushNotificationBuilder builder;

	private static final Object ACTION_PUSH_BUILDER_GET_DATA = "getBuilderData";
	private static final Object ACTION_PUSH_BUILDER_SET_DATA = "setBuilderData";
	private static final Object ACTION_PUSH_BUILDER_REMOVE_SAVED_DATA = "removeSavedData";
	private static final Object ACTION_PUSH_BUILDER_SET_QUIET_TIME_ENABLED = "setQuietTimeEnabled";
	private static final Object ACTION_PUSH_BUILDER_IS_IN_QUIET_TIME = "isInQuietTime";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		builder = new PushNotificationBuilder(cordova.getActivity());

		try {
			clbContext = callbackContext;
			JSONObject arg_object = args.getJSONObject(0);

			// ############# BUILDER_GET_DATA #############
			if (ACTION_PUSH_BUILDER_GET_DATA.equals(action)) {
				JSONObject data = this.getBuilderData();

				clbContext.success(data);
				return true;
			}

			// ############# BUILDER_SET_DATA #############
			if (ACTION_PUSH_BUILDER_SET_DATA.equals(action)) {
				this.setBuilderData(arg_object);

				clbContext.success();
				return true;
			}

			// ############# BUILDER_REMOVE_SAVED_DATA #############
			if (ACTION_PUSH_BUILDER_REMOVE_SAVED_DATA.equals(action)) {
				builder.removeSavedData();

				clbContext.success();
				return true;
			}

			// ############# BUILDER_SET_QUIET_TIME_ENABLED #############
			if (ACTION_PUSH_BUILDER_SET_QUIET_TIME_ENABLED.equals(action)) {
				boolean ind = arg_object.getBoolean("ind");
				builder.setQuietTimeEnabled(ind);

				clbContext.success();
				return true;
			}

			// ############# BUILDER_IS_IN_QUIET_TIME #############
			if (ACTION_PUSH_BUILDER_IS_IN_QUIET_TIME.equals(action)) {
				boolean ind = builder.isInQuietTime();
				JSONObject json = new JSONObject();
				json.put("isInQuietTime", ind);

				clbContext.success(json);
				return true;
			}

			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			clbContext.error(e.getMessage());
			return false;
		}
	}

	private JSONObject getBuilderData()  throws JSONException {
		JSONObject builderData = new JSONObject();

		builderData.put("tickerText", builder.getTickerText());
		builderData.put("applicationName", builder.getApplicationName());
		builderData.put("sound", builder.getSound());
		builderData.put("vibration", builder.getVibration());
		builderData.put("light", builder.getLight());
		builderData.put("vibrationPattern", builder.getVibrationPattern());
		builderData.put("lightsColor", builder.getLightsColor());

		JSONObject lights = new JSONObject();
		lights.put("lightsOffMS", builder.getLightsOffMS());
		lights.put("lightsOnMS", builder.getLightsOnMS());
		builderData.put("lightsOnOffMS", lights);

		JSONObject quietTime = new JSONObject();
		quietTime.put("startHour", builder.getStartHour());
		quietTime.put("endHour", builder.getEndHour());
		quietTime.put("startMinute", builder.getStartMinute());
		quietTime.put("endMinute", builder.getEndMinute());
		builderData.put("quietTime", quietTime);

		builderData.put("quietTimeEnabled", builder.isQuietTimeEnabled());

		return builderData;
	}

	private void setBuilderData(JSONObject arg_object) throws JSONException {
		if(arg_object.has("tickerText")){
			builder.setTickerText(arg_object.getString("tickerText"));
		}
		if(arg_object.has("applicationName")){
			builder.setApplicationName(arg_object.getString("applicationName"));
		}
		if(arg_object.has("sound")){
			builder.setSound(arg_object.getInt("sound"));
		}
		if(arg_object.has("vibration")){
			builder.setVibration(arg_object.getInt("vibration"));
		}
		if(arg_object.has("light")){
			builder.setLight(arg_object.getInt("light"));
		}
		if (arg_object.has("vibrationPattern")) {
			JSONArray jsonArray = arg_object.getJSONArray("vibrationPattern");
			long[] vibPattern = new long[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				vibPattern[i] = jsonArray.getInt(i);
			}
			builder.setVibrationPattern(vibPattern);
		}
		if(arg_object.has("lightsColor")){
			builder.setLightsColor(arg_object.getInt("lightsColor"));

		}
		if(arg_object.has("lightsOnOffMS")){
			JSONObject lights = arg_object.getJSONObject("lightsOnOffMS");
			int on = lights.getInt("on");
			int off = lights.getInt("off");
			builder.setLightsOnOffMS(on, off);
		}
		if(arg_object.has("quietTime")){
			JSONObject quietTime = arg_object.getJSONObject("quietTime");
			int startHour = quietTime.getInt("startHour");
			int startMinute = quietTime.getInt("startMinute");
			int endHour = quietTime.getInt("endHour");
			int endMinute = quietTime.getInt("endMinute");
			builder.setQuietTime(startHour, startMinute, endHour, endMinute);
		}
	}

}
