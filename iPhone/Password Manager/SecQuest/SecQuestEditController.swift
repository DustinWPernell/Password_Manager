//
//  SecQuestEditController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/14/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit

class SecQuestEditController: UIViewController, UITextFieldDelegate, UINavigationControllerDelegate {

    internal var accountID: Int! = -1
    internal var secDetails: SecQuestDetails! = nil
    internal var questionID: Int! = -1
    internal var newQuest: Bool! = true
    
    @IBOutlet weak var secQuesttxt: UITextField!
    @IBOutlet weak var secAnswertxt: UITextField!
    @IBOutlet weak var activeSwitch: UISwitch!

    
    @IBOutlet weak var saveBtn: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if !newQuest {
            configureView()
        } else {
            navigationItem.title = "Add Security Question"
            activeSwitch.isOn = true
        }
        
        updateSaveButtonState()
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
            secAnswertxt.becomeFirstResponder()
        } else if ( textField.tag == 2) {
            textField.resignFirstResponder()
        }
        return true;
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        // Get the new view controller using segue.destinationViewController.

        guard let button = sender as? UIBarButtonItem, button === saveBtn else {
            return
        }
        saveTap()
    }

    
    // MARK: - Private
    
    private func addAcctHistory() {
        let histDetails = HistoryDetails()
        
        let secQuest = SecQuestDetails()
        secQuest.AccountID = accountID
        secQuest.Answer = secAnswertxt.text
        secQuest.Question = secQuesttxt.text
        secQuest.QuestionID = questionID
        secQuest.Active = activeSwitch.isOn
        
        if (!newQuest) {
            if(secDetails?.Question != secQuest.Question) {
                histDetails.DateModified = Date()
                histDetails.AccountID = accountID
                histDetails.HistoryType = "Security Question Updated"
                histDetails.Info = "Security Question Updated"
                HistoryDetails.saveHistory(detail: histDetails)
            }
            if(secDetails?.Answer != secQuest.Answer) {
                histDetails.DateModified = Date()
                histDetails.AccountID = accountID
                histDetails.HistoryType = "Security Question Updated"
                histDetails.Info = "Security Question Answer Updated"
                HistoryDetails.saveHistory(detail: histDetails)
            }
            if(secDetails?.Active != secQuest.Active) {
                if(secQuest.Active) {
                    histDetails.DateModified = Date()
                    histDetails.AccountID = accountID
                    histDetails.HistoryType = "Security Question Activated"
                    histDetails.Info = "Security Question Activated"
                    HistoryDetails.saveHistory(detail: histDetails)
                } else {
                    histDetails.DateModified = Date()
                    histDetails.AccountID = accountID
                    histDetails.HistoryType = "Security Question De-Activate"
                    histDetails.Info = "Security Question De-Activate"
                    HistoryDetails.saveHistory(detail: histDetails)
                }
            }
        } else {
            histDetails.DateModified = Date()
            histDetails.AccountID = accountID
            histDetails.HistoryType = "Security Question Added"
            histDetails.Info = "Security Question Added"
            HistoryDetails.saveHistory(detail: histDetails)
        }
    }
    
    func configureView(){
        if let question = self.secQuesttxt{
            question.text = secDetails?.Question
        }
        if let answer = self.secAnswertxt{
            answer.text = secDetails?.Answer
        }
        if let active = self.activeSwitch{
            active.isOn = (secDetails?.Active)!
        }
    }
    
    func saveTap() {
        let secQuest = SecQuestDetails()
        secQuest.AccountID = accountID
        secQuest.QuestionID = questionID
        secQuest.Answer = secAnswertxt.text
        secQuest.Question = secQuesttxt.text
        secQuest.DateModified = Date()
        secQuest.Active = activeSwitch.isOn

        addAcctHistory()
        SecQuestDetails.saveSecQuest(detail: secQuest)
    }
    
    private func updateSaveButtonState() {
        if (secAnswertxt.text?.isEmpty)! || (secQuesttxt.text?.isEmpty)! {
            saveBtn.isEnabled = false
        } else {
            saveBtn.isEnabled = true
        }
    }

}
