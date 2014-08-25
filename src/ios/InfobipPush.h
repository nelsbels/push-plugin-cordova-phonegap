//
///  @file  InfobipPush.h
//  InfobipPush
//
/// @author Copyright (c) 2013 Infobip. All rights reserved.
/// @version 1.1.0

/**
 * @mainpage Infobip Push
 * @section Description
 *
 * iOS library for Infobip Push
 * Minimum iOS version 5.0
 *
 * http://push.infobip.com
 *
 * @section Dependencies
 *
 * CoreLocation.framework
 *
 * CoreTelephony.framework
 *
 * MapKit.framework
 *
 * SystemConfiguration.framework
 *
 */

#import <Availability.h>

#import "CoreLocation/CoreLocation.h"

#ifndef __IPHONE_5_0
#warning "This project uses features only available in iOS SDK 5.0 and later."
#endif

#ifndef NS_BLOCKS_AVAILABLE
#warning Infobip Push requires blocks
#endif

#pragma mark -
#pragma mark Infobip Enum Declarations

/**
 * @enum Infobip push error codes enumeration
 * @since 1.0
 */
typedef enum {
    IPPushNetworkError, /**< An error when something is wrong with network, either no network or server error */
    IPPushNoMessageIDError, /**< An error when there's no messageID in push notification and library can't execute an operation without it */
    IPPushJSONError, /**< An error with JSON encoding/decoding */
    IPPushNoLocationError, /**< An error when library can't get user location */
    IPPushNoDeviceTokenError, /**< An error when there's no device token and library can't execute an operation without it */
    IPPushNotificationChannelsArrayEmptyError, /**< An error when channels array is empty */
} InfobipPushLibraryError;

/**
 * @enum Infobip push log level types
 * @since 1.0.9
 */
typedef enum {
    IPPushLogLevelError,
    IPPushLogLevelWarn,
    IPPushLogLevelInfo,
    IPPushLogLevelDebug
} InfobipPushLogLevel;

#pragma mark -
#pragma mark Infobip Block Declarations

/**
 * A block declaration for listing subscribed channels.
 * If operation succeeded it takes array of strings otherwise, an error is produced. Example channels response: @code(@"",@"music",@"sport")@endcode where @code@""@endcode means broadcast (all channels).
 * @see InfobipPush#getListOfChannelsInBackgroundUsingBlock:()
 * @since 1.0.0
 */
typedef void (^IPChannelsListResultBlock)(BOOL succeeded, NSArray *channels, NSError *error);

/**
 * A block declaration for listing unreceived notifications.
 * If operation succeeded it takes array of InfobipPushNotification objects, otherwise an error is produced.
 * @see InfobipPush#getListOfUnreceivedNotificationsInBackgroundUsingBlock:()
 * @since 1.0.0
 */
typedef void (^IPUnreceivedNotificationsListResultBlock)(BOOL succeeded, NSArray *notifications, NSError *error);

/**
 * A block declaration for general Infobip Push response.
 * @since 1.0.0
 */
typedef void (^IPResponseBlock)(BOOL succeeded, NSError *error);

@class InfobipPushNotification;
/**
 * A block declaration for getting push notification extended info from Infobip Push.
 * If operation succeeded it takes an instance of InfobipPushNotification object, otherwise an error is produced.
 * @see InfobipPush#getListOfChannelsInBackgroundUsingBlock:()
 * @since 1.0.0
 */
typedef void (^IPPushNotificationInfoBlock)(BOOL succeeded, InfobipPushNotification *notification, NSError *error);

/**
 * Main interface for Infobip Push.
 * Provides all methods for dealing with Infobip Push services.
 *
 *  @version 1.0.0
 *  @since 1.0.0
 */
@interface InfobipPush : NSObject

#pragma mark -
#pragma mark Logger methods

/**
 * Check log mode to be enabled or disabled. If enabled, all logs will be written to the console. Default level will be "Info" (IPPushLogLevelInfo).
 *
 * @since 1.0.7
 * @param isEnabled Bool value if the log mode will be enabled or not
 */
+ (void)setLogModeEnabled: (BOOL) isEnabled;

