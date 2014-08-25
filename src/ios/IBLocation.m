//
//  Location.m
//  PushTry
//
//  Created by Nevena Kolarevic on 12/30/13.
//
//

#import "IBLocation.h"

@implementation IBLocation

-(void)enableLocation:(CDVInvokedUrlCommand *)command {
    NSLog(@"Location.m: enableLocation");
    
    [InfobipPush startLocationUpdate];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

};
-(void)disableLocation:(CDVInvokedUrlCommand *)command {
    NSLog(@"ILocation.m: disableLocation");
    [InfobipPush stopLocationUpdate];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};

-(void)isLocationEnabled:(CDVInvokedUrlCommand *)command {
    NSLog(@"ILocation.m: isLocationEnabled");
    BOOL isLocation = [InfobipPush locationUpdateActive];
    NSMutableDictionary * response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: [NSNumber numberWithBool:isLocation], @"isLocationEnabled",  nil];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};
-(void)setBackgroundLocationUpdateModeEnabled:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: setBackgroundLocationUpdateModeEnabled");
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *backgroundLocationObj = [jsonObject objectForKey:@"backgroundLocation"];
    BOOL backgroundLocation =(BOOL)backgroundLocationObj;
    
    [InfobipPush setBackgroundLocationUpdateModeEnabled:backgroundLocation];
        
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
    
};
-(void)backgroundLocationUpdateModeEnabled:(CDVInvokedUrlCommand *)command {
    NSLog(@"ILocation.m: backgroundLocationUpdateModeEnabled");
    BOOL isBackgroundLocation = [InfobipPush backgroundLocationUpdateModeEnabled];
    NSMutableDictionary * response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: [NSNumber numberWithBool:isBackgroundLocation], @"isBackgroundLocation",  nil];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};

-(void)setLocationUpdateTimeInterval:(CDVInvokedUrlCommand *)command {
    NSLog(@"IBPush.m: setLocationUpdateTimeInterval");
    NSDictionary * jsonObject = [command.arguments objectAtIndex:0];
    NSNumber *intervalObj = [jsonObject objectForKey:@"interval"];
    int interval =(int)intervalObj;
    if (nil!=intervalObj) {
        
       [InfobipPush setBackgroundLocationUpdateModeEnabled:interval];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
          [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        
    }else{
        // if interval is not provided
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_JSON_EXCEPTION];
          [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
   
    } 
};

-(void)getLocationUpdateTimeInterval:(CDVInvokedUrlCommand *)command {
    NSLog(@"ILocation.m: getLocationUpdateTimeInterval");
    int interval = [InfobipPush locationUpdateTimeInterval];
    NSMutableDictionary * response = [[NSMutableDictionary alloc] initWithObjectsAndKeys: [NSNumber numberWithBool:interval], @"interval",  nil];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};

@end
