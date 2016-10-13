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
    var firstNameTextField: UITextField!
    var lastNameTextField: UITextField!
    var emailTextField: UITextField!
    var passwordTextField: UITextField!
    
    var loginButton: UIButton!
    var registerButton: UIButton!
    var logoImage: UIImageView!
    var logoText: UILabel!
    
    var ref:FIRDatabaseReference!
    var thisUser: FIRUser?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        
        //Background
        let backgroundImageView = UIImageView(image: UIImage(named: "Background"))
        backgroundImageView.frame = self.view.bounds
        self.view.addSubview(backgroundImageView)
        self.view.sendSubview(toBack: backgroundImageView)
        
        // ##Item Sizes##
        //General
        let width = self.view.frame.width
        let height = self.view.frame.height
        //TextField
        let textFieldWidth = width/1.5
        let textFieldHeight = width/10
        let textFieldFontSize = width/18
        //Button
        let buttonTitleWidth = width/1.5
        let buttonTitleHeight = width/6.5
        let buttonFontSize = width/15
        //Image
        let logoSize = width/3
        
        // ##Logo Image##
        logoImage = UIImageView(image: UIImage(named: "WhiteIcon"))
        logoImage.frame = CGRect(x: width/2 - logoSize/2, y: logoSize/2, width: logoSize, height: logoSize * 0.75)
        logoImage.alpha = 0.9
        logoText = UILabel(frame: CGRect(x: width/2 - logoSize, y: logoSize/2 + logoSize/2, width: logoSize * 2, height: logoSize))
        logoText.font = UIFont(name: CUSTOM_FONT, size: CGFloat(buttonFontSize*1.5))
        logoText.textColor = UIColor.white
        logoText.textAlignment = NSTextAlignment.center
        logoText.text = "Register"
        logoText.alpha = 0.8
        
        // ##Text Field##
        // Email/Password
        let textFieldY = height/2.5
        
        firstNameTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY - textFieldHeight * 4, width: textFieldWidth, height: textFieldHeight))
        lastNameTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY - textFieldHeight * 2, width: textFieldWidth, height: textFieldHeight))
        emailTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY, width: textFieldWidth, height: textFieldHeight))
        passwordTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY + textFieldHeight * 2, width: textFieldWidth, height: textFieldHeight))
        
        firstNameTextField.textAlignment = NSTextAlignment.center
        lastNameTextField.textAlignment = NSTextAlignment.center
        emailTextField.textAlignment = NSTextAlignment.center
        passwordTextField.textAlignment = NSTextAlignment.center
        passwordTextField.isSecureTextEntry = true
        
        firstNameTextField.placeholder = "First Name"
        lastNameTextField.placeholder = "Last Name"
        emailTextField.placeholder = "Email"
        passwordTextField.placeholder = "Password"
        
        //Font
        firstNameTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        lastNameTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        emailTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        passwordTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        
        //Color
        firstNameTextField.textColor = UIColor.white
        lastNameTextField.textColor = UIColor.white
        emailTextField.textColor = UIColor.white
        passwordTextField.textColor = UIColor.white
        
        //Alpha
        firstNameTextField.alpha = 0.8
        lastNameTextField.alpha = 0.8
        emailTextField.alpha = 0.8
        passwordTextField.alpha = 0.8
        
        // ##Buttons##
        // Login
        loginButton = UIButton(frame: CGRect(x:width/2 - buttonTitleWidth/2, y:height/1.5, width: buttonTitleWidth, height: buttonTitleHeight))
        loginButton.backgroundColor = UIColor.white
        loginButton.setTitle("Register", for: UIControlState())
        loginButton.addTarget(self, action: #selector(registerDidTapped), for: .touchUpInside)
        loginButton.titleLabel!.font = UIFont(name: CUSTOM_FONT, size: CGFloat(buttonFontSize))
        loginButton.setTitleColor(UIColor.black, for: UIControlState())
        loginButton.alpha = 0.8
        
        // ##Add Components##
        self.view.addSubview(firstNameTextField)
        self.view.addSubview(lastNameTextField)
        self.view.addSubview(emailTextField)
        self.view.addSubview(passwordTextField)
        self.view.addSubview(loginButton)
        
        self.ref = FIRDatabase.database().reference()
    }
    
    override func viewDidLayoutSubviews() {
        firstNameTextField.setBottomBorder()
        lastNameTextField.setBottomBorder()
        emailTextField.setBottomBorder()
        passwordTextField.setBottomBorder()
    }
    
    func backDidTapped() {
        dismiss(animated: true, completion: nil)
    }
    
    func registerDidTapped() {
        let currentView = self
        if !emailTextField.hasText || !passwordTextField.hasText || !firstNameTextField.hasText || !lastNameTextField.hasText {
            errorMessage(TEXT_FIELD_EMPTY_TITLE, message: TEXT_FIELD_ERROR, location: self)
        }
        else {
            FIRAuth.auth()?.createUser(withEmail: emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
                if error == nil{
                    //Add to users
                    let thisUser = FIRAuth.auth()?.currentUser!
                    let post = ["first_name": self.firstNameTextField.text!,
                        "last_name": self.lastNameTextField.text!,
                        "email": self.emailTextField.text!,
                        "affiliation": 1,
                        "score": 80] as [String : Any]
                    let childUpdates = ["/users/\(thisUser!.uid)": post]
                    self.ref.updateChildValues(childUpdates)
                    
                    //Add to list of user for organization
                    self.ref.child("/organizations/\(1)/users/\(thisUser!.uid)").setValue(true)
                    //Email Verification
                    user?.sendEmailVerification(completion: { (error) in
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
                    errorMessage(REGISTER_ERROR, message: error!.localizedDescription, location: self)
                }
            })
        }
    }
    
    override var preferredStatusBarStyle : UIStatusBarStyle {
        return UIStatusBarStyle.lightContent
    }
}