/**
 * Check log mode to be enabled or disabled. If value of parameter isEnabled is equal to NO, then second parameter is not used. Otherwise if value is YES, all logs less and equal to the logLevel will be written to the console.
 * @since 1.1.0
 * @param isEnabled Bool value if the log mode will be enabled or not
 * @param logLevel Level of the log which will be used if the log is enabled
 */
+ (void)setLogModeEnabled: (BOOL) isEnabled withLogLevel: (InfobipPushLogLevel) logLevel;

/**
 * Check if the log mode is enabled.
 * @since 1.0.7
 */
+ (BOOL)isLogModeEnabled;

#pragma mark -
#pragma mark Initialization methods

/**
 * Sets the application id and application secret of your application. Get them at https://push.infobip.com
 * 
 * @since 1.1.0
 *
 * @param appID The application id for your Infobip Push application
 * @param appSecret The application secret for your Infobip Push application
 * @exception throws an exception when appID or appSecret are nil
 */
+(void) initializeWithAppID: (NSString *) appID appSecret: (NSString *) appSecret;

#pragma mark -
#pragma mark Location methods

/**
 Setter for location update time interval. It is an interval which triggers location updates. Minimum time interval to be set is 5 minutes. Default is 15 minutes.
 @since 1.1.0
 @param timeInterval desired location update time interval
 @warning Please consider setting the interval as long as possible to prevent battery draining
 */
+ (void)setLocationUpdateTimeInterval:(NSTimeInterval) timeInterval;

/**
 Get the location update time interval which is used as a trigger to the location update. 
 @since 1.1.0
 */
+ (NSTimeInterval)locationUpdateTimeInterval;

/**
 Register user for background location updates. Background location updates are disabled as default mode.
 @since 1.1.0
 @param isEnabled Enable or disable background location update (default is NO)
 */
+ (void)setBackgroundLocationUpdateModeEnabled:(BOOL)isEnabled;

/**
 Check if the background location updates are enabled or disabled.
 @since 1.1.0
 */
+ (BOOL)backgroundLocationUpdateModeEnabled;

/**
 Check if the location updating is active (either in foreground or background).
 @since 1.1.0
 */
+ (BOOL)locationUpdateActive;

/**
 Starts location update service (background mode have to be enabled with method setBackgroundLocationUpdateModeEnabled:). Location update time interval is by default set to 15 minutes. Minimum is 5 minutes, and can be set with the method setLocationUpdateTimeInterval:.
 @since 1.1.0
 */
+ (void)startLocationUpdate;

/**
 Stops location update service (in both foreground and background).
 @since 1.1.0
 */
+ (void)stopLocationUpdate;

/**
 Report user location only once to Infobip Push servers. Response of the request can be checked with the block statement. Minimum time interval between two user location reports is 5 minutes.
 @since 1.1.0
 @param shareLocation user location
 @deprecated
 */
+ (void)shareLocation:(CLLocation *) userLocation withBlock:(IPResponseBlock)block;

/**
 Report user location only once to Infobip Push servers. Minimum time interval between two user location reports is 5 minutes.
 @since 1.1.0
 @param shareLocation user location
 @deprecated
 */
+ (void)shareLocation:(CLLocation *) userLocation;

/**
 Retrieves and sends user location only once. If you want to send user location in time intervals see InfobipPush#shareUserLocation:setUserLocationUpdateInterval:() (deprecated)
 @since 1.0.0
 @see InfobipPush#shareUserLocation:setUserLocationUpdateInterval:()
 @param shareLocation share user location (default is NO)
 @deprecated
 */
+(void)shareUserLocation: (BOOL) shareLocation __deprecated;

/**
 Retrieves and sends user location in time intervals. (deprecated)
 @since 1.0.0
 @param shareLocation share user location (default is NO)
 @param timeInterval desired time interval (minimum is 5 minutes)
 @warning Please consider setting the interval as long as possible to prevent battery draining
 @deprecated
 */
+(void)shareUserLocation: (BOOL) shareLocation setUserLocationUpdateInterval: (NSTimeInterval) timeInterval __deprecated;

#pragma mark -
#pragma mark Timezone Offset methods

/**
 Timezone is updated by default but you can set your own timezone offset in minutes from GMT. If you manually set timezone, automatic checking for timezone changes will be disabled.
 @since 1.1.0
 @param offsetMinutes Timezone offset in minutes
 */
