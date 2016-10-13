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
    
    @IBAction func NewPostDidTapped(_ sender: AnyObject) {
        performSegue(withIdentifier: "CreatePost", sender: nil)
    }
    
    func RefreshDidTapped() {
        posts = [Post]()
        populatePosts()
    }
    
    @IBAction func LogoutDidTapped(_ sender: AnyObject) {
        let firebaseAuth = FIRAuth.auth()
        do {
            try firebaseAuth?.signOut()
            dismiss(animated: true, completion: nil)
        } catch let signOutError as NSError {
            print ("Error signing out: %@", signOutError)
        }
    }
    
    func populatePosts(){
        let activePosts = ref.child("posts").queryOrdered(byChild: "date")
        activePosts.observeSingleEvent(of: FIRDataEventType.value, with: {(snapshot) in
            if (snapshot.childrenCount > 0){
                for child in (snapshot.value as! NSDictionary) {
                    let item = child.value as? NSDictionary
                    if item?["status"] as! String == "Active"{
                        let title = item?["title"] as! String
                        print(title)
                        let description = item?["desc"] as! String
                        print(description)
                        let date = item?["date"] as! String
                        let post = Post(title: title, description: description, date: date)
                        self.posts += [post]
                        self.tableView.reloadData()
                    }
                    
                }
            }
        })
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return posts.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "HomeTableViewCell"
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as! HomeTableViewCell
        
        
        // MARK: - Fetch labels and insert here
        // find record for current cell
        let thisRecord : Post  = self.posts[(indexPath as NSIndexPath).row]
        cell.titleLabel.text = thisRecord.title
        cell.descriptionLabel.text = thisRecord.description
        
        return cell
    }
}
