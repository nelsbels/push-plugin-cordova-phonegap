//
//  AppDelegate+notification.m
//  PushTry
//
//  Created by MMiroslav on 12/25/13.
//  Copyright (c) 2013 MMiroslav. All rights reserved.
//

#import "AppDelegate+notification.h"
#import "MainViewController.h"

@implementation AppDelegate (notification)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    
    [InfobipPush registerWithDeviceToken:deviceToken toChannels:IBPush.channels usingBlock:^(BOOL succeeded, NSError *error) {
        if(succeeded) {
            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"onRegistered\", -1)", [IBPush notificationClb]];
            [[IBPush IBPushInstance] writeJavascript:js];
        } else {
            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"onError\", %d)", [IBPush notificationClb], error.code];
            [[IBPush IBPushInstance] writeJavascript:js];
            NSLog(@"Notification error: %d", error.code);
        }
    }];
}

-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSString* js = [[NSString alloc] initWithFormat:@"%@(\"onError\", %d)", [IBPush notificationClb], error.code];
    [[IBPush IBPushInstance] writeJavascript:js];

}

// TODO check is everything ok
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    NSLog(@"Notification Received");

    InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo:userInfo];
    [InfobipPush confirmPushNotificationWasReceived:notification];

    // convert notification to Android style and send it to JavaScript
    [InfobipPush pushNotificationFromUserInfo:userInfo getAdditionalInfo:^(BOOL succeeded, InfobipPushNotification *notification, NSError *error) {
        if (succeeded) {
            NSError* error;
            NSDictionary* cordovaReadyNotification = [[NSDictionary alloc] initWithDictionary:[IBPush convertNotificationToAndroidFormat:notification]];
            NSData* notifData = [NSJSONSerialization dataWithJSONObject:cordovaReadyNotification options:0 error:&error];
            NSString*notifJson = [[NSString alloc] initWithData:notifData encoding:NSUTF8StringEncoding];

            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"%@\", %@)", [IBPush notificationClb], ON_NOTIFICATION_OPENED, notifJson];
            NSLog(@"%@", js);
            [[IBPush IBPushInstance] writeJavascript:js];
        } else {
            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"onError\", %d)", [IBPush notificationClb], error.code];
            [[IBPush IBPushInstance] writeJavascript:js];

            NSLog(@"Notification error: %d", error.code);
        }
    }];
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    NSLog(@"Notification Received: didReceiveRemoteNotification:fetchCompletionHandler:");

    InfobipPushNotification *notification = [InfobipPush pushNotificationFromUserInfo:userInfo];
    [InfobipPush confirmPushNotificationWasReceived:notification];
    NSString * event = nil;
    if((application.applicationState == UIApplicationStateActive) || (application.applicationState == UIApplicationStateInactive)) {
        event = ON_NOTIFICATION_OPENED;
        [InfobipPush confirmPushNotificationWasOpened:notification];
    } else {
        event = ON_NOTIFICATION_RECEIVED;
    }

    // convert notification to Android style and send it to JavaScript
    [InfobipPush pushNotificationFromUserInfo:userInfo getAdditionalInfo:^(BOOL succeeded, InfobipPushNotification *notification, NSError *error) {
        if (succeeded) {
            NSError* error;
            NSDictionary* cordovaReadyNotification = [[NSDictionary alloc] initWithDictionary:[IBPush convertNotificationToAndroidFormat:notification]];
            NSData* notifData = [NSJSONSerialization dataWithJSONObject:cordovaReadyNotification options:0 error:&error];
            NSString* notifJson = [[NSString alloc] initWithData:notifData encoding:NSUTF8StringEncoding];

            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"%@\", %@)", [IBPush notificationClb], event, notifJson];
            NSLog(@"%@", js);
            [[IBPush IBPushInstance] writeJavascript:js];
        } else {
            NSString* js = [[NSString alloc] initWithFormat:@"%@(\"%@\", %d)", [IBPush notificationClb], ON_NOTIFICATION_ERROR, error.code];
            [[IBPush IBPushInstance] writeJavascript:js];

            NSLog(@"Notification error: %d", error.code);
        }
    }];

    completionHandler(UIBackgroundFetchResultNewData);
}

@end
