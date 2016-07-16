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

// Error Messages
let TEXT_FIELD_ERROR = "The Text Field Entries cannot be empty"
let TEXT_FIELD_EMPTY_TITLE = "Text Field Empty"
let LOGIN_ERROR = "Login Error"
let REGISTER_ERROR = "Register Error"


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

// Functions
func errorMessage(title: String, message: String, location: AnyObject) {
    let alertController = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.Alert)
    alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default, handler: nil))
    location.presentViewController(alertController, animated: true, completion: nil)
}