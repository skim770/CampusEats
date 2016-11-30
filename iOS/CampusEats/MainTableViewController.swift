//
//  MainTableViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase

class MainTableViewController: UITableViewController {
    var posts = [Post]()
    var ref:FIRDatabaseReference!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        ref = FIRDatabase.database().reference()
        populatePosts()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    func populatePosts() {
        let activePosts = ref.child("posts").queryOrdered(byChild: "date")
        let currentDate = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = DATE_FORMAT
        
        activePosts.observe(FIRDataEventType.value, with: { snapshot in
            self.posts.removeAll()
            if (snapshot.childrenCount > 0) {
                let enumerator = snapshot.children
                while let item = enumerator.nextObject() as? FIRDataSnapshot {
                    guard let value = item.value as? NSDictionary else {
                        continue
                    }
                    
                    let title = value["title"] as! String
                    let description = value["body"] as! String
                    
                    for date in value["times"] as! [NSDictionary]{
                        let start = date["start"] as! String
                        let end = date["end"] as! String
                        let thisDate = formatter.date(from: end)
                        guard let isGreater = thisDate?.isGreaterThanDate(dateToCompare: currentDate) else {
                            continue
                        }
                        if isGreater {
                            let post = Post(title: title, description: description, start: start, end: end)
                            self.posts += [post]
                            self.tableView.reloadData()
                        }
                    }
                }
            }
        })
    }
    @IBAction func PostDidTapped(_ sender: Any) {
        performSegue(withIdentifier: "PostFromMain", sender: nil)
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return posts.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TableViewCell", for: indexPath) as! TableViewCell

        // Configure the cell...
        let thisRecord : Post  = self.posts[indexPath.row]
        cell.titleLabel.text = thisRecord.title
        cell.descriptionLabel.text = thisRecord.description
        
        return cell
    }

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
