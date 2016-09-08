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
    @IBOutlet var firstNameTextField: UITextField!
    @IBOutlet var lastNameTextField: UITextField!
    
    var ref:FIRDatabaseReference!
    var thisUser: FIRUser?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.hideKeyboardWhenTappedAround()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "Background")!)
        self.ref = FIRDatabase.database().reference()
    }
    @IBAction func BackDidTapped(sender: AnyObject) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    
    @IBAction func RegisterDidTapped(sender: AnyObject) {
        let currentView = self
        if !emailTextField.hasText() || !passwordTextField.hasText() || !firstNameTextField.hasText() || !lastNameTextField.hasText() {
            errorMessage(TEXT_FIELD_EMPTY_TITLE, message: TEXT_FIELD_ERROR, location: self)
        }
        else {
            FIRAuth.auth()?.createUserWithEmail(emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
                if error == nil{
                    //Add to users
                    let thisUser = FIRAuth.auth()?.currentUser!
                    let post = ["first_name": self.firstNameTextField.text!,
                        "last_name": self.lastNameTextField.text!,
                        "email": self.emailTextField.text!,
                        "affiliation": 1,
                        "score": 80]
                    let childUpdates = ["/users/\(thisUser!.uid)": post]
                    self.ref.updateChildValues(childUpdates)
                    
                    //Add to list of user for organization
                    self.ref.child("/organizations/\(1)/users/\(thisUser!.uid)").setValue(true)
                    //Email Verification
                    user?.sendEmailVerificationWithCompletion({ (error) in
                        if let error = error {
                            print(error.localizedDescription)
                            return
                        }
                        print("Sent")
                    })
                    
                    let alertController = UIAlertController(title: EMAIL_ALERT, message: EMAIL_VERIFICATION_MESSAGE, preferredStyle: UIAlertControllerStyle.Alert)
                    alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default, handler: { action in
                        switch action.style{
                        case UIAlertActionStyle.Default:
                            currentView.dismissViewControllerAnimated(true, completion: nil)
                        default:
                            print("default?")
                        }
                        
                    }))
                    self.presentViewController(alertController, animated: true, completion: nil)
                    
                }
                else{
                    errorMessage(REGISTER_ERROR, message: error!.localizedDescription, location: self)
                }
            })
        }
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent
    }
}