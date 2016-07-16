//
//  ViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/12/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import UIKit
import Firebase

class LoginViewController: UIViewController {
    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    @IBOutlet var saveSwitch: UISwitch!
    
    
    let defaults = NSUserDefaults.standardUserDefaults()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        
        let email = defaults.stringForKey("Email")
        let password = defaults.stringForKey("Password")
        if email != nil && password != nil{
            emailTextField.text = email
            passwordTextField.text = password
        }
        else{
            saveSwitch.setOn(false, animated: false)
        }
    }
    
    func doLogin(){
        if saveSwitch.on {
            defaults.setString(emailTextField.text!, forKey: "Email")
            defaults.setString(passwordTextField.text!, forKey: "Password")
            defaults.synchronize()
        }
        else{
            defaults.setValue(nil, forKey: "Email")
            defaults.setValue(nil, forKey: "Password")
        }
        self.performSegueWithIdentifier("GoToHome", sender: nil)
    }
    
    @IBAction func loginDidTapped(sender: AnyObject) {
        if !emailTextField.hasText() || !passwordTextField.hasText() {
            errorMessage(TEXT_FIELD_EMPTY_TITLE, message: TEXT_FIELD_ERROR, location: self)
        }
        else {
            FIRAuth.auth()?.signInWithEmail(emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
                if error == nil{
                    self.doLogin()
                }
                else{
                    errorMessage(LOGIN_ERROR, message: error!.localizedDescription, location: self)
                }
            })
        }
    }
}


