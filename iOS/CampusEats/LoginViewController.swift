//
//  LoginViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase

class LoginViewController: UIViewController {

    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.hideKeyboardWhenTappedAround()
        loginButton.layer.cornerRadius = 5
    }
    
    @IBAction func loginDidTapped(_ sender: Any) {
        guard let email = emailTextField.text else {
            return
        }
        guard let password = passwordTextField.text else {
            return
        }
        FIRAuth.auth()?.signIn(withEmail: email, password: password, completion: { (user, error) in
            if (error != nil) {
                self.performSegue(withIdentifier: "login", sender: nil)
            }
            else {
                print(error?.localizedDescription ?? "There was an unknown error")
            }
        })
    }
}
