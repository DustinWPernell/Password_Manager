//
//  AccountViewController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData


class AccountViewController: UIViewController{

    
    

    @IBOutlet weak var AccountNameLbl: UILabel!
    @IBOutlet weak var UserNameLbl: UILabel!
    @IBOutlet weak var paswordLbl: UILabel!
    @IBOutlet weak var activeLbl: UILabel!
    @IBOutlet var secQuestTable: UITableView!
    
    
    internal var accountDetails: AccountDetails?
    internal var AccountID: Int = -1
    
    func configureView(){
        if let accountName = self.AccountNameLbl{
            accountName.text = accountDetails?.Name
        }
        if let username = self.UserNameLbl{
            username.text = accountDetails?.UserName
        }
        if let password = self.paswordLbl{
            password.text = accountDetails?.Password
        }
        if let active = self.activeLbl{
            if (accountDetails?.active)! {
                active.text = "Active"
            } else {
                active.text = "Inactive"
            }
        }
    }
    
    func reloadView(){
        accountDetails = AccountDetails()
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.AccountEntityKey)
        
        let pred = NSPredicate(format: "%K = %d", Keys.AccountIDKey , AccountID)
        request.predicate = pred
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                accountDetails?.AccountID = (object as AnyObject).value(forKey: Keys.AccountIDKey)! as! Int
                accountDetails?.Name = (object as AnyObject).value(forKey: Keys.NameKey) as? String ?? ""
                accountDetails?.UserName = (object as AnyObject).value(forKey: Keys.UserNameKey) as? String ?? ""
                accountDetails?.Password = (object as AnyObject).value(forKey: Keys.PasswordKey) as? String ?? ""
                accountDetails?.active = (object as AnyObject).value(forKey: Keys.ActiveKey) as? Bool ?? true
                accountDetails?.CreateDate = (object as AnyObject).value(forKey: Keys.DateAddedKey) as? Date
                accountDetails?.ModifiedDate = (object as AnyObject).value(forKey: Keys.DateModifiedKey) as? Date
            }
            
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
    
        configureView()
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureView()
    }
    
    @IBAction func unwindAccountFunc(sender: UIStoryboardSegue) {
        reloadView()
    }
    


    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.

        if(segue.identifier == Keys.SegueKeyAccntHist){
            let historyVC = segue.destination as! HistoryController
            historyVC.accountID = AccountID
            
        } else if (segue.identifier == Keys.SegueKeyViewSecQuestList){
            let secQuestListVC = segue.destination as! SecQuestListController
            secQuestListVC.AccountID = AccountID
            
        }  else if (segue.identifier == Keys.SegueKeyEditAccnt){
            let accountVC = segue.destination as! AccountEditController
            accountVC.newAccount = false
            accountVC.AccountID = AccountID
            accountVC.accountDetails = accountDetails
        }
    }
 

}
