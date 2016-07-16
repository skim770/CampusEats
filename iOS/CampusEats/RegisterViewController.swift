//
//  RegisterViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/16/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import Foundation
import Firebase

class RegisterViewController: UIViewController {
    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    
    @IBAction func RegisterDidTapped(sender: AnyObject) {
        FIRAuth.auth()?.createUserWithEmail(emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
            if error == nil{
                print("Register successful")
                let vc = self.storyboard?.instantiateViewControllerWithIdentifier("HomeView")
                self.presentViewController(vc!, animated: false, completion: nil)
            }
            else{
                print(error?.localizedDescription)
            }
        })
    }
}