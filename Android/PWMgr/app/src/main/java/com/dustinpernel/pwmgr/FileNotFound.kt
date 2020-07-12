package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

/**
 * Created by dustinpernell on 4/20/17.
 */

class FileNotFound : Activity() {

    //File_Not_Found (FNF)
    private var createNewFile: Button? = null
    private var lookForFile: Button? = null


    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_not_found)
        Log.d("LOAD", "FileNotFound Loaded")

        createNewFile = findViewById(R.id.CreateButtonFNF) as Button
        lookForFile = findViewById(R.id.LookButtonFNF) as Button

        createNewFile!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "CreateNewFile Button Pressed")
                createNew()
            }
        })

        lookForFile!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "LookForFile Button Pressed")
                lookForFile()
            }
        })
    }

    fun lookForFile() {
        val intent = Intent(this, FileBrowser::class.java)
        startActivityForResult(intent, 4)
    }

    fun createNew() {
        val intent = Intent(this, NewFile::class.java)
        startActivityForResult(intent, 5)
    }

    @Override
    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("METHOD", "onActivityResult_FNF")

        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data)
                finish()
            }
        }

        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data)
                finish()
            }
        }

    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : FNF Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : FNF Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : FNF Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : FNF Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : FNF Activity Destroyed")
    }
}
