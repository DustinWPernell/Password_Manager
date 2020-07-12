//
//  AccountListController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData

class AccountListController: UITableViewController {

    var accountViewController: AccountViewController? = nil
    
    private var accountList = [AccountDetails]()
    
    private static let accountCell = "AccountCell"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        loadData()
            
    }
    
    func loadData(){
        accountList = [AccountDetails]()
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.managedObjectContext
        let request: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: Keys.AccountEntityKey)
        
        
        do{
            let objects = try context.fetch(request)
            for object in objects {
                let accountDetails = AccountDetails()
                accountDetails.AccountID = (object as AnyObject).value(forKey: Keys.AccountIDKey)! as! Int
                accountDetails.Name = (object as AnyObject).value(forKey: Keys.NameKey) as? String ?? ""
                accountDetails.UserName = (object as AnyObject).value(forKey: Keys.UserNameKey) as? String ?? ""
                accountDetails.Password = (object as AnyObject).value(forKey: Keys.PasswordKey) as? String ?? ""
                accountDetails.active = (object as AnyObject).value(forKey: Keys.ActiveKey) as? Bool ?? true
                accountDetails.CreateDate = (object as AnyObject).value(forKey: Keys.DateAddedKey) as? Date
                accountDetails.ModifiedDate = (object as AnyObject).value(forKey: Keys.DateModifiedKey) as? Date
                
                accountList.append(accountDetails)
            }
        
        } catch {
            print("There was an error in executeFetchRequest(): \(error)")
        }
        
        tableView.reloadData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func unwindAccountFunc(sender: UIStoryboardSegue) {
        loadData()
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return accountList.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AccountListController.accountCell, for: indexPath)

        cell.textLabel?.text = accountList[indexPath.row].Name

        return cell
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
        if (segue.identifier == Keys.SegueKeyLogout) {
            
        } else if(segue.identifier != Keys.SegueKeyNewAccnt)
        {
            let accountVC = segue.destination as! AccountViewController
            accountVC.accountDetails = accountList[(self.tableView.indexPathForSelectedRow?.row)!]
            accountVC.AccountID = accountList[(self.tableView.indexPathForSelectedRow?.row)!].AccountID
        } else
        {
            let accountVC = segue.destination as! AccountEditController
            accountVC.newAccount = true
            if(accountList.count > 0){
                accountVC.AccountID = (accountList.last?.AccountID)! + 1
            } else {
                accountVC.AccountID = 0
            }
        }
    
    }
}
