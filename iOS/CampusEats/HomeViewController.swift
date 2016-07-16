//
//  HomeViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/16/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import Foundation
import Firebase

class HomeViewController: UIViewController {
    
    @IBAction func LogoutDidTapped(sender: AnyObject) {
        let firebaseAuth = FIRAuth.auth()
        do {
            try firebaseAuth?.signOut()
            let vc = self.storyboard?.instantiateViewControllerWithIdentifier("LoginView")
            self.presentViewController(vc!, animated: false, completion: nil)
        } catch let signOutError as NSError {
            print ("Error signing out: %@", signOutError)
        }
    }
}