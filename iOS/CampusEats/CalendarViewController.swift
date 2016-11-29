//
//  CalendarViewController.swift
//  CampusEats
//
//  Created by Shawn Kim on 11/23/16.
//  Copyright © 2016 campuseats. All rights reserved.
//

import UIKit
import Firebase
import JTAppleCalendar

class CalendarViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var postsTableView: UITableView!
    @IBOutlet weak var calendarView: JTAppleCalendarView!
    
    var posts = [Post]()
    var allPosts = [String: [Post]]()
    var ref:FIRDatabaseReference!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Firebase
        ref = FIRDatabase.database().reference()
//        populatePosts()
        
        //Posts
        postsTableView.delegate = self
        postsTableView.dataSource = self
        
        //Calendar
        calendarView.delegate = self
        calendarView.dataSource = self
        calendarView.registerCellViewXib(file: "CellView")
        calendarView.registerHeaderView(xibFileNames: ["MonthHeaderView"])
        populatePosts()
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let date = Date()
        filterPosts(selectedDate: formatter.string(from: date), clear: false)
        
        initializeCalendar()
    }
    
    func filterPosts(selectedDate: String, clear: Bool) {
        posts.removeAll()
        if !clear {
            guard let selectedPosts = allPosts[selectedDate] else {
                self.postsTableView.reloadData()
                return
            }
            for post in selectedPosts {
                posts += [post]
            }
            self.postsTableView.reloadData()        }
    }
    
    func populatePosts() {
        let formatter = DateFormatter()
        formatter.dateFormat = DATE_FORMAT
        
        let activePosts = ref.child("posts").queryOrdered(byChild: "date")
        activePosts.observe(FIRDataEventType.value, with: { snapshot in
            if (snapshot.childrenCount > 0) {
                let enumerator = snapshot.children
                while let item = enumerator.nextObject() as? FIRDataSnapshot {
                    guard let value = item.value as? NSDictionary else {
                        continue
                    }
                    let title = value["title"] as! String
                    let description = value["body"] as! String
                    
                    for date in value["times"] as! [NSDictionary] {
                        let start = date["start"] as! String
                        let end = date["end"] as! String
                        let post = Post(title: title, description: description, start: start, end: end)
                        let index = end.index(end.startIndex, offsetBy: 10)
                        let dateKey = end.substring(to: index)
                        if self.allPosts[dateKey] != nil {
                            self.allPosts[dateKey]! += [post]
                        }
                        else {
                            self.allPosts[dateKey] = [post]
                        }
                    }
                }
            }
        })
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return posts.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CalendarTableViewCell", for: indexPath) as! CalendarTableViewCell
        
        // Configure the cell...
        let thisRecord : Post  = self.posts[indexPath.row]
        cell.titleLabel.text = thisRecord.title
        
        return cell
    }
}

// Calendar
extension CalendarViewController: JTAppleCalendarViewDataSource, JTAppleCalendarViewDelegate {
    func initializeCalendar() {
        let date = Date()
        calendarView.scrollToDate(date, animateScroll: false)
        calendarView.selectDates([date])
    }
    
    func configureCalendar(_ calendar: JTAppleCalendarView) -> ConfigurationParameters {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy MM dd"
        
        let startDate = formatter.date(from: "2016 02 01")! // You can use date generated from a formatter
        let endDate = Date()                                // You can also use dates created from this function
        let parameters = ConfigurationParameters(
            startDate: startDate,
            endDate: endDate,
            numberOfRows: 6, // Only 1, 2, 3, & 6 are allowed
            calendar: Calendar.current,
            generateInDates: .forAllMonths,
            generateOutDates: .tillEndOfGrid,
            firstDayOfWeek: .sunday)
        return parameters
    }
    
    func calendar(_ calendar: JTAppleCalendarView, willDisplayCell cell: JTAppleDayCellView, date: Date, cellState: CellState) {
        let myCustomCell = cell as! CellView
        
        // Setup Cell text
        myCustomCell.dayLabel.text = cellState.text
        
        // Setup text color
        if cellState.dateBelongsTo == .thisMonth {
            myCustomCell.dayLabel.textColor = UIColor.black
            myCustomCell.isUserInteractionEnabled = true
        } else {
            myCustomCell.dayLabel.textColor = UIColor.gray
            myCustomCell.isUserInteractionEnabled = false
            myCustomCell.selectedView.isHidden = true
        }
        
        handleCellSelection(view: cell, cellState: cellState, update: false)
    }
    
    func calendar(_ calendar: JTAppleCalendarView, didSelectDate date: Date, cell: JTAppleDayCellView?, cellState: CellState) {
        handleCellSelection(view: cell, cellState: cellState, update: true)
    }
    
    func calendar(_ calendar: JTAppleCalendarView, didDeselectDate date: Date, cell: JTAppleDayCellView?, cellState: CellState) {
        handleCellSelection(view: cell, cellState: cellState, update: true)
    }
    
    func handleCellSelection(view: JTAppleDayCellView?, cellState: CellState, update: Bool) {
        guard let myCustomCell = view as? CellView  else {
            return
        }
        if cellState.isSelected {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            if (calendarView.selectedDates.count > 0) {
                let selectedDate = formatter.string(from: calendarView.selectedDates[0])
                print(selectedDate)
                filterPosts(selectedDate: selectedDate, clear: false)
            }
            
            myCustomCell.selectedView.layer.cornerRadius =  22.5
            myCustomCell.selectedView.isHidden = false
            myCustomCell.dayLabel.textColor = UIColor.white
        } else {
            filterPosts(selectedDate: "", clear: true)
            myCustomCell.selectedView.isHidden = true
            if (update) {
                myCustomCell.dayLabel.textColor = UIColor.black
            }
        }
    }
    
    // This sets the height of your header
    func calendar(_ calendar: JTAppleCalendarView, sectionHeaderSizeFor range: (start: Date, end: Date), belongingTo month: Int) -> CGSize {
        let bounds = UIScreen.main.bounds
        return CGSize(width: bounds.size.width, height: 80)
    }
    // This setups the display of your header
    func calendar(_ calendar: JTAppleCalendarView, willDisplaySectionHeader header: JTAppleHeaderView, range: (start: Date, end: Date), identifier: String) {
        let headerCell = (header as? MonthHeaderView)
        headerCell?.title.text = range.start.getMonth()
    }
}
