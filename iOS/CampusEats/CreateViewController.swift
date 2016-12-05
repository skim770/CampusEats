//
//  CreateViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 9/28/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import UIKit
import Firebase

class CreateViewController: UIViewController {
    var ref:FIRDatabaseReference!
    
    @IBOutlet var eventNameTextField: UITextField!
    @IBOutlet var eventLocationTextField: UITextField!
    @IBOutlet var eventDescriptionTextField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.ref = FIRDatabase.database().reference()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func SubmitDidTapped(_ sender: AnyObject) {
        let query = [
            "category": "user",
            "comments": "",
            "cost": 0,
            "date": "",
            "desc": "",
            "loc": "",
            "pubDate": "",
            "rawDate": "",
            "reports": "DNE",
            "sourceID": "User",
            "status": "active",
            "title": ""
        ] as [String : Any]
        
        self.ref.child("posts").childByAutoId().setValue(query)
        
        let alertController = UIAlertController(title: CREATE_ALERT, message: CREATE_MESSAGE, preferredStyle: UIAlertControllerStyle.alert)
        alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: { action in
            switch action.style{
            case UIAlertActionStyle.default:
                self.dismiss(animated: true, completion: nil)
            default:
                print("default?")
            }
            
        }))
        self.present(alertController, animated: true, completion: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
