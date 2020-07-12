//
//  SecQuestListController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/16/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData

class SecQuestListController: UITableViewController {
    
    private static let secQuestCell = "SecQuestCell"
    
    private var secQuestList = [SecQuestDetails]()
    internal var AccountID: Int = -1


    override func viewDidLoad() {
        super.viewDidLoad()

        loadSecQuest()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SecQuestListController.secQuestCell, for: indexPath)

        let secQuest = secQuestList[indexPath.row]
        
        cell.textLabel?.text = secQuest.Question
        cell.detailTextLabel?.text = secQuest.Answer
        
        return cell
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return secQuestList.count
    }

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == Keys.SegueKeyNewSecQuest){
            let secQuestListVC = segue.destination as! SecQuestEditController
            secQuestListVC.accountID = AccountID
            secQuestListVC.newQuest = true
            if(secQuestList.count > 0){
                secQuestListVC.questionID = (secQuestList.last?.QuestionID)! + 1
            } else {
                secQuestListVC.questionID = 0
            }
        } else if (segue.identifier == Keys.SegueKeyViewSecQuest){
            let secQuestVC = segue.destination as! SecQuestViewController
            secQuestVC.secDetails = secQuestList[(self.tableView.indexPathForSelectedRow?.row)!]
        }
    }
    
    @IBAction func unwindSecQuestFunc(sender: UIStoryboardSegue) {
        loadSecQuest()
        tableView.reloadData()
    }
    
    // MARK: - Private
    func loadSecQuest(){
        secQuestList = [SecQuestDetails]()
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.SecQuestEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.AccountIDKey , AccountID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                let secQuestDetails = SecQuestDetails()
                
                secQuestDetails.AccountID = (object as AnyObject).value(forKey: Keys.AccountIDKey)! as! Int
                secQuestDetails.Answer = (object as AnyObject).value(forKey: Keys.AnswerKey) as? String ?? ""
                secQuestDetails.Question = (object as AnyObject).value(forKey: Keys.QuestionKey) as? String ?? ""
                secQuestDetails.QuestionID = (object as AnyObject).value(forKey: Keys.SecQuestIDKey) as! Int
                secQuestDetails.DateModified = (object as AnyObject).value(forKey: Keys.DateModifiedKey) as! Date
                secQuestDetails.Active = (object as AnyObject).value(forKey: Keys.ActiveKey) as! Bool

                secQuestList.append(secQuestDetails)
            }
            
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
    }

}
