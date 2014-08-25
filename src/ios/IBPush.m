//
//  IBPush.m
//  Cordova iOS Hello
//
//  Created by MMiroslav on 12/25/13.
//
//

#import "IBPush.h"


#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0];

@implementation IBPush

//@synthesize notificationClb;

// static channels
static NSArray* channels;
+(NSArray *)channels { return channels; }
+(void)setChannels:(NSArray *)newChannels { channels = newChannels; }

// static callbackId
static NSString* callbackId;
+(NSString*)callbackId { return callbackId; }
+(void)setCallbackId:(NSString *)newCallbackId { callbackId = newCallbackId; }

static NSString* notificationClb;
+(NSString*)notificationClb { return notificationClb; }
+(void)setNotificationClb:(NSString *)newCallback { notificationClb = newCallback; }

static IBPush *gInstance = nil;
+(IBPush*)IBPushInstance {
    return gInstance;
}

-(void)initialize:(CDVInvokedUrlCommand *)command {
    gInstance = self;
    NSLog(@"IBPush.m: Initialize");
    NSDictionary* jsonObject = [command.arguments objectAtIndex:0];
    notificationClb = [jsonObject objectForKey:@"notificationListener"];

    if ([InfobipPush isRegistered]) {
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes: (UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound)];
    }

    // result
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}

-(void)registerOnPushService:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: Registration");

    NSDictionary* jsonObject = [command.arguments objectAtIndex:0];

    // parse JSON data
    NSString* applicationId = [jsonObject objectForKey:@"applicationId"];
    NSString* applicationSecret = [jsonObject objectForKey:@"applicationSecret"];
    NSDictionary* registrationData = [jsonObject objectForKey:@"registrationData"];
    NSArray* channelsArray = [registrationData objectForKey:@"channels"];
    NSString* userId = [registrationData objectForKey:@"userId"];

    // set channels
    channels = channelsArray;
    if (channels == nil) {
        channels = [NSArray arrayWithObjects:@"ROOT", nil];
    }

    // set callbackId
    callbackId = command.callbackId;

    // set userId if exist
    if (nil != userId) {
        [InfobipPush setUserID:userId];
    }

    // initialization and registration
    [InfobipPush initializeWithAppID:applicationId appSecret:applicationSecret];
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes: (UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound)];
}


-(void)setUserId:(CDVInvokedUrlCommand *)command {
    NSDictionary* jsonObject = [command.arguments objectAtIndex:0];
    NSString* userId = [jsonObject objectForKey:@"userId"];

    // If userId is provided
    if (nil != userId) {
        [InfobipPush setUserID:userId usingBlock:^(BOOL succeeded, NSError *error) {
            if (succeeded) {
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            } else {
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageToErrorObject:error.code];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            }
        }];
    } else {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_JSON_EXCEPTION];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

-(void)getUserId:(CDVInvokedUrlCommand *)command {
    NSString* userId = [InfobipPush userID];
    NSMutableDictionary* response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: userId, @"userId",  nil];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


-(void)getDeviceId:(CDVInvokedUrlCommand *)command {
    NSString* deviceId = [InfobipPush deviceID];
    NSMutableDictionary* response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: deviceId, @"deviceId",  nil];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)isRegistered:(CDVInvokedUrlCommand *)command {
    BOOL isReg = [InfobipPush isRegistered];
    NSMutableDictionary * response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: [NSNumber numberWithBool:isReg], @"isRegistered",  nil];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)unregister:(CDVInvokedUrlCommand *)command {
    [InfobipPush unregisterFromInfobipPushUsingBlock:^(BOOL succeeded, NSError *error) {
        if (succeeded) {
            NSString * js = [[NSString alloc] initWithFormat:@"%@(\"onUnregistered\", -1)", notificationClb];
            [self writeJavascript:js];
        } else {
            NSString * js = [[NSString alloc] initWithFormat:@"%@(\"onError\", %d)", notificationClb, error.code];
            [self writeJavascript:js];
        }
    }];
}


-(void)registerToChannels:(CDVInvokedUrlCommand *)command {
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSArray * channels = [jsonObject objectForKey:@"channels"];
    NSNumber * removeExistingChannels = [jsonObject objectForKey:@"removeExistingChannels"];
    NSString * callback = [jsonObject objectForKey:@"registrationCallback"];

    [InfobipPush subscribeToChannelsInBackground:channels removePrevious:[removeExistingChannels boolValue] usingBlock:^(BOOL succeeded, NSError *error) {
        NSString* js = nil;
        if (succeeded) {
            js = [NSString stringWithFormat:@"%@(\"onChannelsRegistered\", -1)", callback];
        } else {
            js = [NSString stringWithFormat:@"%@(\"onChannelRegistrationFailed\", %d)", callback, error.code];
        }

        [self writeJavascript:js];
    }];
}

