//
//  AuthenticationTextField.swift
//  CampusEats
//
//  Created by Shawn Kim on 12/2/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit

@IBDesignable
class AuthenticationTextField: UITextField {
    override func layoutSubviews() {
        super.layoutSubviews()
        
        self.layer.borderColor = UIColor(white: 1, alpha: 0.3).cgColor
        self.layer.borderWidth = 2
        self.layer.cornerRadius = 5
        
    }
    
    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.insetBy(dx: 8, dy: 7)
    }
    
    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return textRect(forBounds: bounds)
    }
}
