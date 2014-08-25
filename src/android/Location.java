package com.infobip.push.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infobip.push.PushLocationManager;

public class Location extends CordovaPlugin {
	private static CallbackContext clbContext;
	private static PushLocationManager locationManager;

	public static final String ACTION_PUSH_LOCATION_ENABLE = "enableLocation";
	public static final String ACTION_PUSH_LOCATION_DISABLE = "disableLocation";
	public static final String ACTION_PUSH_LOCATION_UPDATE_TIME_INTERVAL = "setLocationUpdateTimeInterval";
	public static final String ACTION_PUSH_LOCATION_IS_LOCATION_ENABLED = "isLocationEnabled";
	public static final String ACTION_PUSH_LOCATION_GET_UPDATE_TIME_INTERVAL = "getLocationUpdateTimeInterval";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		try {
			clbContext = callbackContext;
			JSONObject arg_object = args.getJSONObject(0);
			locationManager = new PushLocationManager(cordova.getActivity());

			// ############# LOCATION_ENABLE #############
			if (ACTION_PUSH_LOCATION_ENABLE.equals(action)) {
				locationManager.enableLocation();

				clbContext.success();
				return true;
			}

			// ############# LOCATION_ENABLE #############
			if (ACTION_PUSH_LOCATION_DISABLE.equals(action)) {
				locationManager.disableLocation();

				clbContext.success();
				return true;
			}

			// ############# LOCATION_UPDATE_TIME_INTERVAL #############
			if (ACTION_PUSH_LOCATION_UPDATE_TIME_INTERVAL.equals(action)) {
				int interval = arg_object.getInt("interval");
				locationManager.setLocationUpdateTimeInterval(interval);

				clbContext.success();
				return true;
			}
			
			 // ############# IS_LOCATION_ENABLED #############
			if (ACTION_PUSH_LOCATION_IS_LOCATION_ENABLED.equals(action)) {
				boolean isLocationEnabled = this.locationManager.isLocationEnabled();
				clbContext.success(new JSONObject("{\"isLocationEnabled\": "+ isLocationEnabled +"}"));
				return true;
			}
			
            // ############# GET_UPDATE_TIME_INTERVAL #############
			if (ACTION_PUSH_LOCATION_GET_UPDATE_TIME_INTERVAL.equals(action)) {
				int timeInterval = this.locationManager.getLocationUpdateTimeInterval();
				clbContext.success(new JSONObject("{\"getLocationUpdateTimeInterval\": "+ timeInterval +"}"));
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
}