-(void)getRegisteredChannels:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: getRegisteredChannels");

    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSString * callback = [jsonObject objectForKey:@"registeredChannelsCallback"];

    [InfobipPush getListOfChannelsInBackgroundUsingBlock:^(BOOL succeeded, NSArray *channels, NSError *error) {
        NSString* js = nil;
        if (succeeded) {
            //convert channels to json
            NSError * error = 0;
            NSData *channelsJson = [NSJSONSerialization dataWithJSONObject:channels options:0 error:&error];
            NSString *jsonString = [[NSString alloc] initWithData:channelsJson encoding:NSUTF8StringEncoding];
            js = [NSString stringWithFormat:@"%@(\"onChannelsObtained\", %@)", callback, jsonString];
        } else {
            js = [NSString stringWithFormat:@"%@(\"onChannelObtainFailed\", %d)", callback, error.code];
        }

        [self writeJavascript:js];
    }];

}

-(void)setDebugModeEnabled:(CDVInvokedUrlCommand *)command{
    NSLog(@"IBPush.m: setDebugModeEnable");
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *debugObj = [jsonObject objectForKey:@"debug"];
    BOOL debug =(BOOL)debugObj;
    NSNumber *level=[jsonObject objectForKey:@"logLevel"];;
    InfobipPushLogLevel logLevel= [level intValue];

    if (nil != level) {
        [InfobipPush setLogModeEnabled:debug withLogLevel:(logLevel)];
    } else {
        [InfobipPush setLogModeEnabled:debug];
    }

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


-(void)isDebugModeEnabled:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: isDebugModeEnabled");
    BOOL isLogModeEnabled = [InfobipPush isLogModeEnabled];
    NSMutableDictionary * response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: [NSNumber numberWithBool:isLogModeEnabled], @"isLogModeEnabled",  nil];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


-(void)notifyNotificationOpened:(CDVInvokedUrlCommand *)command {
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSString * pushId = [jsonObject objectForKey:@"pushId"];
    NSLog(@"PushID: %@", pushId);
    InfobipPushNotification* tmpNotification = [[InfobipPushNotification alloc] init];
    [tmpNotification setMessageID:pushId];

    [InfobipPush confirmPushNotificationWasOpened:tmpNotification];
}

-(void)cancelNotification:(CDVInvokedUrlCommand *)command {
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSString * pushId = [jsonObject objectForKey:@"pushId"];
    NSLog(@"PushID: %@", pushId);
    InfobipPushNotification* tmpNotification = [[InfobipPushNotification alloc] init];
    [tmpNotification setMessageID:pushId];

    // TODO convert InfobipPushnotification to UILocalNotification
    //[[UIApplication sharedApplication] cancelLocalNotification:tmpNotification];
}

-(void)cancelAllNotifications:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: cancelAllNotifications");

    [[UIApplication sharedApplication] cancelAllLocalNotifications];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


-(void)getUnreceivedNotifications:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: getUnreceivedNotifications");

    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSString * clb = [jsonObject objectForKey:@"unreceivedNotificationCallback"];

    [InfobipPush getListOfUnreceivedNotificationsInBackgroundUsingBlock:^(BOOL succeeded, NSArray *notifications, NSError *error) {
        NSString * js = nil;
        if (succeeded) {
            NSMutableArray * notificationsArray = [[NSMutableArray alloc] init];
            for (InfobipPushNotification *notification in notifications) {
                [notificationsArray addObject:[IBPush convertNotificationToAndroidFormat:notification]];
            }

            NSError * error = 0;
            NSData *notificationsData = [NSJSONSerialization dataWithJSONObject:notificationsArray options:0 error:&error];
            NSString *jsonString = [[NSString alloc] initWithData:notificationsData encoding:NSUTF8StringEncoding];
            js = [NSString stringWithFormat:@"%@(\"onUnreceivedNotificationsObtained\", %@)", clb, jsonString];
        } else {
            NSLog(@"IBPush.m: Error occured while geting unreceived notifications.");
            js = [NSString stringWithFormat:@"%@(\"onUnreceivedNotificationsObtainFailed\", %d)", clb, error.code];
        }
        NSLog(@"%@", js);
        [self writeJavascript:js];
    }];
}


-(void) setTimezoneOffsetInMinutes:(CDVInvokedUrlCommand*)command {
    NSLog(@"IBPush.m: setTimezoneOffsetInMinutes");
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *offsetMin = [jsonObject objectForKey:@"offsetMinutes"];
    NSInteger offsetMinutes =(NSInteger)offsetMin;

    if (nil != offsetMin) {
        [InfobipPush setTimezoneOffsetInMinutes:offsetMinutes];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } else {
        // if offsetMinutes is not provided
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_JSON_EXCEPTION];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}


