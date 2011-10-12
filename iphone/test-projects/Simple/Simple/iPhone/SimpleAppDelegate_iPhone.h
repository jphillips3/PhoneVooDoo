//
//  SimpleAppDelegate_iPhone.h
//  Simple
//
//  Created by Trampus Richmond on 9/19/11.
//  Copyright 2011 SugarCRM. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SimpleAppDelegate.h"

@interface SimpleAppDelegate_iPhone : SimpleAppDelegate {
    
    UITextField *userInput;
    UITableView *dataTable;
    UITextView *textView;
    UILabel *appLabel;
    NSMutableArray *data;
}
- (IBAction)appendText:(id)sender;
@property (nonatomic, retain) IBOutlet UILabel *appLabel;

@property (nonatomic, retain) IBOutlet UITableView *dataTable;

@property (nonatomic, retain) IBOutlet UITextView *textView;
@property (nonatomic, retain) IBOutlet UITextField *userInput;

@end
