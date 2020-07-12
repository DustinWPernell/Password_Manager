//
//  SecQuestViewController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData
import CoreText

class SecQuestViewController: UIViewController {

    internal var secDetails: SecQuestDetails! = nil
    
    @IBOutlet weak var secQuestlbl: UILabel!
    @IBOutlet weak var secAnswerlbl: UILabel!
    @IBOutlet weak var activelbl: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        configureView()
        
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == Keys.SegueKeyEditSecQuest){
            let secQuestVC = segue.destination as! SecQuestEditController
            secQuestVC.secDetails = secDetails
            secQuestVC.newQuest = false
            secQuestVC.questionID = secDetails.QuestionID
            secQuestVC.accountID = secDetails.AccountID
        }
    }
    

    
    @IBAction func unwindSecQuestFunc(sender: UIStoryboardSegue) {
        loadSecQuest()
        configureView()
    }

    // MARK: - Private
    
    func configureView(){
        if let question = self.secQuestlbl{
            question.text = secDetails?.Question

            question.textAlignment = .natural
            question.numberOfLines = 0
            question.sizeToFit()
        }
        if let answer = self.secAnswerlbl{
            answer.text = secDetails?.Answer
            
            answer.textAlignment = .natural
            answer.numberOfLines = 0
            answer.sizeToFit()
        }
        if let active = self.activelbl{
            if (secDetails?.Active)! {
                active.text = "Active"
            } else {
                active.text = "Inactive"
            }
        }
    }
    
    func loadSecQuest(){
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.SecQuestEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.SecQuestIDKey , secDetails.QuestionID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                secDetails.AccountID = (object as AnyObject).value(forKey: Keys.AccountIDKey)! as! Int
                secDetails.Answer = (object as AnyObject).value(forKey: Keys.AnswerKey) as? String ?? ""
                secDetails.Question = (object as AnyObject).value(forKey: Keys.QuestionKey) as? String ?? ""
                secDetails.DateModified = (object as AnyObject).value(forKey: Keys.DateModifiedKey) as! Date
                secDetails.Active = (object as AnyObject).value(forKey: Keys.ActiveKey) as! Bool
            }
            
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
    }
}