+(void)setTimezoneOffsetInMinutes:(NSInteger)offsetMinutes;

/**
 Timezone will be automatically updated by default. If you manually set timezone with method setTimezoneOffsetInMinutes:, automatic checking for timezone changes will be disabled. With this method you can change automatic timezone updates to be enabled or disabled according to timezone changes.
 @since 1.1.0
 @param isEnabled Timezone automatic update to be enabled or disabled
 */
+(void)setTimezoneOffsetAutomaticUpdateEnabled:(BOOL)isEnabled;

#pragma mark -
#pragma mark User Information Managements

/**
 Sets the user ID. If user ID is not set, an [UUID](http://en.wikipedia.org/wiki/Universally_unique_identifier) is created and set as user ID.
 @since 1.0.8
 @param userID desired user ID
 */
+(void)setUserID: (NSString *) userID;

/**
 Sets the user ID. If user ID is not set, an [UUID](http://en.wikipedia.org/wiki/Universally_unique_identifier) is created and set as user ID. Block can be used to check if the update operation was successful.
 @since 1.0.8
 @param userID desired user ID
 */
+(void)setUserID: (NSString *) userID usingBlock:(IPResponseBlock) block;

/**
 Gets the user ID. New method to retrieve userID is "userID".
 @see InfobipPush#userID
 @deprecated
 @since 1.0.0
 */
+(NSString *)getUserID __deprecated;

/**
 Gets the user ID. If user ID is not set, an [UUID](http://en.wikipedia.org/wiki/Universally_unique_identifier) is created and returned as user ID.
 @since 1.0.8
 */
+(NSString *)userID;

/**
 Gets the device ID.
 @since 1.0.8
 */
+(NSString *)deviceID;

#pragma mark -
#pragma mark Registration and unregistration methods

/**
 Stores new device token and registers it to Infobip Push. See other options like InfobipPush#registerWithDeviceToken:toChannels:() and InfobipPush#registerWithDeviceToken:toChannels:usingBlock:() if you want to subscribe to channels at this point or handle a completion block.
 @since 1.1.0
 @see InfobipPush#registerWithDeviceToken:toChannels:()
 @see InfobipPush#registerWithDeviceToken:toChannels:usingBlock:()
 @param newDeviceToken returned from application:didRegisterForRemoteNotificationsWithDeviceToken: method in UIApplicationDelegate
 @code - (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)newDeviceToken {
     [InfobipPush registerWithDeviceToken: newDeviceToken];
 }@endcode
 */
+(void)registerWithDeviceToken:(NSData *)newDeviceToken;

/**
 Stores new device token, registers it to Infobip Push and subscribes to channels.
 @since 1.1.0
 @see InfobipPush#registerWithDeviceToken:toChannels:usingBlock:()
 @param newDeviceToken returned from application:didRegisterForRemoteNotificationsWithDeviceToken: method in UIApplicationDelegate
 @param channels an array of strings, for example: @code[NSArray arrayWithObjects: @"music",@"sport",nil]@endcode. Empty array means broadcast (all channels).
 @code - (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)newDeviceToken {
     [InfobipPush registerWithDeviceToken: newDeviceToken toChannels: [NSArray arrayWithObjects: @"",@"music",@"sport",nil]];
 }@endcode
 */
+(void)registerWithDeviceToken: (NSData *) newDeviceToken toChannels: (NSArray *) channels;

/**
 Stores new device token, registers it to Infobip Push, subscribes to channels and returns a block.
 @since 1.1.0
 @param newDeviceToken returned from application:didRegisterForRemoteNotificationsWithDeviceToken: method in UIApplicationDelegate
 @param channels an array of strings, for example: @code[NSArray arrayWithObjects: @"",@"music",@"sport",nil]@endcode. Empty array means broadcast (all channels).
 @param block #IPResponseBlock
 */
+(void) registerWithDeviceToken: (NSData *) newDeviceToken toChannels: (NSArray *) channels usingBlock: (IPResponseBlock) block;

/**
 Unregisters device from Infobip Push service
 @since 1.0.0
 */
+(void) unregisterFromInfobipPush;

/**
 Unregisters device from Infobip Push service using block for callback responses
 @since 1.0.6
 */
+(void) unregisterFromInfobipPushUsingBlock:(IPResponseBlock) block;

