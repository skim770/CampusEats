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
<<<<<<< HEAD

    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var loginButton: UIButton!
=======
    var emailTextField: UITextField!
    var passwordTextField: UITextField!
    var saveSwitch: UISwitch!
    var loginButton: UIButton!
    var registerButton: UIButton!
    var logoImage: UIImageView!
    var logoText: UILabel!
    
    let defaults = UserDefaults.standard
    
>>>>>>> origin/shawn
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.hideKeyboardWhenTappedAround()
<<<<<<< HEAD
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
=======
        
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
        logoText.text = "CampusEats"
        logoText.alpha = 0.8
        
        // ##Text Field##
        // Email/Password
        let textFieldY = height/2.5
        emailTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY, width: textFieldWidth, height: textFieldHeight))
        passwordTextField = UITextField(frame: CGRect(x:width/2 - textFieldWidth/2, y:textFieldY + textFieldHeight * 2, width: textFieldWidth, height: textFieldHeight))
        emailTextField.textAlignment = NSTextAlignment.center
        passwordTextField.textAlignment = NSTextAlignment.center
        passwordTextField.isSecureTextEntry = true
        //Font
        emailTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        passwordTextField.font = UIFont(name: CUSTOM_FONT, size: CGFloat(textFieldFontSize))
        //Color
        emailTextField.textColor = UIColor.white
        passwordTextField.textColor = UIColor.white
        //Autofill
        let email = defaults.string(forKey: "Email")
        let password = defaults.string(forKey: "Password")
        if email != nil && password != nil{
            emailTextField.text = email
            passwordTextField.text = password
        }
        else{
            emailTextField.text = "Email"
            passwordTextField.text = "Password"
//            passwordTextField.secureTextEntry = false
//            saveSwitch.setOn(false, animated: false)
        }
        emailTextField.alpha = 0.8
        passwordTextField.alpha = 0.8
        
        // ##Buttons##
        // Login
        loginButton = UIButton(frame: CGRect(x:width/2 - buttonTitleWidth/2, y:height/1.5, width: buttonTitleWidth, height: buttonTitleHeight))
        loginButton.backgroundColor = UIColor.white
        loginButton.setTitle("Login", for: UIControlState())
        loginButton.addTarget(self, action: #selector(loginDidTapped), for: .touchUpInside)
        loginButton.titleLabel!.font = UIFont(name: CUSTOM_FONT, size: CGFloat(buttonFontSize))
        loginButton.setTitleColor(UIColor.black, for: UIControlState())
        loginButton.alpha = 0.8
        // Register
        registerButton = UIButton(frame: CGRect(x:width/2 - buttonTitleWidth/2, y:height - buttonTitleHeight, width: buttonTitleWidth, height: buttonTitleHeight))
        registerButton.setTitle("Don't have an account?", for: UIControlState())
        registerButton.addTarget(self, action: #selector(registerDidTapped), for: .touchUpInside)
        registerButton.titleLabel!.font = UIFont(name: CUSTOM_FONT, size: CGFloat(buttonFontSize/1.5))
        registerButton.titleLabel!.textAlignment = NSTextAlignment.center
        registerButton.alpha = 0.8
        
        // ##Add Components##
        self.view.addSubview(logoImage)
        self.view.addSubview(logoText)
        self.view.addSubview(emailTextField)
        self.view.addSubview(passwordTextField)
        self.view.addSubview(loginButton)
        self.view.addSubview(registerButton)
        
    }
    
    override func viewDidLayoutSubviews() {
        emailTextField.setBottomBorder()
        passwordTextField.setBottomBorder()
    }
    
    func doLogin(){
//        if saveSwitch.on {
            defaults.setString(emailTextField.text!, forKey: "Email")
            defaults.setString(passwordTextField.text!, forKey: "Password")
            defaults.synchronize()
//        }
//        else{
//            defaults.setValue(nil, forKey: "Email")
//            defaults.setValue(nil, forKey: "Password")
//        }
        self.performSegue(withIdentifier: "GoToHome", sender: nil)
    }
    
    func loginDidTapped() {
        if !emailTextField.hasText || !passwordTextField.hasText {
            errorMessage(TEXT_FIELD_EMPTY_TITLE, message: TEXT_FIELD_ERROR, location: self)
        }
        else {
            FIRAuth.auth()?.signIn(withEmail: emailTextField.text!, password: passwordTextField.text!, completion: { (user, error) in
                if error == nil{
                    if user?.isEmailVerified == false{
                        self.doLogin()
                    }
                    else{
                        print("not verified")
                    }
                }
                else{
                    errorMessage(LOGIN_ERROR, message: error!.localizedDescription, location: self)
                }
            })
        }
    }
    
    func registerDidTapped() {
        self.performSegue(withIdentifier: "GoToRegister", sender: nil)
    }
    
    override var preferredStatusBarStyle : UIStatusBarStyle {
        return UIStatusBarStyle.lightContent
>>>>>>> origin/shawn
    }
}
