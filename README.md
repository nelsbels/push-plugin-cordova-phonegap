Infobip Push Notification Plugin for Cordova
====================================

Infobip Push is a service by Infobip Ltd. ([Infobip Push](https://push.infobip.com)) providing its clients with the  ability to send push notifications to various devices, enabling  rich media push, geographical targeted sending areas, delivery reports, and much more.

Installation
------------

To install the plugin to your Cordova project use the Cordova CLI Tool:

	$ cordova plugin add com.infobip.push.cordova

Requirements
------------

* `Android™`
	* Set the minimal required Android SDK version to 8 because GCM push is enabled only from that Android OS version and above.
* `iOS™`
	* Tested on iOS 6 and 7.
	* Manage certificates as explained in "APNs setup manual" section [here](https://push.infobip.com/docs).

Basic Usage
-----------

### Initialization

Once you've added the plugin to your project, you will be able to use it in JavaScript code as `push`.

The first thing you should do is to initialize push plugin with `push.initialize(notificationListenerCallbackName)`. You provide this function with the name (string) of your call-back function which hold the signature `function(event, data)`. The first parameter `event` (string) will take on following values:

* `onNotificationReceived` - when a device receives a push notification from Infobip's Push service. In this case, the `data` argument of this function will be a JSON object representing a notification.
* `onInvisibleNotificationReceived` - the device receives a push notification from Infobip's Push service. In this case, `data` argument of this function will be a JSON object representing the notification.
* `onNotificationOpened` - when an user opens the received notification from the notification bar. In this case, the `data` argument of this function will be a JSON object representing the notification.
* `onUnregistered` - when an application successfully unregisters from Infobip's Push service. In this case, the `data` argument doesn't play a significant role.
* `onRegistered` - when an application successfully registers to Infobip's Push service. In this case, the `data` argument doesn't play a significant role.
* `onError` - when an error occurs. This time, the `data` argument will simply be error code. Error codes are listed below under the section named "Error Codes".

#### Usage examples:

This code will initialize push notifications with `notificationListener` call-back listener:

	push.initialize(notificationListener);

Sample implementation of `notificationListener` call-back listener:

	notificationListener: function(event, notification){
        switch(event){
            case ("onNotificationReceived"):
                // TODO your code here
                break;
            case ("onInvisibleNotificationReceived"):
                // TODO your code here
                break;
            case ("onNotificationOpened"):
                // TODO your code here
                break;
            case ("onUnregistered"):
                // TODO your code here
                break;
            case ("onRegistered"):
                // TODO your code here
                break;
            case ("onError"):
                // TODO your code here
                break;
            default:
                // TODO your code here
                break;
        }
    }

### Registration

You need only one more function to make the magic happen. It's `push.register(regData);` and it should be called after calling `push.initialize(notificationListener);` function. It's first argument is a JSON object containing the mandatory:

* `applicationId` - Application UID from Infobip's Push Portal.
* `applicationSecret` - Application Secret from Infobip's Push Portal.
* `senderId` - represents your Google Project number, obtained from the Google API Console (mandatory and specific for Android)

and the optional:

* `registrationData` - JSON object containing:
	* `userId` - User ID which you can use for custom targeting of push notifications from the portal. If omitted, it will be random hash.
	* `channels` - JSON array of strings representing channel names to which the application will register.

#### Example of a registration object

	regData: {
	    applicationId: "<YOUR-APPLICATION-ID>",
	    applicationSecret: "<YOUR-APPLICATION-SECRET>",
	    senderId: "<YOUR-SENDER-ID>",

	    registrationData: {
	        userId: "<SOME-USER-ID>",
	        channels:
	        [
	            "news",
	            "sport",
	            "infobip"
	        ]
	    }
	}

That was the core of your application's interaction with our plugin. Other than that, there are multiple functions you can use. We will list them all in the following chapter.

Advanced usage
--------------

#### Notification

The received notification has the following form:

	{
	    "notificationId":"<52d916a...00006e7>",
	    "message":"<Some message>",
	    "aditionalInfo":
	    {
	        "key1":"value1",
	        "key2":"value2"
	    },
	    "mediaData":"",
	    "url":"<http://google.com>",
	    "title":"<Message title>",
	    "sound":true,
	    "lights":false, // Android only
	    "vibrate":false, // Android only
	    "mimeType":"text/html",
	    "badge":"10" // iOS only
	}

Depending on the operating system, some fields will bw non-existent.

* `lights` and `vibrate` are only available on Android.
* `badge` is only available on iOS


#### UserId

Overrides the previously defined userID with a new one:

	push.setUserId(newUserId, successCallback, errorCallback),

	successCallback: function(),
	errorCallback: function(errorCode),

Error call-back accepts the following error codes:

| Error Code| Description 					|
| ---------:| ------------------------------|
| 1			| INTERNET_NOT_AVAILABLE		|
| 2 		| PUSH_SERVICE_NOT_AVAILABLE	|
| 4 		| USER_NOT_REGISTERED			|
| 12 		| OPERATION_FAILED				|
| 512 		| LIBRARY_NOT_INITIALIZED		|

Get current userId:

	push.getUserId(successCallback),
	successCallback: function(userId) {
		alert(userId);
	}

#### DebugMode

To get logs, set debug mode enabled to `true`:

	push.setDebugModeEnabled(true, logLevel);
	push.setDebugModeEnabled(true);

`logLevel` is integer that can take on values from 0 to 3, and works only on iOS.
It is possible to set it on Android too, but it does not affect the result.

Check if the debug	mode has been set or not with following code:

	push.isDebugModeEnabled(successCallback),
	successCallback: function(data) {
		alert(data.debugMode);
	}

`data` is object that contains boolean field debugMode.

#### Channels

If the user is already registered, subscribe him/her to channels by using

	push.registerToChannels(args, successCallback, errorCallback);

`arguments` is a JSON object like the following:

	{
	    channels: [
	        "channel1",
	        "channel2",
	        "channel3"
	    ],
	    removeExistingChannels: true
	}

`successCallback` is a function with no arguments indicating successful registration to channels, and `errorCallback` is a function with one argument (error code) indicating unsuccessful registration.

User will be registered to provided channels, with the new channels created on Infobip Push service if you haven't already created them per application. Once you set `removeExistingChannels` field to true, existing channels on the Push service will be deleted, and a list of new channels will replace them. If false, existing channels will stay intact and user will be registered to the newly provided list of channels. Monitor channel registration success by providing callback functions.

Channels to which you registered your user are saved on Infobip's Push service. You can obtain them by using:

	push.getRegisteredChannels(getRegisteredChannelsCallback, errorCallback);

Call-back that accepts previously obtained channels should look like this:

	getRegisteredChannelsCallback: function(channels)

and error call-back should look like:

	errorCallback: function(error)

`channels` is a JSON list of obtained channels.
`error` is one of following error codes in table below

List of possible error codes

| Error Code| Description 					|
| ---------:| ------------------------------|
| 1			| INTERNET_NOT_AVAILABLE		|
| 2 		| PUSH_SERVICE_NOT_AVAILABLE	|
| 4 		| USER_NOT_REGISTERED			|
| 12 		| OPERATION_FAILED				|
| 512 		| LIBRARY_NOT_INITIALIZED		|

#### Registration

`push.unregister();` - unregister from Infobip's Push Notification service. `onUnregistered` event of notification listener call-back (set in `push.initialize`) will be launched upon unregistering.

Checks whether the application is registered to Infobip's Push service

`push.isRegistered(isRegisteredClb);`

`isRegisteredClb` should look like this:

	isRegisteredClb: function(response) {
		alert(response.isRegistered);
	}

The call-back function has one argument (JSON object) which has a field `isRegistered` of boolean type.
Alternatively, this should take on the following form:

	push.isRegistered(function(response){alert(response.isRegistered);});

#### Unreceived

To get a list of unreceived push notifications simply call this function:

	push.getUnreceivedNotifications(unreceivedNotificationsCallback, errorCallback);

`unreceivedNotificationsCallback` is call-back function that accepts one parameter `notificationArray` (JSON array of notification objects) and could look like this:

    unreceivedNotificationsCallback: function(notificationArray)

and `errorCallback` like the following, where the error represents one of the error codes from table below:

	errorCallback: function(error)

| Error Code| Description 					|
| ---------:| ------------------------------|
| 1			| INTERNET_NOT_AVAILABLE		|
| 2 		| PUSH_SERVICE_NOT_AVAILABLE	|
| 4 		| USER_NOT_REGISTERED			|
| 12 		| OPERATION_FAILED				|
| 512 		| LIBRARY_NOT_INITIALIZED		|

#### DeviceId

Device ID is a unique device identifier in Infobip's Push system. It can be used to send push notifications to a specific user. It will be created only once. To get it, use the following function:

	push.getDeviceId(successCallback)

`successCallback` accepts one parameter (JSON Object) that contain field `deviceId`. Usage example:

	successCallback: function(response){
    	alert(response.deviceId);
	});

#### Notify Notification Opened

To notify the Infobip Push Service that the notification has been opened, call

	notifyNotificationOpened(args)

`args` parameter is a JSON Object like the following:

	{
	    notificationId: "<NOTIFICATION-ID>",
	    successCallback: function(){},
	    errorCallback: function(error){}
	}

On iOS, automatically notifying that a notification has been opened currently isn't possible so you need to call this function manually.

Also, on Android, when the application receives invisible notification (see `overrideDefaultMessageHandling`), you need to manually call the `notifyNotificationOpened` function.

### Location

In our Push library version 1.1.1 we introduced our own location service that acquires your user's latest location and periodically sends it to the Infobip Push service in the background. By using this service, your location can be retrieved with all the location providers: GPS, NETWORK or PASSIVE provider.

Start Push location service using `push.enableLocation()` to track your user's location and stop it with `push.disableLocation()` method. Once started, Push location service periodically sends location updates to the Infobip's Push service. Time interval between these location updates can be set by `push.setLocationUpdateTimeInterval(interval, errorCallback)`, where `interval` is integer value in minutes, and `errorCallback` is call-back function with one argument that represents error code.

To enable location service, use function below:

	push.enableLocation();

and to disable sending location to Push Service use the following function:

	push.disableLocation();

To check if Infobip's Push location service is enabled use

	isLocationEnabled(successClb)

where `successClb` is like the following:

	successClb: function(data) {
		alert(data.isLocationEnabled);
	}

Success callback accepts one parameter (JSON Object) that has boolean field `isLocationEnabled`.

#### Time Interval (location)

To change default time interval for updating your user's location (15 min), use the following function:

	push.setLocationUpdateTimeInterval(interval, errorCallback);

where `errorCallback` is like following:

	errorCallback: function(errorCode) {
		alert("Error code: " + errorCode);
	}

Getting current update time interval is possible with the following function:

	push.getLocationUpdateTimeInterval(successClb);

where `successClb` is like the following:

	successClb: function(data) {
		alert(data.getLocationUpdateTimeInterval)
	}

Only parameter in call-back function is like following:

	{
		getLocationUpdateTimeInterval: 25 // In minutes
	}


#### Media Data

Media content refers to multimedia content (image, video, audio) wrapped inside HTML tags. You can check if notification received has any media content simply by comparing it's `mediaData` field to `undefined` or empty string, which is also done in `push.isMediaNotification(notification, callback)` function. Here, `callback` is function with one boolean parameter, indicating whether notification contains any media data.

#### Media View

Media View is used to show media content from the media push notification. Using Infobip's Media View is optional which means that at any time you can create your own kind of Media View where you can show the media content from notification. Infobip's Media View offers basic functionality of showing media content inside rounded web view with the default shadow around it. View also has a dismiss button through which the user can dismiss the Media View. Any of these fields can be changed according to your application needs.

To use Infobip's Media View call function below:

	push.addMediaView(notification, customization, errorCallback);

`notification` is notification with media content received from Infobip Push server,
`customization` is JSON object used to customize media view outlook and is like the following:

	{
		x: 10,
		y: 20,
		width: 100,
		height: 200,
		shadow: true,
		radius: 15,
		dismissButtonSize: 20,
		foregroundColor: "#ffffff",
		backgroundColor: "#000000"
	}

`errorCallback` is call-back function that accepts one parameter (`errorCode`) and is like the following:

	errorCallback: function(errorCode) {
		alert("Error code: " + errorCode);
	}

### Android only

#### AndroidManifest

To make sure that nothing required is missing in your Android manifest file, use the following method. Any error will be logged, so please check your LogCat to verify that the manifest has been properly configured.

	checkManifest(successCallback, errorCallback)

Note: Debug mode has to be enabled to view the log.

#### Application Data

To obtain application data call the following function:

	getApplicationData(successCallback)

`successCallback` accepts one parameter (JSON Object), that should look like following:

	successCallback: function(data)

`data` parameter is like the following:

	{
	     senderId: "<SENDER-ID>",
	     deviceId: "<DEVICE-ID>",
	     applicationId: "<APPLICATION-ID>",
	     applicationSecret: "<APPLICATION-SECRET>",
	}

#### Builder Data

To obtain builder data call the following function:

	getBuilderData(successCallback, errorCallback)

`successCallback` accepts one parameter (JSON Object), that should look like following:

	successCallback: function(data)

To set builder data call the following function:

	setBuilderData: function(data, successCallback, errorCallback);

`data` parameter on both cases should look like the following:

    {
        tickerText: "Infobip Push Demo",
        applicationName: "Infobip Push Demo App",
        sound: -1,
        vibration: -1,
        light: -1,
        vibrationPattern: [100, 100],
        lightsColor: 10201,
        lightsOnOffMS: {
            on: 100,
            off: 200
        },
        quietTime: {
            startHour: 23,
            startMinute: 30,
            endHour: 8,
            endMinute: 45
        }
    }


* `tickerText` represents text to scroll across the screen when this item is added to the status bar.
* `applicationName` represents the application name shown in the notification drawer as a title if the title isn't sent via push notification.  
* `sound`, `vibration` and `light` can take on one of three possible values.
	* -1 means UNSET
	*  0 means DISABLED
	*  1 means ENABLED
* `vibrationPattern` Pass in an array of ints that are the durations for which to turn on or off the vibrator in milliseconds.
* `lightsColor` Sets the color of the LED light.
* `lightsOnOffMS` Sets the number of milliseconds for the LED to be on and off while it's flashing.
* `quietTime` Quiet time is an interval set with `startHour`, `startMinute`, `endHour`, `endMinute` that determines the time when the sound, vibration and flashing lights won't perform on notification reception. Hours are presented in a 24-hour format.

<!--Using this function, you can override default message handling on your Android. Combined with `setBuilderData`, you can highly customize your application's notifications. TODO: give link or extensive description of how this can be done
-->

#### Override Default Message Handling

Use the following function to override default message handling, where `shouldOverride` value is set to true:

	overrideDefaultMessageHandling(shouldOverride)

Overriding default notification handling means that:

* the library won't display the received notification
* to track notification opened statistics you will have to manually call the `notifyNotificationOpened(args)` function

#### Remove Saved Builder Data

To remove saved builder data call the following function:

	push.removeSavedBuilderData();

#### Enable Quiet Time

Set a time when sound, vibration and flashing lights won't be active for your user by implementing quiet time.

	push.setQuietTimeEnabled(ind);

where `ind` is boolean that indicates the state of quiet time.
Quiet time interval is set with the start hour, start minute, end hour, end minute parameters, in `data` object passed to `setBuilderData`,  where time is set in a 24-hour time format.

#### Timezone offset

Automatic update of timezone offset is enabled by default. The value of the timezone offset is the time difference in minutes between GMT and your user's current location. Information on timezone offset for each user can be useful when sending a scheduled notification for which you want each user to receive it in the specific time according to the timezone they are in.

You can manually set timezone offset in minutes using the following function:

	push.setTimezoneOffsetInMinutes(minutes);

If you manually set timezone offset then the default automatic timezone offset updates will be disabled. Also, if you enable automatic timezone offset updates again, then the manually timezone offset value will be overridden by automatic updates. To enable automatic timezone offset updates you should use the following function:

	push.setTimezoneOffsetAutomaticUpdateEnabled(ind);

where `ind` is boolean value.

### iOS only

#### Badge number

iOS will automatically set the application's badge number to the `badge` (number) field received in the push notification. It's up to you to handle the badge number within the application according to unread notifications count. Use this code anywhere in the app to set the badge number:

	push.setBadgeNumber(num);

#### Dismiss All Notifications

To dismiss all notifications from notification bar call the following function:

	push.cancelAllNotifications();

#### Background Location Update

On iOS, location updates work only when the application is active. Background location updates are disabled by default. To enable background location updates, use the following function:

	push.setBackgroundLocationUpdateModeEnabled(enabled)

where `enabled` is boolean value.

To check if background location update is enabled on iOS, use the following function:

	push.backgroundLocationUpdateModeEnabled(successClb);

where `successClb` is function like:

	successClb: function(data) {
		alert(data.isBackgroundLocation);
	}

Data parameter is JSON object like the following:

	{
		isBackgroundLocation: true
	}

Error codes
-----------

### Android error codes

<table><tr>
    <th>
        Reason
    </th>
    <th>
        Performed operation
    </th>
    <th>
        Description
    </th>
    <th>
        Value
    </th>
</tr>
<tbody>
<tr>
    <td>
        OPERATION_FAILED
    </td>
    <td>
        <code>register</code>
        <code>unregister</code>
        <code>registerToChannels</code>
        <code>getRegisteredChannels</code>
        <code>notifyNotificationOpened</code>
        <code>getUnreceivedNotifications</code>
        <code>saveNewUserId</code>
    </td>
    <td>
        Performed operation failed
    </td>
    <td>
        12
    </td>
</tr>
<tr>
    <td>
        INTERNET_NOT_AVAILABLE
    </td>
    <td>
        <code>register</code>
        <code>unregister</code>
        <code>registerToChannels</code>
        <code>getRegisteredChannels</code>
        <code>notifyNotificationOpened</code>
        <code>getUnreceivedNotifications</code>
        <code>saveNewUserId</code>
    </td>
    <td>
        Internet access is unavailable
    </td>
    <td>
        1
    </td>
</tr>
<tr>
    <td>
        PUSH_SERVICE_NOT_AVAILABLE
    </td>
    <td>
        <code>register</code>
        <code>unregister</code>
        <code>registerToChannels</code>
        <code>getRegisteredChannels</code>
        <code>notifyNotificationOpened</code>
        <code>getUnreceivedNotifications</code>
        <code>saveNewUserId</code>
    </td>
    <td>
        Infobip Push service is currently unavailable
    </td>
    <td>
        2
    </td>
</tr>
<tr>
    <td>
        USER_NOT_REGISTERED
    </td>
    <td>
        <code>unregister</code>
        <code>registerToChannels</code>
        <code>getRegisteredChannels</code>
        <code>notifyNotificationOpened</code>
        <code>getUnreceivedNotifications</code>
        <code>saveNewUserId</code>
    </td>
    <td>
        User is not registered for receiving push notifications
    </td>
    <td>
        4
    </td>
</tr>
<tr>
    <td>
        GCM_ACCOUNT_MISSING
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        Google account is missing on the phone
    </td>
    <td>
        8
    </td>
</tr>
<tr>
    <td>
        GCM_AUTHENTICATION_FAILED
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        Authentication failed – your user's Google account password is invalid
    </td>
    <td>
        16
    </td>
</tr>
<tr>
    <td>
        GCM_INVALID_SENDER
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        Invalid GCM sender ID
    </td>
    <td>
        32
    </td>
</tr>
<tr>
    <td>
        GCM_PHONE_REGISTRATION_ERROR
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        Phone registration error occurred
    </td>
    <td>
        64
    </td>
</tr>
<tr>
    <td>
        GCM_INVALID_PARAMETERS
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        Invalid parameters are sent
    </td>
    <td>
        128
    </td>
</tr>
<tr>
    <td>
        GCM_SERVICE_NOT_AVAILABLE
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        GCM service is unavailable
    </td>
    <td>
        256
    </td>
</tr>
<tr>
    <td>
        LIBRARY_NOT_INITIALIZED
    </td>
    <td>
        <code>register</code>
        <code>unregister</code>
        <code>registerToChannels</code>
        <code>getRegisteredChannels</code>
        <code>notifyNotificationOpened</code>
        <code>getUnreceivedNotifications</code>
        <code>saveNewUserId</code>
    </td>
    <td>
        Library is not initialized
    </td>
    <td>
        512
    </td>
</tr>
<tr>
    <td>
        USER_ALREADY_REGISTERED
    </td>
    <td>
        <code>register</code>
    </td>
    <td>
        User is already registered
    </td>
    <td>
        1024
    </td>
</tr>
</tbody>
</table>

### iOS error codes

<table><tr>
    <th>
        Reason
    </th>
    <th>
        Description
    </th>
    <th>
        Value
    </th>
</tr>
<tbody>
<tr>
    <td>
       IPPushNetworkError
    </td>

    <td>
        An error when something is wrong with network, either no network or server error.
    </td>
    <td>
        0
    </td>
</tr>
<tr>
    <td>
        IPPushNoMessageIDError
    </td>

    <td>
        An error when there's no messageID in push notification and library can't execute an operation without it.


    </td>
    <td>
        1
    </td>
</tr>
<tr>
    <td>
        IPPushJSONError
    </td>
        <td>
        An error with JSON encoding/decoding.
    </td>
    <td>
        2
    </td>
</tr>
<tr>
    <td>
        IPPushNoLocationError
    </td>
       <td>
        An error when library can't get user location.
    </td>
    <td>
        3
    </td>
</tr>
<tr>
    <td>
        IPPushNoDeviceTokenError
    </td>
        <td>
        An error when there's no device token and library can't execute an operation without it.
    </td>
    <td>
        4
    </td>
</tr>
<tr>
    <td>
       IPPushNotificationChannelsArrayEmptyError
    </td>
       <td>
        An error when channels array is empty.
       </td>
    <td>
        5
    </td>
</tr>


</tbody>
</table>


Owners
------

Framework Integration Team @ Belgrade, Serbia

*Android is a trademark of Google Inc.*

*iOS is a trademark of Cisco in the U.S. and other countries and is used under license.*

© 2013-2014, Infobip Ltd.
