var push = {
  /********************************/
  /********** Push Class **********/
  /********************************/
  push_notificationListener: function(event, data) {},
  push_notificationListenerCallback: function(event, data) {
    push.push_notificationListener(event, data);
  },
  initialize: function(notificationListener) {
    function successCallback() {};

    function errorCallback() {
      console.log("Error occurred while INITIALIZATION!");
    };
    push.push_notificationListener = notificationListener;

    cordova.exec(
      successCallback, // success callback function
      errorCallback, // error callback function
      'Push', // mapped to our native Java class called "Centili"
      'initialize', // with this action name
      [{
        "notificationListener": "push.push_notificationListenerCallback"
      }] // and this array of custom arguments
      //                [notificationListener]
    );
  },
  register: function(args) {
    function dummySuccessCallback() {};

    function errorCallback(errorCode) {
      push.push_notificationListener('onError', errorCode);
    };

    cordova.exec(
      dummySuccessCallback,
      errorCallback,
      'Push',
      'registerOnPushService', [args]
    );
  },
  setUserId: function(userId, successCallback, errorCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }
    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return
    }

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'setUserId', [{
        "userId": userId
      }]
    );
  },
  getUserId: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    function errorCallback() {
      console.log("Error occurred while getting user ID!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'getUserId', [{}]
    );
  },
  getDeviceId: function(successCallback) {
    function errorCallback() {
      console.log("Error occurred while getting deviceId!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'getDeviceId', [{}]
    );
  },
  setDebugModeEnabled: function(debug, level) {
    function successCallback() {};

    function errorCallback() {
      console.log("Error occurred while setting debug mode!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'setDebugModeEnabled', [{
        "debug": debug,
        "logLevel": level
      }]
    );
  },
  isDebugModeEnabled: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    function errorCallback() {
      console.log("Error occurred while getting debug mode!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'isDebugModeEnabled', [{}]
    );
  },
  registerToChannels_successCallback: function(data) {},
  registerToChannels_errorCallback: function(data) {},
  registerToChannelsCallback: function(event, data) {
    if (event == 'onChannelsRegistered') {
      push.registerToChannels_successCallback(data);
    } else {
      push.registerToChannels_errorCallback(data);
    }
  },
  registerToChannels: function(args, successCallback, errorCallback) {
    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return;
    };
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: successCallback parameter not a function");
      return;
    };

    function dummySuccessCallback() {};

    push.registerToChannels_successCallback = successCallback;
    push.registerToChannels_errorCallback = errorCallback;

    args.registrationCallback = 'push.registerToChannelsCallback';

    cordova.exec(
      dummySuccessCallback,
      errorCallback,
      'Push',
      'registerToChannels', [args]
    );
  },
  getRegisteredChannels_successCallback: function(data) {},
  getRegisteredChannels_errorCallback: function(data) {},
  getRegisteredChannelsCallback: function(event, channels) {
    if (event == 'onChannelsObtained') {
      push.getRegisteredChannels_successCallback(channels);
    } else {
      push.getRegisteredChannels_errorCallback(channels);
    }
  },
  getRegisteredChannels: function(successCallback, errorCallback) {
    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return;
    };
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: successCallback parameter not a function");
      return;
    };

    function dummySuccessCallback() {};

    push.getRegisteredChannels_successCallback = successCallback;
    push.getRegisteredChannels_errorCallback = errorCallback;

    cordova.exec(
      dummySuccessCallback,
      errorCallback,
      'Push',
      'getRegisteredChannels', [{
        "registeredChannelsCallback": "push.getRegisteredChannelsCallback"
      }]
    );
  },
  checkManifest: function(successCallback, errorCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return;
    }

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'checkManifest', [{}]
    );
  },
  unregister: function() {
    function successCallback() {}

    function errorCallback() {
      console.log("Error occurred while unregistering application!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'unregister', [{}]
    );
  },
  isRegistered: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    errorCallback = function() {
      console.log(
        "Error occurred while checking whether application is registered!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'isRegistered', [{}]
    );
  },
  getUnreceivedNotifications_successCallback: function() {},
  getUnreceivedNotifications_errorCallback: function() {},
  getUnreceivedNotificationsCallback: function(event, data) {
    if (event == 'onUnreceivedNotificationsObtained') {
      push.getUnreceivedNotifications_successCallback(data);
    } else {
      push.getUnreceivedNotifications_errorCallback(data);
    }
  },
  getUnreceivedNotifications: function(successCallback, errorCallback) {
    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return;
    };
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: successCallback parameter not a function");
      return;
    };

    function dummySuccessCallback() {};

    push.getUnreceivedNotifications_errorCallback = errorCallback;
    push.getUnreceivedNotifications_successCallback = successCallback;

    cordova.exec(
      dummySuccessCallback,
      errorCallback,
      'Push',
      'getUnreceivedNotifications', [{
        "unreceivedNotificationCallback": "push.getUnreceivedNotificationsCallback"
      }]
    );
  },
  getRegistrationData: function(registrationDataCallback) {
    if (typeof registrationDataCallback != "function") {
      console.log(
        "Infobip Push failure: registration data callback parameter must be a function"
      );
      return
    }

    function errorCallback() {
      console.log("Error occurred while getting registration data!");
    };

    cordova.exec(
      registrationDataCallback,
      errorCallback,
      'Push',
      'getRegistrationData', [{}]
    );
  },
  getApplicationData: function(applicationDataCallback) {
    if (typeof applicationDataCallback != "function") {
      console.log(
        "Infobip Push failure: applicationDataCallback parameter must be a function"
      );
      return
    }

    function errorCallback() {
      console.log("Error occurred while getting application data!");
    };

    cordova.exec(
      applicationDataCallback,
      errorCallback,
      'Push',
      'getApplicationData', [{}]
    );
  },
  isLibraryInitialized: function(isInitializeCallback) {
    if (typeof isInitializeCallback != "function") {
      console.log(
        "Infobip Push failure: isInitializeCallback parameter must be a function"
      );
      return
    }

    function errorCallback() {
      console.log("Error occurred checking whether library is initialized!");
    };

    cordova.exec(
      isInitializeCallback,
      errorCallback,
      'Push',
      'isLibraryInitialized', [{}]
    );
  },
  overrideDefaultMessageHandling: function(developerHandleMessage) {
    function successCallback() {}

    function errorCallback() {
      console.log("Error occurred while overriding default message handling!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'overrideDefaultMessageHandling', [{
        "developerHandleMessage": developerHandleMessage
      }]
    );
  },
  isDefaultMessageHandlingOverriden: function(developerHandleMessageCallback) {
    if (typeof developerHandleMessageCallback != "function") {
      console.log(
        "Infobip Push failure: developerHandleMessageCallback parameter must be a function"
      );
      return
    }

    function errorCallback() {
      console.log(
        "Error occurred while checking if default message handling is overriden!"
      );
    };

    cordova.exec(
      developerHandleMessageCallback,
      errorCallback,
      'Push',
      'isDefaultMessageHandlingOverriden', [{}]
    );
  },
  notifyNotificationOpened_successCallback: function() {},
  notifyNotificationOpened_errorCallback: function(errorCode) {
    console.log("Error occurred while notifying that notification is opened!");
  },
  notifyNotificationOpened: function(args) {
    function dummySuccessCallback() {};

    var successCallback = "push.notifyNotificationOpened_successCallback";
    if (typeof args.successCallback == "function") {
      push.notifyNotificationOpened_successCallback = args.successCallback;
    };

    function errorCallback() {
      console.log("Error occurred while notifying that notification is opened!");
    };
    if (typeof args.errorCallback == "function") {
      errorCallback = args.errorCallback;
      push.notifyNotificationOpened_errorCallback = errorCallback;
    };

    cordova.exec(
      dummySuccessCallback,
      errorCallback,
      'Push',
      'notifyNotificationOpened', [{
        "pushId": args.notificationId,
        "successCallback": "push.notifyNotificationOpened_successCallback",
        "errorCallback": "push.notifyNotificationOpened_errorCallback"
      }]
    );
  },
  // iOS specific
  cancelAllNotifications: function() {
    function successCallback() {}

    function errorCallback() {
      console.log(
        "Error occurred while canceling all notifications from notification bar!"
      );
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'cancelAllNotifications', [{}]
    );
  },
  // iOS specific
  setBadgeNumber: function(badgeNumber) {
    function successCallback() {}

    function errorCallback() {
      console.log("Error occurred while setting badge number!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'setBadgeNumber', [{
        "badgeNumber": badgeNumber
      }]
    );
  },

  setTimezoneOffsetInMinutes: function(offsetMinutes) {
    function successCallback() {};

    function errorCallback() {
      console.log("Error occurred while setting timezone offset in minutes!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'setTimezoneOffsetInMinutes', [{
        "offsetMinutes": offsetMinutes
      }]
    );
  },

  setTimezoneOffsetAutomaticUpdateEnabled: function(updataEnable) {
    function successCallback() {};

    function errorCallback() {
      console.log(
        "Error occurred while setting timezone offset automatic update!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'setTimezoneOffsetAutomaticUpdateEnabled', [{
        "updataEnable": updataEnable
      }]
    );
  },

  isMediaNotification: function(notification, successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    errorCallback = function() {
      console.log(
        "Error occurred while checking if notification is a media notification!"
      );
    };

    if ((notification.mediaData === undefined) || ("" == notification.mediaData)) {
      successCallback(false);
    } else {
      successCallback(true);
    }
  },

  getMediaData: function(notification, successCallback, errorCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: error callback parameter must be a function");
      return
    }

    if (notification.mediaData === undefined) {
      errorCallback("There is not media data");
    } else {
      successCallback(notification.mediaData);
    }
  },

  addMediaView: function(notification, customization, errorCallback) {
    function successCallback() {};
    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: error callback parameter must be a function");
      return;
    }

    cordova.exec(
      successCallback,
      errorCallback,
      'Push',
      'addMediaView', [{
        "notification": notification,
        "customization": customization
      }]
    );
  },


  /********************************/
  /******** Location Class ********/
  /********************************/

  enableLocation: function() {
    function successCallback() {}

    function errorCallback() {
      console.log("Error occurred while enabling location service");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'enableLocation', [{}]
    );
  },
  disableLocation: function() {
    function successCallback() {}

    function errorCallback() {
      console.log("Error occurred while disabling location service!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'disableLocation', [{}]
    );
  },

  isLocationEnabled: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    errorCallback = function() {
      console.log("Error occurred while checking is location enabled!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'isLocationEnabled', [{}]
    );
  },

  setBackgroundLocationUpdateModeEnabled: function(backgroundLocation) {
    function successCallback() {};

    function errorCallback() {
      console.log(
        "Error occurred while setting background location update mode!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'setBackgroundLocationUpdateModeEnabled', [{
        "backgroundLocation": backgroundLocation
      }]
    );
  },

  backgroundLocationUpdateModeEnabled: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    errorCallback = function() {
      console.log(
        "Error occurred while checking if backgroud location update mode is enabled!"
      );
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'backgroundLocationUpdateModeEnabled', [{}]
    );
  },

  getLocationUpdateTimeInterval: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: success callback parameter must be a function");
      return
    }

    errorCallback = function(e) {
      console.log("Error occurred while getting location update time interval!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'getLocationUpdateTimeInterval', [{}]
    );
  },


  setLocationUpdateTimeInterval: function(interval, errorCallback) {
    function successCallback() {}

    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Location',
      'setLocationUpdateTimeInterval', [{
        "interval": interval
      }]
    );
  },


  /*******************************/
  /******** Builder Class ********/
  /*******************************/

  getBuilderData: function(successCallback, errorCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: developerHandleMessageCallback parameter must be a function"
      );
      return
    }

    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Builder',
      'getBuilderData', [{}]
    );
  },
  setBuilderData: function(data, successCallback, errorCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: developerHandleMessageCallback parameter must be a function"
      );
      return
    }

    if (typeof errorCallback != "function") {
      console.log(
        "Infobip Push failure: errorCallback parameter not a function");
      return
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Builder',
      'setBuilderData', [data]
    );
  },
  removeSavedBuilderData: function() {
    function successCallback() {};

    function errorCallback() {
      console.log("Error occurred while removing saved builder data!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Builder',
      'removeSavedData', [{}]
    );
  },
  setQuietTimeEnabled: function(ind) {
    function successCallback() {};

    function errorCallback() {
      console.log("Error occurred while setting quiet time!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Builder',
      'setQuietTimeEnabled', [{
        "ind": ind
      }]
    );
  },
  isInQuietTime: function(successCallback) {
    if (typeof successCallback != "function") {
      console.log(
        "Infobip Push failure: developerHandleMessageCallback parameter must be a function"
      );
      return
    }

    function errorCallback() {
      console.log("Error occurred while setting quiet time!");
    };

    cordova.exec(
      successCallback,
      errorCallback,
      'Builder',
      'isInQuietTime', [{}]
    );
  }
};
module.exports = push;
