package com.dustinpernel.pwmgr

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset


class PWMgr_Main : AppCompatActivity() {
    private var path = "PWMGR/PWFile.txt"
    private var outputStream: FileOutputStream? = null
    private var inputStream: FileInputStream? = null
    private var beginButton: Button? = null
    private var rawData: ByteArray? = null
    private var newData: ByteArray? = null
    private var password: ByteArray? = null
    private var title: String? = null
    private var fileTester = true

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwmgr__main)
        Log.d("LOAD", "PWMgr Loaded")

        beginButton = findViewById(R.id.BeginButton) as Button
        beginButton!!.setOnClickListener(object : View.OnClickListener {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "BeginButton Pressed")
                loadFile()
            }
        })
        Log.d("VARIABLES", "Main Buttons Set")
    }


    fun loadFile() {
        Log.d("METHOD", "loadFile Loaded")

        Log.d("PROCCESS", "Testing File")

        val test: File

        try {
            inputStream = openFileInput(title)
            inputStream!!.close()
            fileTester = true
        } catch (x: Exception) {
            Log.d("PROCCESS", "Testing File failed")
            fileTester = false
            val intent = Intent(getApplicationContext(), FileNotFound::class.java)
            startActivityForResult(intent, 1)
        }

        if (fileTester) {
            val intent = Intent(this, DecryptionClass::class.java)
            intent.putExtra("path", path)
            intent.putExtra("title", title)
            startActivityForResult(intent, 2)
        }
    }

    fun saveFile() {
        Log.d("METHOD", "saveFile Loaded")

        //        FileCipher fileCipher = new FileCipher(password);

        val dataEncrypted: ByteArray? = null
        try {
            //            dataEncrypted = fileCipher.cryptography(newData,"Encrypt");
        } catch (x: Exception) {
        }

        try {
            outputStream = openFileOutput(title, Context.MODE_PRIVATE)
        } catch (x: IOException) {
        }

        try {
            outputStream!!.write(dataEncrypted)
        } catch (x: IOException) {
        }

    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : Main Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : Main Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : Main Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : Main Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
    }

    @Override
    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("METHOD", "onActivityResult_Main")
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                path = data.getStringExtra("path")
                title = data.getStringExtra("title")
                password = data.getExtras().get("pass")

                setPath()

                writeFileOnInternalStorage()

                Log.d("PROCCESS", "Testing File")

                try {
                    inputStream = openFileInput(title)
                    inputStream!!.close()
                    fileTester = true
                } catch (x: Exception) {
                    Log.d("PROCCESS", "Testing File failed")
                    fileTester = false
                    val intent = Intent(getApplicationContext(), FileNotFound::class.java)
                    startActivityForResult(intent, 1)
                }

                if (fileTester) {
                    val intent = Intent(this, DecryptionClass::class.java)
                    intent.putExtra("path", path)
                    intent.putExtra("title", title!!.toString())
                    startActivityForResult(intent, 2)
                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                rawData = data.getExtras().get("data")
                password = data.getExtras().get("password")
            }
            val intent = Intent(this, SavedSiteList::class.java)
            intent.putExtra("data", rawData)
            startActivityForResult(intent, 3)
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                newData = data.getStringExtra("data").toString().getBytes(Charset.forName("UTF-8"))
                saveFile()
            }
        }
    }


    fun setPath() {
        Log.d("METHOD", "setPath Loaded")
        if (title!!.indexOf(".") !== title!!.lastIndexOf(".")) {
            title = title!!.substring(0, title!!.indexOf(".") - 1)
        }

        if (!title!!.endsWith(".txt")) {
            title = title!! + ".txt"
        }

        if (!path.endsWith("/")) {
            path = "$path/"
        }
    }

    fun writeFileOnInternalStorage() {
        Log.d("METHOD", "writeFileOnInternalStorage Loaded")

        //        FileCipher fileCipher = new FileCipher(password);

        val dataEncrypted = "This phrase serves to check the key".getBytes(Charset.forName("UTF-8"))
        try {
            //            dataEncrypted = fileCipher.cryptography(dataEncrypted,"Encrypt");
        } catch (x: Exception) {
        }

        try {
            outputStream = openFileOutput(title, Context.MODE_PRIVATE)
            outputStream!!.write(dataEncrypted)

        } catch (x: IOException) {
        }

    }
}
