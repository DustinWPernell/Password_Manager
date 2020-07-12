//
//  HistoryController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData

class HistoryController: UITableViewController {
    
    private static let historyCell = "HistoryCell"

    internal var accountID: Int! = -1
    private var historyList = [HistoryDetails]()

    override func viewDidLoad() {
        super.viewDidLoad()

        loadHistory()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return historyList.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HistoryController.historyCell, for: indexPath)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .medium
        
        cell.textLabel?.text = historyList[indexPath.row].HistoryType
        cell.detailTextLabel?.text = dateFormatter.string(from: historyList[indexPath.row].DateModified)
        
        return cell
    }

    @IBAction func unwindHistoryFunc(sender: UIStoryboardSegue) {
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == Keys.SegueKeyHistDetails){
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            let histDetailsVC = segue.destination as! HistoryDetailsController
            histDetailsVC.histDetails = historyList[indexPath.row]
        }
    }
    
    // MARK: - Private
    
    func loadHistory(){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request =  NSFetchRequest<NSFetchRequestResult>(entityName: Keys.HistoryEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.AccountIDKey , accountID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                let historyDetails = HistoryDetails()
                historyDetails.AccountID = (object as AnyObject).value(forKey: Keys.AccountIDKey)! as! Int
                historyDetails.DateModified = (object as AnyObject).value(forKey: Keys.DateModifiedKey) as! Date
                historyDetails.HistoryID = (object as AnyObject).value(forKey: Keys.HistoryIDKey) as! Int
                historyDetails.HistoryType = (object as AnyObject).value(forKey: Keys.TypeKey) as? String ?? ""
                historyDetails.Info = (object as AnyObject).value(forKey: Keys.InfoKey) as? String ?? ""
                
                historyList.append(historyDetails)
            }
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
    }
}
