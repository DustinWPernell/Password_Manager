package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Created by dustinpernell on 4/20/17.
 */

class DecryptionClass : Activity() {
    private var continuePWI: Button? = null
    private var cancelPWI: Button? = null
    private var fileTitle: TextView? = null
    private var pwInput: EditText? = null
    private var path: String? = null
    private var title: String? = null
    private var pw: ByteArray? = null
    private var inputStream: FileInputStream? = null
    private var outputStream: FileOutputStream? = null
    private val encData = ByteArray(100)
    private var decData: ByteArray? = null


    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pw_input)
        Log.d("LOAD", "DecryptionClass Loaded")


        continuePWI = findViewById(R.id.EnterPWM) as Button
        cancelPWI = findViewById(R.id.CancelPWM) as Button
        fileTitle = findViewById(R.id.FileNameBoxM) as TextView
        pwInput = findViewById(R.id.FilePasswordM) as EditText

        continuePWI!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "continuePWI Pressed")
                continuePress()
            }
        })
        cancelPWI!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "cancelPWI Pressed")
                setResult(RESULT_CANCELED)
                finish()
            }
        })
        val intent = getIntent()
        path = intent.getStringExtra("path")
        title = intent.getStringExtra("title")
        fileTitle!!.setText(title)

    }

    private fun continuePress() {
        if (pwInput!!.getText().equals(null) || pwInput!!.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_LONG)

        } else {
            try {
                pw = pwInput!!.getText().toString().getBytes(Charset.forName("UTF-8"))
                decrypt(pw)
                Log.d("ACTION", "decrypt run")
                val result = Intent(this, String::class.java)
                result.putExtra("data", decData)
                result.putExtra("password", pw)
                setResult(RESULT_OK, result)
                finish()
            } catch (x: IOException) {
                val intent = Intent(this, FileNotFound::class.java)
                startActivityForResult(intent, 6)
            }

        }
    }


    @Throws(IOException::class)
    private fun decrypt(pw: ByteArray?) {
        Log.d("METHOD", "decrypt Loaded")
        var fullData: ByteArray
        val encDataArray = ArrayList()
        Log.d("ACTION", "Cipher finished")

        val fileCipher = FileCipher(pw)

        inputStream = openFileInput(title)
        Log.d("ACTION", title!! + " opened")
        if (inputStream!!.read(encData, 0, 100) !== -1) {
            val count = 1
            Log.d("READ", "Read group $count")

            encDataArray.add(encData)
            var test: Boolean

            do {
                if (inputStream!!.read(encData, 0, 100) !== -1) {
                    encDataArray.add(encData)
                    test = false
                    Log.d("READ", "Read group $count")
                } else {
                    Log.d("READ", "All groups read")
                    test = true
                }
            } while (!test)
        } else {
            encDataArray.add(encData)
        }
        inputStream!!.close()

        fullData = ByteArray(encDataArray.size() * 100)

        for (bA in encDataArray) {
            fullData = (fullData.toString() + bA.toString()).getBytes(Charset.forName("UTF-8"))
        }
        Log.d("VARIABLES", "All groups added")

        decData = fullData
        try {
            //            decData = fileCipher.cryptography(fullData,"Decrypt");
        } catch (x: Exception) {
            Log.d("ERROR", x.toString())
        }

    }


    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : DECRYPT Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : DECRYPT Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : DECRYPT Activity Paused")
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

    @Override
    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("METHOD", "onActivityResult_DECRYPT")
        if (requestCode == 6) {
            path = data.getStringExtra("path")
            title = data.getStringExtra("title")
            pw = data.getStringExtra("password").getBytes()

            setPath()

            wrtieFileOnInternalStorage()

            Log.d("PROCCESS", "Testing File")

            try {
                inputStream = openFileInput(title)
                inputStream!!.close()
            } catch (x: Exception) {
                Log.d("PROCCESS", "Testing File failed")
                val intent = Intent(getApplicationContext(), FileNotFound::class.java)
                startActivityForResult(intent, 7)
            }

        }
        if (requestCode == 7) {
            title = data.getStringExtra("title")

            val intent = Intent(this, DecryptionClass::class.java)
            intent.putExtra("path", path)
            intent.putExtra("title", title)
            startActivityForResult(intent, 7)
        }
    }

    fun setPath() {
        Log.d("METHOD", "setPath_DECRYPT Loaded")
        if (title!!.indexOf(".") !== title!!.lastIndexOf(".")) {
            title = title!!.substring(0, title!!.indexOf(".") - 1)
        }

        if (!title!!.endsWith(".txt")) {
            title = title!! + ".txt"
        }

        if (!path!!.endsWith("/")) {
            path = path!! + "/"
        }
    }

    fun wrtieFileOnInternalStorage() {
        Log.d("METHOD", "writeFileOnInternalStorage_DECRYPT Loaded")

        try {
            outputStream = openFileOutput(title, Context.MODE_PRIVATE)
            outputStream!!.write("This phrase serves to check the key".getBytes())
            outputStream!!.flush()
            outputStream!!.close()
        } catch (x: Exception) {
        }

    }
}
