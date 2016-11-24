//
//  RegisterViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase

class RegisterViewController: UIViewController {
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextFIeld: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    
    var ref:FIRDatabaseReference!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    @IBAction func registerDidTapped(_ sender: Any) {
        guard let firstName = firstNameTextField.text else {
            return
        }
        guard let lastName = lastNameTextFIeld.text else {
            return
        }
        guard let email = emailTextField.text else {
            return
        }
        guard let password = passwordTextField.text else {
            return
        }
        
        FIRAuth.auth()?.createUser(withEmail: email, password: password, completion: { (thisUser, error) in
            let currentView = self
            guard let user = thisUser else {
                return
            }
            if error == nil{
                //Add to users
                let post = ["first_name": firstName,
                            "last_name": lastName,
                            "email": email,
                            "affiliation": 1,
                            "score": 80] as [String : Any]
                let childUpdates = ["/users/\(user.uid)": post]
                self.ref.updateChildValues(childUpdates)
                
                //Add to list of user for organization
                self.ref.child("/organizations/\(1)/users/\(user.uid)").setValue(true)
                //Email Verification
                user.sendEmailVerification(completion: { (error) in
                    if let error = error {
                        print(error.localizedDescription)
                        return
                    }
                    print("Sent")
                })
                
                let alertController = UIAlertController(title: EMAIL_ALERT, message: EMAIL_VERIFICATION_MESSAGE, preferredStyle: UIAlertControllerStyle.alert)
                alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: { action in
                    switch action.style{
                    case UIAlertActionStyle.default:
                        currentView.dismiss(animated: true, completion: nil)
                    default:
                        print("default?")
                    }
                    
                }))
                self.present(alertController, animated: true, completion: nil)
                
            }
            else{
                errorMessage(title: REGISTER_ERROR, message: error!.localizedDescription, location: self)
            }

        })
    }

}
