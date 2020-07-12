package com.dustinpernel.pwmgr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.view.View
import android.widget.SimpleAdapter

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by dustinpernell on 4/19/17.
 */

class SavedSiteList : AppCompatActivity(), AdapterView.OnItemClickListener {

    //Saved_Sites (SS)
    private var addSiteSS: Button? = null
    private var closeButton: Button? = null
    private var savedSitesListSS: ListView? = null
    private val siteListSS = ArrayList()
    private var rawData = ByteArray(15000)
    private var newData: ByteArray? = ByteArray(15000)

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_sites)
        Log.d("LOAD", "SavedSiteList Loaded")

        val intent = getIntent()
        rawData = intent.getExtras().get("data")
        dataFilling()

        savedSitesListSS = findViewById(R.id.SiteListSS) as ListView
        addSiteSS = findViewById(R.id.AddNewSiteSS) as Button
        closeButton = findViewById(R.id.CloseFile) as Button

        addSiteSS!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "AddNewSiteSS clicked")
                addPress()
            }
        })

        closeButton!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                Log.d("ACTION", "CloseFile Clicked")
                closePress()
            }
        })

        savedSitesListSS!!.setOnItemClickListener(this)

    }

    fun closePress() {
        dataSaving()
        val finalFile = Intent(this, String::class.java)
        finalFile.putExtra("data", newData)
        setResult(RESULT_OK, finalFile)
    }

    fun addPress() {
        val intent = Intent(this, Site::class.java)
        this.startActivityForResult(intent, 9)
    }

    fun updateList() {
        Log.d("METHOD", "updateList")

        if (siteListSS.size() === 0) {
            return
        }

        val delete = byteArrayOf('D'.toByte(), 'E'.toByte(), 'L'.toByte(), 'E'.toByte(), 'T'.toByte(), 'E'.toByte())

        val data = ArrayList<HashMap<String, ByteArray>>()
        for (item in siteListSS) {

            if (item.getSite() === delete) {
                siteListSS.remove(item)
            } else {
                val map = HashMap<String, ByteArray>()
                map.put("Site", item.getSite())
                map.put("Date", item.getEditDate())
                data.add(map)
            }
        }

        val resource = R.id.SiteListSS
        val from = arrayOf("stie", "date")
        val to = intArrayOf(R.id.SiteViewSI, R.id.PWViewSI)

        // create and set the adapter
        val adapter = SimpleAdapter(this, data, resource, from, to)
        savedSitesListSS!!.setAdapter(adapter)
    }

    @Override
    fun onItemClick(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        Log.d("METHOD", "onItemClick")

        val item = siteListSS.get(position)

        val intent = Intent(this, Site::class.java)

        intent.putExtra("site", item.getSite())
        intent.putExtra("password", item.getPassword())
        intent.putExtra("editDate", item.getEditDate())
        intent.putExtra("position", position)

        var count = 0
        for (secQuestItem in item.getSecQuestList()) {
            count++
            val tq = "secQuest$count"
            val ta = "secAnswr$count"
            intent.putExtra(tq, secQuestItem.getSecQuest())
            intent.putExtra(ta, secQuestItem.getAnswer())
        }

        this.startActivityForResult(intent, 8)
    }

    private fun dataFilling() {
        Log.d("METHOD", "dataFilling")

        var position = 0
        val setEnds = IntArray(1000)
        if (rawData.size != 0) {
            var count = 0
            do {
                setEnds[count] = setEnd(position)
                count++
                position++
            } while (rawData.size > position)
        }


        val tS = Site()
        val tSQ = SecQuest(null, null)

        for (i in setEnds) {
            var curByte: Byte = '1'.toByte()
            var pos = 0
            val cPos = i + 5

            if (i == rawData.size - 1) {

            } else {
                when (rawData[i + 4]) {
                    's', 'S' -> {
                        if (!tS.getSite().equals(null)) {
                            tS.addSecQuest(tSQ)
                            siteListSS.add(tS)
                            updateList()
                            tS.setSite(null)
                        }
                        val site = ByteArray(250)
                        while (curByte != '-'.toByte()) {
                            site[pos] = rawData[cPos]
                            curByte = rawData[cPos]
                            pos++
                        }
                        tS.setSite(site)
                    }
                    'p', 'P' -> {
                        val pw = ByteArray(32)
                        while (curByte != '-'.toByte()) {
                            pw[pos] = rawData[cPos]
                            curByte = rawData[cPos]
                            pos++
                        }
                        tS.setPassword(pw)
                    }
                    'd', 'D' -> {
                        val date = ByteArray(50)
                        while (curByte != '-'.toByte()) {
                            date[pos] = rawData[cPos]
                            curByte = rawData[cPos]
                            pos++
                        }
                        tS.setEditDate(date)
                    }
                    'q', 'Q' -> {
                        if (!tSQ.getSecQuest().equals(null)) {
                            tS.addSecQuest(tSQ)
                            tSQ.setSecQuest(null)
                        }
                        val secQ = ByteArray(500)
                        while (curByte != '-'.toByte()) {
                            secQ[pos] = rawData[cPos]
                            curByte = rawData[cPos]
                            pos++
                        }
                        tSQ.setSecQuest(secQ)
                    }
                    'a', 'A' -> {
                        val secA = ByteArray(1000)
                        while (curByte != '-'.toByte()) {
                            secA[pos] = rawData[cPos]
                            curByte = rawData[cPos]
                            pos++
                        }
                        tSQ.setAnswer(secA)
                    }
                }
            }
        }
    }

    private fun setEnd(pos: Int): Int {
        Log.d("METHOD", "setEnd")

        var test = true
        var x = pos
        var y = x + 1
        var z = y + 1
        var xC: Byte
        var yC: Byte
        var zC: Byte

        do {
            if (x == rawData.size - 1 || y == rawData.size - 1 || z == rawData.size - 1) {
                return rawData.size - 1
            } else {

                xC = rawData[x]
                yC = rawData[y]
                zC = rawData[z]
                if (xC == yC && xC == zC && xC == '-'.toByte()) {
                    test = false
                } else if (x == rawData.size) {
                    test = false
                } else {
                    x++
                    y++
                    z++
                }
            }
        } while (test)
        return x
    }

    private fun dataSaving() {
        Log.d("METHOD", "dataSaving")

        newData = "This phrase serves to check the key".getBytes()
        for (item in siteListSS) {
            while (newData != null) {
                newData = newData
            }
            var count = 0
            val data = ByteArray(newData!!.size)
            val resultS = byteArrayOf('-'.toByte(), '-'.toByte(), '-'.toByte(), 'S'.toByte())
            val resultP = byteArrayOf('-'.toByte(), '-'.toByte(), '-'.toByte(), 'P'.toByte())
            val resultD = byteArrayOf('-'.toByte(), '-'.toByte(), '-'.toByte(), 'D'.toByte())
            val resultQ = byteArrayOf('-'.toByte(), '-'.toByte(), '-'.toByte(), 'Q'.toByte())
            val resultA = byteArrayOf('-'.toByte(), '-'.toByte(), '-'.toByte(), 'A'.toByte())

            for (b in newData!!) {
                data[count] = b
                count++
            }

            for (b in resultS) {
                data[count] = b
                count++
            }

            while (item.getSite() != null) {
                for (b in item.getSite()) {
                    data[count] = b
                    count++
                }
            }
            for (b in resultP) {
                data[count] = b
                count++
            }

            while (item.getSite() != null) {
                for (b in item.getPassword()) {
                    data[count] = b
                    count++
                }
            }
            for (b in resultD) {
                data[count] = b
                count++
            }

            while (item.getSite() != null) {
                for (b in item.getEditDate()) {
                    data[count] = b
                    count++
                }
            }

            for (i in item.getSecQuestList()) {
                for (b in resultQ) {
                    data[count] = b
                    count++
                }

                while (item.getSite() != null) {
                    for (b in i.getSecQuest()) {
                        data[count] = b
                        count++
                    }
                }
                for (b in resultA) {
                    data[count] = b
                    count++
                }

                while (item.getSite() != null) {
                    for (b in i.getAnswer()) {
                        data[count] = b
                        count++
                    }
                }
            }
        }
    }

    @Override
    protected fun onStart() {
        super.onStart()
        Log.i("Activity Life Cycle ", ": onStart : SAVEDSITELIST Activity Started")

    }

    @Override
    protected fun onResume() {
        super.onResume()
        Log.i("Activity Life Cycle ", ": onResume : SAVEDSITELIST Activity Resumed")
    }

    @Override
    protected fun onPause() {
        super.onPause()
        Log.i("Activity Life Cycle ", ": onPause : SAVEDSITELIST Activity Paused")
    }

    @Override
    protected fun onStop() {
        super.onStop()
        Log.i("Activity Life Cycle ", ": onStop : SAVEDSITELIST Activity Stoped")
    }

    @Override
    protected fun onDestroy() {
        super.onDestroy()
        Log.i("Activity Life Cycle ", ": onDestroy : SAVEDSITELIST Activity Destroyed")
    }


    @Override
    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("METHOD", "onActivityResult_SavedSiteList")
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                val tS = Site()
                val results = data.getExtras()
                tS.setSite(results.get("siteName") as ByteArray)
                tS.setPassword(results.get("sitePassword") as ByteArray)
                tS.setEditDate(results.get("editDate") as ByteArray)
                tS.setPosition(results.getInt("position", 0))

                var count = 1
                while (results.get("secQuest$count") != null) {
                    val t = SecQuest(results.get("secQuest$count") as ByteArray,
                            results.get("secAnswr$count") as ByteArray)
                    tS.addSecQuest(t)
                    count++
                }
                siteListSS.add(tS.getPosition(), tS)
                updateList()

            }
        }
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                val tS = Site()
                val results = data.getExtras()
                tS.setSite(results.get("siteName") as ByteArray)
                tS.setPassword(results.get("sitePassword") as ByteArray)
                tS.setEditDate(results.get("editDate") as ByteArray)
                tS.setPosition(results.getInt("position", 0))

                var count = 1
                while (results.get("secQuest$count") != null) {
                    val t = SecQuest(results.get("secQuest$count") as ByteArray,
                            results.get("secAnswr$count") as ByteArray)
                    tS.addSecQuest(t)
                    count++
                }
                siteListSS.add(tS)
            }
        }
    }
}
