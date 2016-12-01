//
//  DetailViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 12/1/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var summaryLabel: UILabel!
    
    var post: Post!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.hideKeyboardWhenTappedAround()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        titleLabel.text = post.title
        summaryLabel.text = post.description
    }
}
