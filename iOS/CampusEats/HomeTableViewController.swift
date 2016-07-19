//
//  HomeTableViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/16/16.
//  Copyright © 2016 FFOC. All rights reserved.
//

import UIKit
import Firebase

class HomeTableViewController: UITableViewController {
    
    var posts = [Post]()
    var ref:FIRDatabaseReference!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        ref = FIRDatabase.database().reference()
        populatePosts()
    }
    
    @IBAction func RefreshDidTapped(sender: AnyObject) {
        posts = [Post]()
        populatePosts()
    }
    
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
    
    func populatePosts(){
        let activePosts = ref.child("posts").queryOrderedByChild("date")
        activePosts.observeSingleEventOfType(FIRDataEventType.Value, withBlock: {(snapshot) in
            if (snapshot.childrenCount > 0){
                for item in snapshot.children {
                    if item.value!["status"] as! String == "Active"{
                        let title = item.value!["title"] as! String
                        print(title)
                        let description = item.value!["desc"] as! String
                        let date = item.value!["date"] as! String
                        let post = Post(title: title, description: description, date: date)
                        self.posts += [post]
                        self.tableView.reloadData()
                    }
                    
                }
            }
        })
    }
    
    // MARK: - Table view data source
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return posts.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = "HomeTableViewCell"
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! HomeTableViewCell
        
        
        // MARK: - Fetch labels and insert here
        // find record for current cell
        let thisRecord : Post  = self.posts[indexPath.row]
        cell.titleLabel.text = thisRecord.title
        
        return cell
    }
}
