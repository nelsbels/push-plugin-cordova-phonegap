//
//  Location.h
//  PushTry
//
//  Created by Nevena Kolarevic on 12/30/13.
//
//

#import <Cordova/CDV.h>
#import "InfobipPush.h"

@interface IBLocation : CDVPlugin

-(void)enableLocation:(CDVInvokedUrlCommand *)command;
-(void)disableLocation:(CDVInvokedUrlCommand *)command;
-(void)isLocationEnabled:(CDVInvokedUrlCommand *)command;
-(void)setBackgroundLocationUpdateModeEnabled:(CDVInvokedUrlCommand *)command;
-(void)backgroundLocationUpdateModeEnabled:(CDVInvokedUrlCommand *)command;
-(void)setLocationUpdateTimeInterval:(CDVInvokedUrlCommand *)command;
-(void)getLocationUpdateTimeInterval:(CDVInvokedUrlCommand *)command;

@end
