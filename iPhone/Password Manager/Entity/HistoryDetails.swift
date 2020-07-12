//
//  HistoryDetails.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import Foundation
import UIKit
import CoreData

class HistoryDetails
{
    internal var HistoryID: Int!
    internal var AccountID: Int!
    internal var Info: String!
    internal var HistoryType: String!
    internal var DateModified: Date!
    
    static func getLatHistID(accountID: Int) -> Int{
        var histID = 0
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request =  NSFetchRequest<NSFetchRequestResult>(entityName: Keys.HistoryEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.AccountIDKey , accountID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                if accountID == (object as AnyObject).value(forKey: Keys.AccountIDKey) as! Int {
                    histID = (object as AnyObject).value(forKey: Keys.HistoryIDKey) as! Int
                } else {
                    histID = 0
                }
            }
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
        return histID
    }
    
    static func saveHistory(detail: HistoryDetails){
        if(detail.HistoryID == nil || detail.HistoryID < 0) {
            detail.HistoryID = getLatHistID(accountID: detail.AccountID) + 1
        }
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.HistoryEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.HistoryIDKey , detail.HistoryID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            var theLine: NSManagedObject! = objects.first as? NSManagedObject
            if theLine == nil{
                theLine = NSEntityDescription.insertNewObject( forEntityName: Keys.HistoryEntityKey, into: context) as NSManagedObject
            }
            
            theLine.setValue(detail.AccountID, forKey: Keys.AccountIDKey)
            theLine.setValue(detail.HistoryID, forKey: Keys.HistoryIDKey)
            theLine.setValue(detail.HistoryType, forKey: Keys.TypeKey)
            theLine.setValue(detail.Info, forKey: Keys.InfoKey)
            theLine.setValue(detail.DateModified, forKey: Keys.DateModifiedKey)
            
        } catch{
            print("There was an error in executeFetchRequest(): \(error)")
        }
        
        appDelegate.saveContext()
    }
}
