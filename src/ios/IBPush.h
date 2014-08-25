//
//  IBPush.h
//  Cordova iOS Hello
//
//  Created by MMiroslav on 12/25/13.
//
//

#import <Cordova/CDV.h>
#import "InfobipPush.h"
#import "InfobipMediaView.h"


@interface IBPush : CDVPlugin<InfobipMediaViewDelegate>

+ (NSArray *)channels;
+ (NSString *)callbackId;
+ (NSString *)notificationClb;
+ (IBPush *)IBPushInstance;


-(void)initialize:(CDVInvokedUrlCommand *)command;
-(void)registerOnPushService:(CDVInvokedUrlCommand *)command;
-(void)unregister:(CDVInvokedUrlCommand *)command;
-(void)isRegistered:(CDVInvokedUrlCommand *)command;

-(void)setUserId:(CDVInvokedUrlCommand *)command;
-(void)getUserId:(CDVInvokedUrlCommand *)command;

-(void)getDeviceId:(CDVInvokedUrlCommand *)command;

-(void)setDebugModeEnabled:(CDVInvokedUrlCommand *)command;
-(void)isDebugModeEnabled:(CDVInvokedUrlCommand *)command;

-(void)registerToChannels:(CDVInvokedUrlCommand *)command;
-(void)getRegisteredChannels:(CDVInvokedUrlCommand *)command;

-(void)notifyNotificationOpened:(CDVInvokedUrlCommand *)command;

+(NSDictionary *)convertNotificationToAndroidFormat:(InfobipPushNotification *)notification;

-(void)cancelAllNotifications:(CDVInvokedUrlCommand *)command;

-(void)getUnreceivedNotifications:(CDVInvokedUrlCommand *)command;

-(void)setTimezoneOffsetInMinutes:(CDVInvokedUrlCommand *)command;
-(void)setTimezoneOffsetAutomaticUpdateEnabled:(CDVInvokedUrlCommand *)command;

-(void)setBadgeNumber:(CDVInvokedUrlCommand *)command;

-(void)addMediaView:(CDVInvokedUrlCommand *)command;
@end
