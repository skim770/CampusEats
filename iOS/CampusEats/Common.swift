//
//  Common.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import Foundation
import UIKit

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

extension UserDefaults {
    func setString(string:String, forKey:String) {
        set(string, forKey: forKey)
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

extension Date {
    func getMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMMM"
        return dateFormatter.string(from: self)
    }
}

// Functions
func errorMessage(title: String, message: String, location: AnyObject) {
    let alertController = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
    alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
    location.present(alertController, animated: true, completion: nil)
}