/**
 Retrieves the information if user is registered or not.
 @since 1.0.0
 */
+(BOOL) isRegistered;

#pragma mark -
#pragma mark Channel Management methods

/**
 Retreives a list of channels in background and executes a block when finished
 @since 1.0.0
 @param block #IPChannelsListResultBlock
 */
+(void) getListOfChannelsInBackgroundUsingBlock: (IPChannelsListResultBlock) block;

/**
 Subscribes to channels in background with an option to remove previous channels
 @since 1.0.0
 @param channels an array of strings, for example: @code[NSArray arrayWithObjects: @"",@"music",@"sport",nil]@endcode. Empty array means broadcast (all channels).
 @param removePrevious
 @param block
 */
+(void) subscribeToChannelsInBackground: (NSArray *) channels removePrevious: (BOOL) removePrevious usingBlock: (IPResponseBlock) block;

#pragma mark -
#pragma mark Notification Handling methods

/**
 Displays an alert with message text when app gets a push notification. See other methods like InfobipPush#pushNotificationFromUserInfo:() or InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:() if you want the library to parse the userInfo dictionary for you and retrieve additional data.
 * @code - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
     [InfobipPush handlePush: userInfo];
 } @endcode
 @see InfobipPush#pushNotificationFromUserInfo:()
 @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 @see InfobipPushNotification
 @since 1.0.0
 @param userInfo dictionary from application:didFinishLaunchingWithOptions: or application: didReceiveRemoteNotification: UIApplicationDelegate method
 @return InfobipPushNotification object instance
 */
+(InfobipPushNotification *) handlePush: (NSDictionary *)userInfo;

/**
 Returns an InfobipPushNotification object instance based on userInfo dictionary.
 * @code - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
      InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo: userInfo];
      //do something with notification
 } @endcode
 @since 1.0.0
 @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 @see InfobipPushNotification
 @param userInfo dictionary from application:didFinishLaunchingWithOptions: or application: didReceiveRemoteNotification: UIApplicationDelegate method
 @return InfobipPushNotification object instance
 */
+(InfobipPushNotification *) pushNotificationFromUserInfo: (NSDictionary *)userInfo;

/**
 Retrieves additonal info for notification from Infobip Push.
 * @code - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
     InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo: userInfo];
  
     [InfobipPush pushNotificationFromUserInfo: userInfo getAdditionalInfo:^(BOOL succeeded, InfobipPushNotification *notification, NSError *error) {
         if (succeeded) {
             NSString *url = [notification.data objectForKey: @"url"];
             //open url
         }
     }];
 } @endcode
 @since 1.0.0
 @see InfobipPushNotification
 @param userInfo dictionary from application:didFinishLaunchingWithOptions: or application: didReceiveRemoteNotification: UIApplicationDelegate method
 @param block IPPushNotificationInfoBlock
 */
+(void) pushNotificationFromUserInfo: (NSDictionary *)userInfo getAdditionalInfo: (IPPushNotificationInfoBlock) block;

/**
 Confirms that push notification was received. It's useful if you want to track how many users received the message. Call this method when you get userInfo from application:didFinishLaunchingWithOptions: or application: didReceiveRemoteNotification: UIApplicationDelegate method.
 * @code - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
     InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo: userInfo];
     [InfobipPush confirmPushNotificationWasReceived: notification];
 } @endcode
 @since 1.0.0
 @param pushNotification InfobipPushNotification object
 */
+(void) confirmPushNotificationWasReceived: (InfobipPushNotification *) pushNotification;

/**
 Confirms that push notification was opened. It's useful if you want to track how many users opened the message. Call this method when you display something from a push notification.
 * @code - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
     InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo: userInfo];
     [InfobipPush confirmPushNotificationWasReceived: notification];
     [InfobipPush pushNotificationFromUserInfo: userInfo getAdditionalInfo:^(BOOL succeeded, InfobipPushNotification *notification, NSError *error) {
         if (succeeded) {
             [MessageDetailView displayPushNotification: notification];
             [InfobipPush confirmPushNotificationWasOpened: notification];
         }
     }];
 } @endcode
 @since 1.0.0
 @param pushNotification InfobipPushNotification object
 */
