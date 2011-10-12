//
//  SimpleAppDelegate_iPhone.m
//  Simple
//
//  Created by Trampus Richmond on 9/19/11.
//  Copyright 2011 SugarCRM. All rights reserved.
//

#import "SimpleAppDelegate_iPhone.h"

@implementation SimpleAppDelegate_iPhone
@synthesize appLabel;
@synthesize dataTable;
@synthesize textView;
@synthesize userInput;

- (void)dealloc
{
    [userInput release];
    [dataTable release];
    [appLabel release];
    [textView release];
	[super dealloc];
}

- (IBAction)appendText:(id)sender {
    NSString *str;
    
    str = [textView text];
    [textView setText: [NSString stringWithFormat:@"\n%@\n%@", str, userInput.text]];
    [userInput setText: @""];
    [userInput resignFirstResponder];
}

@end