-(void)setTimezoneOffsetAutomaticUpdateEnabled:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: setTimezoneOffsetAutomaticUpdateEnabled");
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *updataObj = [jsonObject objectForKey:@"updataEnable"];
    BOOL updataEnable =(BOOL)updataObj;

    [InfobipPush setTimezoneOffsetAutomaticUpdateEnabled:updataEnable];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

InfobipMediaView *mediaView = nil;

-(void)addMediaView:(CDVInvokedUrlCommand *)command {
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSDictionary * notification = [jsonObject objectForKey:@"notification"];
    NSDictionary * customization = [jsonObject objectForKey:@"customization"];

    NSString * mediaContent = [notification objectForKey:@"mediaData"];
    NSNumber * x = [customization objectForKey:@"x"];
    NSNumber * y = [customization objectForKey:@"y"];
    NSNumber * width = [customization objectForKey:@"width"];
    NSNumber * height = [customization objectForKey:@"height"];
    NSNumber * shadow = [customization objectForKey:@"shadow"]; //BOOL
    NSNumber * radius = [customization objectForKey:@"radius"]; //int

    NSNumber * dismissButtonSize = [customization objectForKey:@"dismissButtonSize"]; //int
    NSNumber * forgroundColorHex = [customization objectForKey:@"forgroundColor"]; //hex
    NSNumber * backgroundColorHex = [customization objectForKey:@"backgroundColor"]; //hex
    UIColor * forgroundColor = UIColorFromRGB([forgroundColorHex integerValue]);
    UIColor * backgroundColor = UIColorFromRGB([backgroundColorHex integerValue]);

    UIView *topView = [[UIApplication sharedApplication] keyWindow].rootViewController.view;
    CGRect frame = CGRectMake([x floatValue], [y floatValue], [width floatValue], [height floatValue]);
    mediaView = [[InfobipMediaView alloc] initWithFrame:frame andMediaContent:mediaContent];

    //set the size od dismiss button
    if(nil != dismissButtonSize){
        if ((nil != backgroundColor) && (nil != forgroundColor)) {
            [mediaView setDismissButtonSize:[dismissButtonSize integerValue]
                        withBackgroundColor:backgroundColor andForegroundColor:forgroundColor];
        } else {
            [mediaView setDismissButtonSize:[dismissButtonSize integerValue]];
        }
    }

    // disabe/enable shadow
    if (nil != shadow) {
        mediaView.shadowEnabled = [shadow boolValue];
    }

    // corner radius
    if (nil != radius) {
        mediaView.cornerRadius = [radius integerValue];
    } else {
        mediaView.cornerRadius = 0;
    }

    // Add action with selector "dismissInfobipMediaView" to the dismiss button
    [mediaView.dismissButton addTarget:self action:@selector(dismissInfobipMediaView:) forControlEvents:UIControlEventTouchUpInside];

    // display media view
    [topView addSubview:mediaView];
}

+(void)dismissInfobipMediaView:(UIButton *)sender {

    // Dismiss the Media View from the super view
    [mediaView removeFromSuperview];
}


-(void)setBadgeNumber:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: setBadgeNumber");
    NSDictionary *jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *badgeNumber = [jsonObject objectForKey:@"badgeNumber"];

    [UIApplication sharedApplication].applicationIconBadgeNumber = [badgeNumber integerValue];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


+(NSDictionary *)convertNotificationToAndroidFormat:(InfobipPushNotification *)notification {
    NSDictionary * notificationData = [notification data];
    NSMutableDictionary * newNotification = [[NSMutableDictionary alloc] init];
    NSLog(@"Notification: %@", notification);

    [newNotification setValue:[notification messageID] forKey:@"notificationId"];
    [newNotification setValue:[notification sound] forKey:@"sound"];
    [newNotification setValue:[notificationData objectForKey:@"url"] forKey:@"url"];
    [newNotification setValue:[notification additionalInfo] forKey:@"aditionalInfo"];
    [newNotification setValue:[notification mediaContent] forKey:@"mediaData"];
    [newNotification setValue:[notification alert] forKey:@"title"];
    [newNotification setValue:[notificationData objectForKey:@"message"] forKey:@"message"];
    [newNotification setValue:[notification messageType] forKey:@"mimeType"];
    [newNotification setValue:[notification badge] forKey:@"badge"];
    //    [newNotification setValue:nil forKey:@"vibrate"];
    //    [newNotification setValue:nil forKey:@"light"];

    //    return [[NSDictionary alloc] initWithDictionary:newNotification];
    return newNotification;
}

@end
