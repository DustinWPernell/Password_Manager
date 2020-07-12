//
//  AccountDetails.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import Foundation
import UIKit
import CoreData

class AccountDetails
{
    internal var AccountID: Int!
    internal var Name: String!
    internal var UserName: String!
    internal var Password: String!
    internal var active: Bool!
    internal var CreateDate: Date!
    internal var ModifiedDate: Date!
    
    
    static func saveAcct(detail: AccountDetails){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.AccountEntityKey)
        let pred = NSPredicate(format: "%K = %d", Keys.AccountIDKey , detail.AccountID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            var theLine: NSManagedObject! = objects.first as? NSManagedObject
            if theLine == nil{
                theLine = NSEntityDescription.insertNewObject( forEntityName: Keys.AccountEntityKey, into: context) as NSManagedObject
            }
            
            theLine.setValue(detail.AccountID, forKey: Keys.AccountIDKey)
            theLine.setValue(detail.Name, forKey: Keys.NameKey)
            theLine.setValue(detail.UserName, forKey: Keys.UserNameKey)
            theLine.setValue(detail.Password, forKey: Keys.PasswordKey)
            theLine.setValue(detail.active, forKey: Keys.ActiveKey)
            theLine.setValue(detail.CreateDate, forKey: Keys.DateAddedKey)
            theLine.setValue(detail.ModifiedDate, forKey: Keys.DateModifiedKey)
            
        } catch{
            print("There was an error in executeFetchRequest(): \(error)")
        }
        
        appDelegate.saveContext()
    }
    
}
