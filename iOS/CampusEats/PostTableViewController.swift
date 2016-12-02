//
//  PostTableViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/28/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase

class PostTableViewController: UITableViewController {

    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var locationTextField: UITextField!
    @IBOutlet weak var startTimeTextField: UITextField!
    @IBOutlet weak var endTimeTextField: UITextField!
    @IBOutlet weak var summaryTextField: UITextField!
    @IBOutlet weak var detailsTextField: UITextField!
    @IBOutlet weak var imageLocationTextField: UITextField!
    
    var ref: FIRDatabaseReference!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ref = FIRDatabase.database().reference()
    }

    @IBAction func PostDidTapped(_ sender: Any) {
        guard let title = titleTextField.text else {
            return
        }
        guard let location = locationTextField.text else {
            return
        }
        guard let summary = summaryTextField.text else {
            return
        }
        guard let start = startTimeTextField.text else {
            return
        }
        guard let end = endTimeTextField.text else {
            return
        }
        guard let details = detailsTextField.text else {
            return
        }
        
        guard let user = FIRAuth.auth()?.currentUser else {
            return
        }
        
        var imageLocation = ""
        if (!(imageLocationTextField.text?.isEmpty)!) {
            imageLocation = imageLocationTextField.text!
        }
        
        let key = ref.child("posts").childByAutoId().key
        let post = ["author": user.uid,
                    "body": details,
                    "changed_epoch": title,
                    "changed_gmt": title,
                    "contact": title,
                    "created_epoch": title,
                    "created_gmt": title,
                    "email": user.uid,
                    "end": end,
                    "end_gmt": end,
                    "end_last": end,
                    "end_last_gmt": end,
                    "fee": "FREE",
                    "feedback_score": 1,
                    "image": imageLocation,
                    "location": location,
                    "phone": "",
                    "start": start,
                    "start_gmt": start,
                    "summary": summary,
                    "summary_withTags": summary,
                    "times": [[
                        "date_type": "datetime",
                        "end": end,
                        "rrule": "",
                        "start": start,
                        "timezone": "America/New_York",
                        "timezone_db": "America/New_York"
                    ]],
                    "times_gmt": [[
                        "date_type": "datetime",
                        "end": end,
                        "rrule": "",
                        "start": start,
                        "timezone": "America/New_York",
                        "timezone_db": "America/New_York"
                    ]],
                    "title": title,
                    "type": "user",
                    "uid": user.uid,
                    "url": "gatech.edu"] as [String : Any]
        
        let childUpdates = ["/posts/\(key)": post]
        ref.updateChildValues(childUpdates)
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func CancelDidTapped(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

    @IBAction func StartTimeEditing(_ sender: UITextField) {
        let datePickerView = UIDatePicker()
        sender.inputView = datePickerView
        datePickerView.addTarget(self, action: #selector(PostTableViewController.startTimeValueChanged), for: UIControlEvents.valueChanged)
    }
    
    @IBAction func EndTimeEditing(_ sender: UITextField) {
        let datePickerView = UIDatePicker()
        sender.inputView = datePickerView
        datePickerView.addTarget(self, action: #selector(PostTableViewController.endTimeValueChanged), for: UIControlEvents.valueChanged)
    }
    
    func startTimeValueChanged(sender:UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = DATE_FORMAT
        startTimeTextField.text = dateFormatter.string(from: sender.date)
    }
    
    func endTimeValueChanged(sender:UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = DATE_FORMAT
        endTimeTextField.text = dateFormatter.string(from: sender.date)
    }
    
    // MARK: - Table view data source


    /*
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
