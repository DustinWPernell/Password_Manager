//
//  MainViewController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/17/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit
import LocalAuthentication
import CoreData

class MainViewController: UIViewController {
    @IBOutlet var loginBtn: UIButton!
        
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func unwindPasswordFunc(sender: UIStoryboardSegue) {
    }
    
    @IBAction func loginBtnTap(_ sender: Any) {
        authWithTouchID()
    }
    
    func authWithTouchID() {
        let context = LAContext()
        var error: NSError?
        
        // check if Touch ID is available
        if context.canEvaluatePolicy(.deviceOwnerAuthentication, error: &error) {
            let reason = "User your Apple login information."
            context.evaluatePolicy(.deviceOwnerAuthentication, localizedReason: reason)
                {(succes, error) in
                    if succes {
                        self.performSegue(withIdentifier: Keys.SegueKeyLogin, sender: self.loginBtn)
                    } else {
                        self.showAlertController("Login Failed")
                    }
                }
        }
    }
    
    
    func showAlertController(_ message: String) {
        let alertController = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        present(alertController, animated: true, completion: nil)
    }

    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
