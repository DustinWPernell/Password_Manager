package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.os.Bundle

/**
 * Created by dustinpernell on 4/18/17.
 */

class SecQuest : Activity, View.OnClickListener {
    //Sec_Quest_Info (SQI)
    private var secQuestSQI: TextView? = null
    private var answerSQI: TextView? = null
    private var returnSQI: Button? = null
    private var editSQI: Button? = null
    private var deleteSQI: Button? = null

    var secQuest: ByteArray? = null
    var answer: ByteArray? = null
    private val result = Intent(this, SecQuest::class.java)

    constructor() {}

    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sec_quest_info)
        Log.d("LOAD", "SecQuestI Loaded")


        secQuestSQI = findViewById(R.id.SecQuestSQI) as TextView
        answerSQI = findViewById(R.id.AnswerSQI) as TextView
        returnSQI = findViewById(R.id.ReturnButtonSQI) as Button
        editSQI = findViewById(R.id.EditButtonSQI) as Button
        deleteSQI = findViewById(R.id.DeleteButtonSQI) as Button

        val intent = getIntent()
        secQuest = intent.getExtras().get("secQuest")
        answer = intent.getExtras().get("answer")

        secQuestSQI!!.setText(secQuest!!.toString())
        answerSQI!!.setText(answer!!.toString())

    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.ReturnButtonSQI -> setResult(Activity.RESULT_CANCELED)
            R.id.EditButtonSQI -> {
                val intent = Intent(this, SecQuestEdit::class.java)
                intent.putExtra("secQuest", this.secQuest)
                intent.putExtra("answer", this.answer)

                this.startActivity(intent)
                val resultData = Intent(this, SecQuest::class.java)
                val resultcode = 0
                val requestcode = 0
                onActivityResult(requestcode, resultcode, resultData)
                if (resultcode == RESULT_OK) {
                    val results = resultData.getExtras()
                    this.secQuest = results.get("siteName")
                    this.answer = results.get("sitePassword")
                }
                return
            }
            R.id.DeleteButtonSQI -> {
                val delete = byteArrayOf('D'.toByte(), 'E'.toByte(), 'L'.toByte(), 'E'.toByte(), 'T'.toByte(), 'E'.toByte())
                secQuest = delete
                result.putExtra("secQuest", secQuest)
                result.putExtra("answer", answer)
                setResult(Activity.RESULT_OK, result)
                return
            }
        }
    }


    constructor(tQuest: ByteArray, tAnswr: ByteArray) {
        secQuest = tQuest
        answer = tAnswr
    }


    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : SECQUEST Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : SECQUEST Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : SECQUEST Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : SECQUEST Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : SECQUEST Activity Destroyed")
    }
}
