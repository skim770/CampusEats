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
    var start: String
    var end: String
    
    init() {
        title = ""
        description = ""
        start = ""
        end = ""
    }
    
    init(title: String, description: String, start: String, end: String) {
        self.title = title
        self.description = description
        self.start = start
        self.end = end
    }
}
