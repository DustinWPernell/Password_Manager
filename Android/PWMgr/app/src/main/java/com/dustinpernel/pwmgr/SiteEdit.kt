package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText


import java.util.ArrayList
import java.util.Date

/**
 * Created by dustinpernell on 4/19/17.
 */

class SiteEdit : Activity(), View.OnClickListener {
    //Site_Edit (SE)
    private var siteNameSE: EditText? = null
    private var passwordSE: EditText? = null
    private var addSeqQuestSE: Button? = null
    private var saveSE: Button? = null
    private var cancelSE: Button? = null
    private var secQuestListSE: ArrayList<SecQuest>? = null

    private var result: Intent? = null

    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.site_edit)
        Log.d("LOAD", "SiteE Loaded")

        val intent = getIntent()


        siteNameSE = findViewById(R.id.editSiteSE) as EditText
        passwordSE = findViewById(R.id.editPWSE) as EditText
        addSeqQuestSE = findViewById(R.id.AddSecQuestBNuttonSE) as Button
        saveSE = findViewById(R.id.SaveButtonSE) as Button
        cancelSE = findViewById(R.id.CancelButtonSE) as Button

        siteNameSE!!.setText(savedInstanceState.get("siteName").toString())
        passwordSE!!.setText(savedInstanceState.get("password").toString())

        secQuestListSE = ArrayList()

        var count = 1
        while (intent.getExtras().get("secQuest$count") != null) {
            val t = SecQuest(intent.getExtras().get("secQuest$count") as ByteArray,
                    intent.getExtras().get("secAnswr$count") as ByteArray)
            count++
            secQuestListSE!!.add(t)
        }

    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.CancelButtonSE -> {
                setResult(Activity.RESULT_CANCELED)
                return
            }
            R.id.SaveButtonSE -> {
                dataChange()
                setResult(Activity.RESULT_OK, result)
                return
            }
            R.id.AddSecQuestBNuttonSE -> {
                val intent = Intent(this, SecQuestEdit::class.java)
                val resultData = Intent(this, SecQuest::class.java)
                val resultcode = 0
                val requestcode = 0
                this.startActivity(intent)
                onActivityResult(requestcode, resultcode, resultData)
                if (resultcode == RESULT_OK) {
                    val tSQ = SecQuest(null, null)
                    val results = resultData.getExtras()
                    tSQ.setSecQuest(results.get("secQuest") as ByteArray)
                    tSQ.setAnswer(results.get("secAnswr") as ByteArray)
                    secQuestListSE!!.add(tSQ)
                }
            }
        }
    }

    fun dataChange() {
        val tDate = Date()
        result = Intent(this, Site::class.java)
        result!!.putExtra("siteName", siteNameSE!!.getText().toString())
        result!!.putExtra("password", passwordSE!!.getText().toString())
        result!!.putExtra("date", tDate.toString())
        var count = 0
        for (item in secQuestListSE!!) {
            count++
            val tq = "secQuest$count"
            val ta = "secAnswr$count"
            result!!.putExtra(tq, item.getSecQuest())
            result!!.putExtra(ta, item.getAnswer())
        }
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : SITEEDIT Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : SITEEDIT Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : SITEEDIT Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : SITEEDIT Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : SITEEDIT Activity Destroyed")
    }
}
