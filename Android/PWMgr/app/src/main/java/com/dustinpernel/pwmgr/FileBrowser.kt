package com.dustinpernel.pwmgr

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import java.io.File
import java.util.ArrayList
import java.util.Collections

/**
 * Created by dustinpernell on 4/20/17.
 */

class FileBrowser : ListActivity() {
    private var path: String? = null
    private val curDirFiles = ArrayList()

    @Override
    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_browser)
        Log.d("LOAD", "FileBrowser Loaded")


        path = "/"

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path")
        }
        setTitle(path)

        val values = ArrayList()
        val dir = File(path)

        val list = dir.list()
        if (list != null) {
            for (file in list!!) {
                values.add(file)
            }
        }
        Collections.sort(values)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, values)
        setListAdapter(adapter)

    }

    @Override
    fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        var filename = getListAdapter().getItem(position) as String
        if (path!!.endsWith(File.separator)) {
            filename = path!! + filename
        } else {
            filename = path + File.separator + filename
        }
        if (File(filename).isDirectory()) {
            val intent = Intent(this, FileBrowser::class.java)
            intent.putExtra("path", filename)
            startActivity(intent)
        } else {
            val result = Intent(this, FileBrowser::class.java)
            result.putExtra("path", filename)
            setResult(RESULT_OK, result)
        }
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : FILEBROWSE Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : FILEBROWSE Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : FILEBROWSE Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : FILEBROWSE Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : FILEBROWSE Activity Destroyed")
    }
}
