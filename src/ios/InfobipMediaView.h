//
//  @file  InfobipMediaView.h
//  InfobipMediaView
//
// @author Copyright (c) 2013 Infobip. All rights reserved.
// @version 1.0.0

/**
 * @mainpage Infobip Media View
 * @section Description
 *
 * Library for Infobip Push Media View to show media content
 * Minimum iOS version 5.0
 *
 * http://push.infobip.com
 *
 * @section Dependencies
 *
 * QuartzCore.framework
 *
 */

#import <UIKit/UIKit.h>

@class InfobipMediaView;

/**
 * Infobip Push Media View delegate that is used to be triggered by the dismiss action of the Infobip Media View.
 */
@protocol InfobipMediaViewDelegate <NSObject>

/**
 * Delegate method that is called if the default dismiss button is clicked on the Infobip Media View. Delegation is optional because you can define your own action or even your own dismiss button.
 * @since 1.0.0
 */
@optional
-(void)didDismissInfobipMediaView:(InfobipMediaView *)infobipMediaView;
@end

/**
 * Infobip Push Media View that can show media content from the Infobip Push notifications.
 * In order to use Media View you need to initialize it and add mediaContent from the object InfobipPushNotification.mediaContent.
 */
@interface InfobipMediaView : UIView

/**
 * Dismiss button that is used for action of dismissing a media view that is showing media content.
 * @since 1.0.0
 */
@property (nonatomic, retain) UIButton *dismissButton;

/**
 * Delegate to which the default action when the dismiss button is tapped will be delegated.
 * @since 1.0.0
 */
@property (nonatomic, assign) id<InfobipMediaViewDelegate> delegate;

/**
 * Media content that is presented in the Infobip Media View.
 * @since 1.0.0
 */
@property (nonatomic, copy) NSString *mediaContent;

/**
 * Enable or disable the shadow around the Infobip Media View. Shadow is enabled by the default.
 * @since 1.0.0
 */
@property (nonatomic) BOOL shadowEnabled;

/**
 * Enable or disable the scroll functionallity of the media view that is showing the media content. Scroll is disabled by the default.
 * @since 1.0.0
 */
@property (nonatomic) BOOL scrollEnabled;

/**
 * Size of the corner radius of Infobip Media View. By default radius is set to 15.0. If youa do not want corner radius, set this value to 0.
 * @since 1.0.0
 */
@property (nonatomic) int cornerRadius;

/**
 * Initialization of the Infobip Media View with the media content to represent.
 * @since 1.0.0
 * @param frame Frame of the Infobip Media View
 * @param mediaContent Media content to be presented in the view
 */
- (id)initWithFrame:(CGRect)frame andMediaContent:(NSString *)mediaContent;

/**
 * Setting default dismiss button size. If you define your own dismiss button you should not use this method because it only makes changes to the default dismiss button. Default size is 30.
 * @since 1.0.0
 * @param size Size of the dismiss button in the top right corner of the view
 */
- (void)setDismissButtonSize:(int)size;

/**
 * Setting default dismiss button size and colors. If you define your own dismiss button you should not use this method because it only makes changes to the default dismiss button. Default size is 30. Default background color is black, while the default foreground color is white.
 * @since 1.0.0
 * @param size Size of the dismiss button in the top right corner of the view
 * @param backgroundColor Background color of the dismiss button
 * @param foregroundColor Foreground color of the dismiss button
 */
- (void)setDismissButtonSize:(int)size withBackgroundColor:(UIColor *)backgroundColor andForegroundColor:(UIColor *)foregroundColor;

@end
