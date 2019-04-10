package com.gioppl.eventbusdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    private var tv_main:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_main=findViewById(R.id.tv_main)
    }

    @Subscribe
     fun onMessageEvent(messageEvent: MessageEvent){
        tv_main!!.text=messageEvent.message
    }

    @Suppress("UNUSED_PARAMETER")
     fun sendEvent(view:View){
        EventBus.getDefault().post(MessageEvent("GIOPPL"))
    }
    @Suppress("UNUSED_PARAMETER")
    fun activityClick(view:View){
        EventBus.getDefault().postSticky(MessageEvent("你好"))
        startActivity(Intent(this@MainActivity,ThreadActivity::class.java))
    }



    /**
     * @desc  EventBus有四种模式
          POSTING：在当前调用EventBus#post(event)的线程执行
          MAIN：在主线程（即Android的UI线程）执行
          BACKGROUND：当前执行线程为主线程，就切换到后台线程执行；当前线程为非主线程，则就在当前线程执行
          ASYNC：新开后台线程执行
     * @time 2019/4/10
     * @author 17664
     */
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onPostingThreadEvent(event: MessageEvent) {
        Log.i("TEST", "POSTING --> I am on Thread " + Thread.currentThread().name)
    }
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainThreadEvent(event: MessageEvent) {
        Log.i("TEST", "MAIN --> I am on Thread " + Thread.currentThread().name)
    }
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onBackgroundThreadEvent(event: MessageEvent) {
        Log.i("TEST", "BACKGROUND --> I am on Thread " + Thread.currentThread().name)
    }
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onAsyncThreadEvent(event: MessageEvent) {
        Log.i("TEST", "ASYNC --> I am on Thread " + Thread.currentThread().name)
    }
    @Suppress("UNUSED_PARAMETER")
     fun threadClick(view: View){
        EventBus.getDefault().post(MessageEvent("发送 thread 消息，请看打印日志"))

    }




    /**
     * @desc 优先级事件0为最低
     * @time 2019/4/10
     * @author 17664
    */
    @Suppress("UNUSED_PARAMETER")
    fun priorityClick(view:View){
        EventBus.getDefault().post(MessageEvent("PRIORITY"))
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(priority = 0) // 指定优先级为0，默认是0
    fun onLowPriorityEvent(event: MessageEvent) {
        Log.i("TEST", "onLowPriorityEvent")
    }
    @Suppress("UNUSED_PARAMETER")
    @Subscribe(priority = 10) // 指定优先级为10
    fun onHighPriorityEvent(event: MessageEvent) {
        Log.i("TEST", "onHighPriorityEvent")
    }


    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
}