+(void) confirmPushNotificationWasOpened: (InfobipPushNotification *) pushNotification;

/**
 Retreives a list of unreceived notifications in background and executes a block when finished
 @since 1.0.1
 @param block #IPUnreceivedMessagesListResultBlock
 */
+(void) getListOfUnreceivedNotificationsInBackgroundUsingBlock: (IPUnreceivedNotificationsListResultBlock) block;


@end

/**
 * Infobip Push notification model.
 * An model object that's composed of parsed push notification dictionary. You can choose to let the library handle the push notification by displaying an alert by calling InfobipPush#handlePush:() or handle it yourself if you packed your notifications with additonal data.
 * @see InfobipPush#handlePush:()
 * @see InfobipPush#pushNotificationFromUserInfo:()
 * @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 * @author Copyright (c) 2013 Infobip. All rights reserved.
 * @version 1.1.0
 * @since 1.0.0
 */
@interface InfobipPushNotification : NSObject

/** @property alert
 *  @brief Push notification alert text.
 *  Push notification @verbatim"alert"@endverbatim retrieved from @verbatim"aps"@endverbatim dictionary @code{
 "aps" : {
 "alert" : "You got your emails.",
 "badge" : 9,
 "sound" : "bingbong.aiff"}
 }@endcode
 * @since 1.0.0
 */
@property (nonatomic,retain) NSString *alert;

/** @property badge
 *  @brief Push notification alert badge.
 *  Push notification @verbatim"badge"@endverbatim retrieved from @verbatim"aps"@endverbatim dictionary @code{
 "aps" : {
 "alert" : "You got your emails.",
 "badge" : 9,
 "sound" : "bingbong.aiff"}
 }@endcode
 * @since 1.0.0
 */
@property (nonatomic,retain) NSString *badge;

/** @property sound
 *  @brief Push notification alert sound.
 *  Push notification @verbatim"sound"@endverbatim retrieved from @verbatim"aps"@endverbatim dictionary @code{
 "aps" : {
 "alert" : "You got your emails.",
 "badge" : 9,
 "sound" : "bingbong.aiff"}
 }@endcode
 * @since 1.0.0
 * @warning It can be empty an string if an error occures
 */
@property (nonatomic,retain) NSString *sound;

/** @property messageID
 * @brief Infobip Push custom property messageID.
 * Message ID on Infobip Push service. It's used to retrieve notification additional info from Infobip Push service like larger text, url, etc. This is used because Apple's Push Notification Service supports the maximum of 256 bytes of push notification payload which is often not enough to pass that much information. It's also used to confirm if the message was receieved or opened.
 * @see InfobipPush#confirmPushNotificationWasReceived:()
 * @see InfobipPush#confirmPushNotificationWasOpened:()
 * @warning It can be empty an string if an error occures
 * @since 1.0.0
 */
@property (nonatomic,retain) NSString *messageID;

/** @property messageType
 * @brief Infobip Push custom property messageType.
 * Message type on Infobip Push service. It's basically a mime type to differentiate between message types.
 * @warning It can be empty an string if an error occures
 * @since 1.0.0
 */
@property (nonatomic,retain) NSString *messageType;

/** @property data
 * @brief Infobip Push custom property data. You can retrieve it by calling InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:().
 * @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 * @warning It can be nil if an error occures
 * @since 1.0.0
 */
@property (nonatomic,retain) NSDictionary *data;

/** @property additionalInfo
 * @brief Infobip Push custom property additionalInfo. You can retrieve it by calling InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:().
 * @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 * @warning It can be nil if an error occures
 * @since 1.0.0
 */
@property (nonatomic,retain) NSDictionary *additionalInfo;

/** @property mediaContent
 * @brief Infobip Push custom property that contains media content by the media notification. You can retrieve it by calling InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:(). To check if notification contains media content call method: InfobipPushNotification#isMediaNotification.
 * @see InfobipPush#pushNotificationFromUserInfo:getAdditionalInfo:()
 * @warning It can be nil if an error occures
 * @since 1.1.0
 */
@property (nonatomic,retain) NSString *mediaContent;

/**
 * Check if the notification is the media one. Notification is Media notification if it contains media content. Media content can be fetched with the propery mediaContent.
 * @since 1.1.0
 */
- (BOOL)isMediaNotification;

@end
