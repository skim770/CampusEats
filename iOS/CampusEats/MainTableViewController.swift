//
//  MainTableViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase
import SDWebImage

class MainTableViewController: UITableViewController {
    var posts = [Post]()
    var ref:FIRDatabaseReference!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        ref = FIRDatabase.database().reference()
        populatePosts()
    }
    
    func populatePosts() {
        let activePosts = ref.child("posts").queryOrdered(byChild: "start")
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
                    let description = value["summary"] as! String
                    let author = value["author"] as! String
                    let location = value["location"] as! String
                    let imageLocation = value["image"] as! String
                    let body = value["body"] as! String
                    
                    for date in value["times"] as! [NSDictionary]{
                        let start = formatter.date(from: date["start"] as! String)
                        let end = date["end"] as! String
                        let thisDate = formatter.date(from: end)
                        guard let isGreater = thisDate?.isGreaterThanDate(dateToCompare: currentDate) else {
                            continue
                        }
                        if isGreater {
                            let post = Post(
                                title: title,
                                description: description,
                                start: start!,
                                end: end,
                                author: author,
                                location: location,
                                imageLocation: imageLocation,
                                body: body)
                            self.posts += [post]
                            self.tableView.reloadData()
                        }
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "TableViewCell", for: indexPath) as! TableViewCell
        
        let formatter = DateFormatter()
        // Configure the cell...
        let thisRecord : Post  = self.posts[indexPath.row]
        cell.titleLabel.text = thisRecord.title
        cell.descriptionLabel.text = thisRecord.description
        cell.posterLabel.text = thisRecord.author
        
        formatter.dateFormat = CELL_TIME_FORMAT
        cell.timeLabel.text = formatter.string(from: thisRecord.start)
        formatter.dateFormat = CELL_DATE_FORMAT
        cell.dateLabel.text = formatter.string(from: thisRecord.start)
        cell.locationLabel.text = thisRecord.location
        cell.coverImageView.sd_setImage(with: URL(string: thisRecord.imageLocation), placeholderImage:UIImage(named:"placeholder")!)
        
        return cell
    }
    
    // MARK: - Navigation
    @IBAction func PostDidTapped(_ sender: Any) {
        performSegue(withIdentifier: "PostFromMain", sender: nil)
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRow(at: indexPath as IndexPath, animated: true)
        performSegue(withIdentifier: "showDetailMain", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showDetailMain",
            let destination = segue.destination as? DetailViewController,
            let postIndex = tableView.indexPathForSelectedRow?.row{
            destination.post = posts[postIndex]
        }
    }
}
