# EventBus库的使用
## 环境（Kotlin，Android 3.x）
## 导入方法
```
    implementation 'org.greenrobot:eventbus:3.1.1'
```
## 第一步
> 设置一个Message class可以随便设置(kotlin就是这么简单)
```
class MessageEvent(val message:String)
```

## 第二步 
> 在BaseActivity中的onStop()和onStart()中设置取消注册和注册方法
```
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
```
## 第1种方式：普通事件

```
    @Subscribe
     fun onMessageEvent(messageEvent: MessageEvent){
        Log.i("AA",messageEvent.message)
    }
    //这个是按钮的点击事件，随便一个button就行
     fun sendEvent(view:View){
        EventBus.getDefault().post(MessageEvent("GIOPPL"))
    }
    
```
## 第2种方式：粘性事件
> 举个例子，在第一个activity中发送粘性事件以后，第二个activity可能还没有创建没有绑定，这个消息也会被第二个activity注册以后接受的到。

```
    //第一个avtivity
    fun activityClick(view:View){
        EventBus.getDefault().postSticky(MessageEvent("你好"))
        startActivity(Intent(this@MainActivity,ThreadActivity::class.java))
    }
    //第二个activity
    @Subscribe(sticky = true)
    fun onStickyEvent(event: MessageEvent) {
        textView!!.text="从上一个界面里传出来："+event.message
    }
```
## 第3种方式：优先级顺序
```
    //其中0最高
    @Subscribe(priority = 0) // 指定优先级为0，默认是0
    fun onLowPriorityEvent(event: MessageEvent) {
        Log.i("TEST", "onLowPriorityEvent")
    }
    @Subscribe(priority = 10) // 指定优先级为10
    fun onHighPriorityEvent(event: MessageEvent) {
        Log.i("TEST", "onHighPriorityEvent")
    }
```
> 接着你就会在logcat中看到

```
I/TEST: onHighPriorityEvent
I/TEST: onLowPriorityEvent
```

## 第4种方式：不同线程中传递信息
> EventBus有四种模式
- POSTING：在当前调用EventBus#post(event)的线程执行

- MAIN：在主线程（即Android的UI线程）执行
- BACKGROUND：当前执行线程为主线程，就切换到后台线程执行；当前线程为非主线程，则就在当前线程执行
- ASYNC：新开后台线程执行

```
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onPostingThreadEvent(event: MessageEvent) {
        Log.i("TEST", "POSTING --> I am on Thread " + Thread.currentThread().name)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainThreadEvent(event: MessageEvent) {
        Log.i("TEST", "MAIN --> I am on Thread " + Thread.currentThread().name)
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onBackgroundThreadEvent(event: MessageEvent) {
        Log.i("TEST", "BACKGROUND --> I am on Thread " + Thread.currentThread().name)
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onAsyncThreadEvent(event: MessageEvent) {
        Log.i("TEST", "ASYNC --> I am on Thread " + Thread.currentThread().name)
    }
    //button
    fun threadClick(view: View){
        EventBus.getDefault().post(MessageEvent("发送 thread 消息，请看打印日志"))

    }
```
> 随后你就会看到locat中有
    
```
I/TEST: MAIN --> I am on Thread main
I/TEST: ASYNC --> I am on Thread pool-1-thread-1
I/TEST: BACKGROUND --> I am on Thread pool-1-thread-2
I/TEST: POSTING --> I am on Thread main

```


