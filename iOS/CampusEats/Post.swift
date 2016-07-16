//
//  Post.swift
//  CampusEats
//
//  Created by Shawn Kim on 7/16/16.
//  Copyright © 2016 FFOC. All rights reserved.
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