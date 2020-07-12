package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

/**
 * Created by dustinpernell on 4/20/17.
 */

class NewFile : Activity() {

    //New_File (NF)
    private var titleNF: EditText? = null
    private var pwNF1: EditText? = null
    private var pwNF2: EditText? = null
    private var newfilepathNF: EditText? = null
    private var cancelNF: Button? = null
    private var saveNF: Button? = null

    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_file)
        Log.d("LOAD", "NewFile Loaded")


        titleNF = findViewById(R.id.TitleInputNF) as EditText
        pwNF1 = findViewById(R.id.PWinput1NF) as EditText
        pwNF2 = findViewById(R.id.PWInput2NF) as EditText
        newfilepathNF = findViewById(R.id.FilePathInputNF) as EditText
        cancelNF = findViewById(R.id.CancelButtonNF) as Button
        saveNF = findViewById(R.id.SaveButtonNF) as Button

        newfilepathNF!!.setText("PWMGR/")

        cancelNF!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                cancelButton()
            }
        })
        saveNF!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                saveButton()
            }
        })

    }

    fun cancelButton() {
        Log.d("ACTION", "cancelButton Pressed")
        setResult(RESULT_CANCELED)
        finish()
    }

    fun saveButton() {
        Log.d("ACTION", "saveButton Pressed")
        val pw1 = pwNF1!!.getText().toString()
        val pw2 = pwNF2!!.getText().toString()
        val nfPath = newfilepathNF!!.getText().toString()
        val title = titleNF!!.getText().toString()

        if (pw1.equals("") || pw2.equals("") || nfPath.equals("") || title.equals("")) {
            Log.d("VARIABLES", "A password is blank")
            Toast.makeText(getApplicationContext(), "A password is blank", Toast.LENGTH_LONG)
        } else if (pw1.equals(pw2)) {
            Log.d("VARIABLES", "Passwords Match")
            val resultData = Intent(this, String::class.java)
            resultData.putExtra("title", title)
            resultData.putExtra("pass", pw1.getBytes())
            resultData.putExtra("path", nfPath)

            setResult(RESULT_OK, resultData)
            finish()
        } else {
            Log.d("VARIABLES", "Passwords don't match")
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG)
        }
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : NEWFILE Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : NEWFILE Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : NEWFILE Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : NEWFILE Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : NEWFILE Activity Destroyed")
    }
}
