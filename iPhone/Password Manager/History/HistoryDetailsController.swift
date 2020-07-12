//
//  HistoryDetailsController.swift
//  Password Manager
//
//  Created by Dustin Pernell on 4/15/18.
//  Copyright © 2018 WakeTech. All rights reserved.
//

import UIKit

class HistoryDetailsController: UIViewController {

    @IBOutlet weak var typelbl: UILabel!
    @IBOutlet weak var infolbl: UILabel!
    @IBOutlet weak var datelbl: UILabel!
    
    internal var histDetails: HistoryDetails!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .medium

        typelbl.text = histDetails.HistoryType
        infolbl.text = histDetails.Info
        datelbl.text = dateFormatter.string(from: histDetails.DateModified)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
