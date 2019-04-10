package com.gioppl.eventbusdemo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ThreadActivity : AppCompatActivity() {
    var textView:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        textView=findViewById(R.id.textView)
    }
    @SuppressLint("SetTextI18n")
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(sticky = true)
    fun onStickyEvent(event: MessageEvent) {
        textView!!.text="从上一个界面里传出来"+event.message
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }





}
