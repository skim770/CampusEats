//
//  Common.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/16/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import Foundation
import UIKit

// Variables
let RSS_FEED_URL = "http://www.calendar.gatech.edu/feeds/events.xml"
let TEXT_FIELD_FONT_SIZE = 15
let BUTTON_TITLE_FONT_SIZE = 20

// Alert Dialog
let EMAIL_VERIFICATION_MESSAGE = "Registration successful! Please check your email to verify your account."
let TEXT_FIELD_ERROR = "The Text Field Entries cannot be empty"
let TEXT_FIELD_EMPTY_TITLE = "Text Field Empty"
let LOGIN_ERROR = "Login Error"
let EMAIL_ALERT = "Thank you!"
let REGISTER_ERROR = "Register Error"
let CUSTOM_FONT = "BebasNeue"


// Extensions
extension String {
    var floatValue: Float {
        return (self as NSString).floatValue
    }
}

extension NSUserDefaults {
    func setString(string:String, forKey:String) {
        setObject(string, forKey: forKey)
    }
}

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
}

extension UITextField
{
    func setBottomBorder()
    {
        self.borderStyle = UITextBorderStyle.None;
        let border = CALayer()
        let width = CGFloat(1.5)
        border.borderColor = UIColor(red: 1.0, green: 1.0, blue: 1.0, alpha: 0.8).CGColor
        border.frame = CGRect(x: 0, y: self.frame.size.height - width,   width:  self.frame.size.width, height: self.frame.size.height)
        border.borderWidth = width
        self.layer.addSublayer(border)
        self.layer.masksToBounds = true
    }
    
}

// Functions
func errorMessage(title: String, message: String, location: AnyObject) {
    let alertController = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.Alert)
    alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default, handler: nil))
    location.presentViewController(alertController, animated: true, completion: nil)
}