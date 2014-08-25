//
//  AppDelegate+notification.h
//  PushTry
//
//  Created by MMiroslav on 12/25/13.
//  Copyright (c) 2013 MMiroslav. All rights reserved.
//

#import "AppDelegate.h"
#import "InfobipPush.h"
#import "IBPush.h"

@class IBPush;

NSString *const ON_NOTIFICATION_OPENED = @"onNotificationOpened";
NSString *const ON_NOTIFICATION_RECEIVED = @"onNotificationReceived";
NSString *const ON_NOTIFICATION_ERROR = @"onError";


@interface AppDelegate (notification)

-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo;
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;

@end
