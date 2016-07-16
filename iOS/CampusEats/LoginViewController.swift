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
            
            let email = defaults.stringForKey("Email")
            let password = defaults.stringForKey("Password")
            print(email)
            print(password)
            
        }
        else{
            defaults.setValue(nil, forKey: "Email")
            defaults.setValue(nil, forKey: "Password")
        }
        
        let vc = self.storyboard?.instantiateViewControllerWithIdentifier("HomeView")
        self.presentViewController(vc!, animated: false, completion: nil)
    }
    
    @IBAction func loginDidTapped(sender: AnyObject) {
        FIRAuth.auth()?.signInWithEmail(emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
            if error == nil{
                print("Login successful")
                self.doLogin()
            }
            else{
                print("The Username and password combination does not exist")
                print(error?.localizedDescription)
            }
        })
    }
}

