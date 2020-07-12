package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

/**
 * Created by dustinpernell on 4/19/17.
 */

class SecQuestEdit : Activity(), View.OnClickListener {

    //Sec_Quest_Edit (SQE)
    private var secQuestSQE: EditText? = null
    private var secAnswrSQE: EditText? = null
    private var saveButtonSQE: Button? = null
    private var clearButtonSQE: Button? = null
    private var cancelButtonSQE: Button? = null

    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sec_quest_edit)
        Log.d("LOAD", "SecQuestE Loaded")


        secQuestSQE = findViewById(R.id.SecQuestInputSQE) as EditText
        secAnswrSQE = findViewById(R.id.SecAnswInputSQE) as EditText
        saveButtonSQE = findViewById(R.id.SaveButtonSQE) as Button
        clearButtonSQE = findViewById(R.id.ClearButtonSQE) as Button
        cancelButtonSQE = findViewById(R.id.CancelButtonSQE) as Button

        secQuestSQE!!.setText(savedInstanceState.get("secQuest").toString())
        secAnswrSQE!!.setText(savedInstanceState.get("answer").toString())

    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.SaveButtonSQE -> {
                val intent = Intent(this, SecQuest::class.java)
                intent.putExtra("secQuest", secQuestSQE!!.getText().toString().getBytes())
                intent.putExtra("answer", secAnswrSQE!!.getText().toString().getBytes())
                setResult(RESULT_OK, intent)
                return
            }
            R.id.ClearButtonSQE -> {
                secQuestSQE!!.setText("")
                secAnswrSQE!!.setText("")
            }
            R.id.CancelButtonSQE -> setResult(RESULT_CANCELED)
        }
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : SECQUESTEDIT Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : SECQUESTEDIT Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : SECQUESTEDIT Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : SECQUESTEDIT Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : SECQUESTEDIT Activity Destroyed")
    }
}
