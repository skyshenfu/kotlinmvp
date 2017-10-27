package com.pain.wetestmvpkotlin

import android.app.Application
import android.widget.Toast
import com.pain.wetestkotlin.utils.Event
import com.pain.wetestmvpkotlin.utils.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by zty
 *个人github地址：http://www.github.com/skyshenfu
 *日期：2017/6/20
 *版本：1.0.0
 *描述：
 */
class App:Application(){
    override fun onCreate() {
        super.onCreate()
        RxBus.getInstance().toObservable(Event::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :Consumer<Event>{

                    override fun accept(t: Event) {
                        if (t!!.name == "hello")
                            Toast.makeText(this@App,"hello",Toast.LENGTH_SHORT).show()
                    }

                })
        Observable.interval(10, TimeUnit.SECONDS).take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { RxBus.getInstance().post(Event("textchange")) }

    }
}