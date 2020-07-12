//
//  SecQuestDetails.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import Foundation
import UIKit
import CoreData

class SecQuestDetails
{
    internal var QuestionID: Int!
    internal var AccountID: Int!
    internal var Question: String!
    internal var Answer: String!
    internal var DateModified: Date!
    internal var Active: Bool!
    
    
    static func saveSecQuest(detail: SecQuestDetails){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.SecQuestEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.SecQuestIDKey , detail.QuestionID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            var theLine: NSManagedObject! = objects.first as? NSManagedObject
            if theLine == nil{
                theLine = NSEntityDescription.insertNewObject( forEntityName: Keys.SecQuestEntityKey, into: context) as NSManagedObject
            }
            
            theLine.setValue(detail.AccountID, forKey: Keys.AccountIDKey)
            theLine.setValue(detail.QuestionID, forKey: Keys.SecQuestIDKey)
            theLine.setValue(detail.Question, forKey: Keys.QuestionKey)
            theLine.setValue(detail.Answer, forKey: Keys.AnswerKey)
            theLine.setValue(detail.DateModified, forKey: Keys.DateModifiedKey)
            theLine.setValue(detail.Active, forKey: Keys.ActiveKey)

        } catch{
            print("There was an error in executeFetchRequest(): \(error)")
        }
        
        appDelegate.saveContext()
    }
}
