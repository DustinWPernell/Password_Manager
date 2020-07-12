package com.dustinpernel.pwmgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by dustinpernell on 4/18/17.
 */

class Site : Activity(), AdapterView.OnItemClickListener, View.OnClickListener {

    //site_Info (SI)
    private var siteNameSI: TextView? = null
    private var pwSI: TextView? = null
    private var dateSI: TextView? = null
    private var editSI: Button? = null
    private var deleteSI: Button? = null
    private var returnSI: Button? = null
    private var secQuestListSI: ListView? = null

    var site = byteArrayOf('N'.toByte(), 'e'.toByte(), 'w'.toByte(), '_'.toByte(), 'S'.toByte(), 'i'.toByte(), 't'.toByte(), 'e'.toByte())
    var password = byteArrayOf(' '.toByte())
    var editDate = byteArrayOf('N'.toByte(), 'o'.toByte(), 't'.toByte(), ' '.toByte(), 'S'.toByte(), 'e'.toByte(), 't'.toByte())
    var secQuestsSI: ArrayList<SecQuest>? = null
    private val result = Intent(this, Site::class.java)
    var position = 0

    val secQuestList: ArrayList<SecQuest>?
        get() = secQuestsSI


    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.site_info)
        Log.d("LOAD", "SiteI Loaded")



        siteNameSI = findViewById(R.id.SiteViewSI) as TextView
        pwSI = findViewById(R.id.PWViewSI) as TextView
        dateSI = findViewById(R.id.DateViewSE) as TextView
        secQuestListSI = findViewById(R.id.SecQuestListSI) as ListView
        editSI = findViewById(R.id.EditButtonSI) as Button
        deleteSI = findViewById(R.id.DeleteButtonSI) as Button
        returnSI = findViewById(R.id.SaveButtonSE) as Button

        val intent = getIntent()

        try {
            site = intent.getExtras().get("site")
            password = intent.getExtras().get("password")
            editDate = intent.getExtras().get("editDate")
            position = intent.getIntExtra("position", 0)
        } catch (x: NullPointerException) {
            Log.d("ERROR", x.toString())
        }

        siteNameSI!!.setText(site.toString())
        pwSI!!.setText(password.toString())
        dateSI!!.setText(editDate.toString())

        var count = 1
        while (intent.getExtras().get("secQuest$count") != null) {
            val t = SecQuest(intent.getExtras().get("secQuest$count") as ByteArray,
                    intent.getExtras().get("secAnswr$count") as ByteArray)
            count++
            secQuestsSI!!.add(t)
        }

        secQuestListSI!!.setOnItemClickListener(this)


    }

    @Override
    fun onItemClick(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        val item = secQuestsSI!!.get(position)

        val intent = Intent(this, Site::class.java)

        intent.putExtra("question", item.getSecQuest())
        intent.putExtra("answer", item.getAnswer())

        this.startActivity(intent)
    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.EditButtonSI -> {
                val intent = Intent(this, SiteEdit::class.java)
                intent.putExtra("site", this.site)
                intent.putExtra("password", this.password)
                intent.putExtra("editDate", this.editDate)

                var count = 0
                for (item in secQuestsSI!!) {
                    count++
                    val tq = "secQuest$count"
                    val ta = "secAnswr$count"
                    intent.putExtra(tq, item.getSecQuest())
                    intent.putExtra(ta, item.getAnswer())
                }
                val empty = byteArrayOf('E'.toByte(), 'M'.toByte(), 'P'.toByte(), 'T'.toByte(), 'Y'.toByte())
                intent.putExtra("secQuest$count", empty)

                this.startActivity(intent)
                val resultData = Intent(this, SiteEdit::class.java)
                val resultcode = 0
                val requestcode = 0
                onActivityResult(requestcode, resultcode, resultData)
                if (resultcode == RESULT_OK) {
                    val tS = Site()
                    val results = resultData.getExtras()
                    tS.site = results.get("siteName")
                    tS.password = results.get("sitePassword")
                    tS.editDate = results.get("editDate")

                    count = 1
                    while (results.get("secQuest$count") != null) {
                        val t = SecQuest(results.get("secQuest$count") as ByteArray,
                                results.get("secAnswr$count") as ByteArray)
                        count++
                        secQuestsSI!!.add(t)
                    }
                }
                updateList()
            }

            R.id.DeleteButtonSI -> {
                val delete = byteArrayOf('D'.toByte(), 'E'.toByte(), 'L'.toByte(), 'E'.toByte(), 'T'.toByte(), 'E'.toByte())
                this.site = delete
                result.putExtra("siteName", delete)
                setResult(Activity.RESULT_OK, result)
                return
            }

            R.id.ReturnButtonSI -> {
                result.putExtra("siteName", this.site)
                result.putExtra("password", this.password)
                result.putExtra("editDate", this.editDate)
                count = 0
                for (item in secQuestsSI!!) {
                    count++
                    val tq = "secQuest$count"
                    val ta = "secAnswr$count"
                    result.putExtra(tq, item.getSecQuest())
                    result.putExtra(ta, item.getAnswer())
                }
                setResult(Activity.RESULT_OK, result)
                return
            }
        }
    }

    fun updateList() {
        if (secQuestsSI!!.size() === 0) {
            return
        }

        val data = ArrayList()
        val delete = byteArrayOf('D'.toByte(), 'E'.toByte(), 'L'.toByte(), 'E'.toByte(), 'T'.toByte(), 'E'.toByte())

        for (item in secQuestsSI!!) {

            if (item.getSecQuest() === delete) {
                secQuestsSI!!.remove(item)
            } else {
                val map = HashMap()
                map.put("Question", item.getSecQuest())
                data.add(map)
            }
        }

        val resource = R.id.SecQuestListSI
        val from = arrayOf("stie", "date")
        val to = intArrayOf(R.id.SecQuestSQI, R.id.AnswerSQI)

        // create and set the adapter
        val adapter = SimpleAdapter(this, data, resource, from, to)
        secQuestListSI!!.setAdapter(adapter)
    }

    fun addSecQuest(tSecQuest: SecQuest) {
        secQuestsSI!!.add(tSecQuest)
    }

    fun removeSecQuest(tSecQuest: SecQuest): Boolean {
        return secQuestsSI!!.remove(tSecQuest)
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : SITE Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : SITE Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : SITE Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : SITE Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : SITE Activity Destroyed")
    }
}
