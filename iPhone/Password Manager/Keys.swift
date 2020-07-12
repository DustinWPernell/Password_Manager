//
//  Keys.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/9/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import Foundation

class Keys
{
    internal static let UserSettingsKey = "UserSettings"
    internal static let LoginKey = "password"
    internal static let SegueKeyPWLogin = "PWLogin"
    internal static let SegueKeyLogout = "Logout"
    
    internal static let AccountEntityKey = "Account"
    internal static let AccountIDKey = "accountID"
    internal static let NameKey = "name"
    internal static let UserNameKey = "userName"
    internal static let PasswordKey = "password"
    internal static let ActiveKey = "active"
    internal static let DateAddedKey = "dateAdded"
    internal static let DateModifiedKey = "dateModified"
    
    internal static let SecQuestEntityKey = "SecQuest"
    internal static let SecQuestIDKey = "questionID"
    internal static let QuestionKey = "question"
    internal static let AnswerKey = "answer"

    
    internal static let HistoryEntityKey = "History"
    internal static let HistoryIDKey = "historyID"
    internal static let InfoKey = "info"
    internal static let TypeKey = "type"
    
    
    internal static let SegueKeyLogin = "PerformLogin"
    internal static let SegueKeyNewPW = "SetNewPassword"
    internal static let SegueKeyNewSecQuest = "NewSecQuest"
    internal static let SegueKeyEditSecQuest = "EditSecQuest"
    internal static let SegueKeyViewSecQuest = "ViewSecQuest"
    internal static let SegueKeySaveSecQuest = "SaveSecQuest"
    internal static let SegueKeyViewSecQuestList = "ViewSecQuestList"
    internal static let SegueKeyNewAccnt = "NewAccount"  
    internal static let SegueKeyAccntLoad = "AccntLoad"
    internal static let SegueKeyEditAccnt = "AccountEdit"
    internal static let SegueKeyAccntHist = "AccntHistory"
    internal static let SegueKeyEditCancel = "EditCancel"
    internal static let SegueKeySaveAccount = "AccountSave"
    internal static let SegueKeyViewDone = "ViewDone"
    internal static let SegueKeyHistDetails = "HistDetailsView"
}
