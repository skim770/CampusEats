//
//  Post.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import Foundation

class Post {
    var title: String
    var description: String
    var date: String
    
    init() {
        title = ""
        description = ""
        date = ""
    }
    
    init(title: String, description: String, date: String) {
        self.title = title
        self.description = description
        self.date = date
    }
}
