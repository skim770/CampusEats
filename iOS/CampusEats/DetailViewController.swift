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
    @IBOutlet weak var authorLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var coverImageView: UIImageView!
    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!

    var post: Post!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        descriptionLabel.numberOfLines = 0
        summaryLabel.numberOfLines = 0
        self.hideKeyboardWhenTappedAround()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let formatter = DateFormatter()
        formatter.dateFormat = DATE_FORMAT
        
        formatter.dateFormat = CELL_TIME_FORMAT
        timeLabel.text = formatter.string(from: post.start)
        formatter.dateFormat = CELL_DATE_FORMAT
        dateLabel.text = formatter.string(from: post.start)
        locationLabel.text = post.location
        coverImageView.sd_setImage(with: URL(string: post.imageLocation), placeholderImage:UIImage(named:"placeholder")!)
        
        titleLabel.text = post.title
        summaryLabel.text = post.description
        descriptionLabel.text = parseHTML(htmlString: post.body)
        authorLabel.text = post.author
    }
    
    func parseHTML(htmlString: String) -> String {
        return htmlString.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression, range: nil)
    }
}
