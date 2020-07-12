//
//  AccountEditController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/14/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import CoreData

class AccountEditController: UIViewController, UITextFieldDelegate, UINavigationControllerDelegate {

    private static let secQuestCell = "SecQuestCell"
    
    @IBOutlet weak var AccountNameTxt: UITextField!
    @IBOutlet weak var UserNametxt: UITextField!
    @IBOutlet weak var paswordtxt: UITextField!
    @IBOutlet weak var activeSwitch: UISwitch!
    
    
    @IBOutlet weak var saveBtn: UIBarButtonItem!
    @IBOutlet weak var cancleBtn: UIBarButtonItem!
    
    internal var accountDetails: AccountDetails?
    internal var newAccount: Bool = true
    internal var AccountID: Int = -1

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if !newAccount {
            configureView()
        } else {
            navigationItem.title = "Add Account"
        }
        
        updateSaveButtonState()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - UITextFieldDelegate
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        // Disable the Save button while editing.
        saveBtn.isEnabled = false
        updateSaveButtonState()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        updateSaveButtonState()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if( textField.tag == 1) {
            textField.resignFirstResponder()
            UserNametxt.becomeFirstResponder()
        } else if ( textField.tag == 2) {
            textField.resignFirstResponder()
            paswordtxt.becomeFirstResponder()
        } else if ( textField.tag == 3) {
            textField.resignFirstResponder()
        }
        return true;
    }
    
    @IBAction func unwindAccountFunc(sender: UIStoryboardSegue) {
        updateSaveButtonState()
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        
        if (segue.identifier == Keys.SegueKeyViewSecQuestList){
            let secQuestListVC = segue.destination as! SecQuestListController
            secQuestListVC.AccountID = AccountID
            
        } else {
            guard let button = sender as? UIBarButtonItem, button === saveBtn else {
                return
            }
            saveTap()
        }
    }
    
    
    // MARK: Private Methods
    
    func saveTap() {
        addAcctHistory()
        
        let account = AccountDetails()
        account.AccountID = AccountID
        account.Name = AccountNameTxt.text
        account.UserName = UserNametxt.text
        account.Password = paswordtxt.text
        account.active = activeSwitch.isOn
        if(newAccount){
            account.CreateDate = Date()
        }
        account.ModifiedDate = Date()
        
        AccountDetails.saveAcct(detail: account)
        
    }
    
    private func addAcctHistory() {
        let histDetails = HistoryDetails()
        
        let account = AccountDetails()
        account.AccountID = AccountID
        account.Name = AccountNameTxt.text
        account.UserName = UserNametxt.text
        account.Password = paswordtxt.text
        account.active = activeSwitch.isOn
        
        if (!newAccount) {
            if(accountDetails?.Name != account.Name) {
                histDetails.DateModified = Date()
                histDetails.AccountID = AccountID
                histDetails.HistoryType = "Account Updated"
                histDetails.Info = "Account Name Updated"
                HistoryDetails.saveHistory(detail: histDetails)
            }
            if(accountDetails?.UserName != account.UserName) {
                histDetails.DateModified = Date()
                histDetails.AccountID = AccountID
                histDetails.HistoryType = "Account Updated"
                histDetails.Info = "Account UserName Updated"
                HistoryDetails.saveHistory(detail: histDetails)
            }
            if(accountDetails?.Password != account.Password) {
                histDetails.DateModified = Date()
                histDetails.AccountID = AccountID
                histDetails.HistoryType = "Account Updated"
                histDetails.Info = "Account Password Updated"
                HistoryDetails.saveHistory(detail: histDetails)
            }
            if(accountDetails?.active != account.active) {
                if(account.active) {
                    histDetails.DateModified = Date()
                    histDetails.AccountID = AccountID
                    histDetails.HistoryType = "Account Activated"
                    histDetails.Info = "Account Activated"
                    HistoryDetails.saveHistory(detail: histDetails)
                } else {
                    histDetails.DateModified = Date()
                    histDetails.AccountID = AccountID
                    histDetails.HistoryType = "Account De-Activate"
                    histDetails.Info = "Account De-Activate"
                    HistoryDetails.saveHistory(detail: histDetails)
                }
            }
        } else {
            histDetails.DateModified = Date()
            histDetails.AccountID = AccountID
            histDetails.HistoryType = "Account Added"
            histDetails.Info = "Account Added"
            HistoryDetails.saveHistory(detail: histDetails)
        }
    }
    
    private func updateSaveButtonState() {
        // Disable the Save button if the text field is empty.
        
        if (AccountNameTxt.text?.isEmpty)! || (UserNametxt.text?.isEmpty)! || (paswordtxt.text?.isEmpty)! {
            saveBtn.isEnabled = false
        } else {
            saveBtn.isEnabled = true
        }
    }
    
    
    
    func configureView(){
        if let accountName = self.AccountNameTxt{
            accountName.text = accountDetails?.Name
        }
        if let username = self.UserNametxt{
            username.text = accountDetails?.UserName
        }
        if let password = self.paswordtxt{
            password.text = accountDetails?.Password
        }
        if let active = self.activeSwitch{
            active.isOn = (accountDetails?.active)!
        }
    }
}
