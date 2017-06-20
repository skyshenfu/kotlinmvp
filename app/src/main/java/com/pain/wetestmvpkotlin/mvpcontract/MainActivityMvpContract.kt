package com.pain.wetestmvpkotlin.mvpcontract

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.pain.wetestkotlin.beans.ArticleTypeBean
import com.pain.wetestkotlin.utils.ApiResponse
import com.pain.wetestkotlin.utils.Event
import com.pain.wetestkotlin.utils.NetApi
import com.pain.wetestkotlin.utils.SchedulersUtil
import com.pain.wetestmvpkotlin.base.BaseMvpModel
import com.pain.wetestmvpkotlin.base.BaseMvpPresenterImpl
import com.pain.wetestmvpkotlin.base.BaseMvpView
import com.pain.wetestmvpkotlin.utils.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by zty
 *个人github地址：http://www.github.com/skyshenfu
 *日期：2017/6/19
 *版本：1.0.0
 *描述：
 */
//model
class MainModel:BaseMvpModel(){
    var numberStr:String ?=null
}
//view只有接口
interface MainView:BaseMvpView{
    fun textchange()
}
//presenter调用view里面的方法更新UI
class MainPresenter(context: Context,view: MainView): BaseMvpPresenterImpl<MainView>(context,view) {
    //懒加载，不用的时候不进行初始化
    val mainMode:MainModel by lazy {
        MainModel()
    }
    init {
        //需要RxBUS时在presenter初始化时进行监听
        this.bindRxbus(io.reactivex.functions.Consumer<Event> { t ->
            if (t!!.name=="textchange")
                getView()!!.textchange()
        })
    }
    var data:MainModel?=null
    override fun initMvpView() {
        super.initMvpView()
    }
    fun testRxbusPost(){
        RxBus.getInstance().post(Event("hello"))
    }
    //这个方法没啥用
    fun testRxbusReceive(){

    }
    fun loadData(){
        getView()!!.showProgress()
       val observer=object : DisposableObserver<Long>() {
           override fun onError(e: Throwable?) {
           }

           override fun onComplete() {
               getView()!!.hideProgress()
           }

           override fun onNext(t: Long?) {
               mainMode.numberStr= Random().nextInt(100).toString()
               getView()!!.showData(mainMode)
           }

       }
        compositeDisposable.add(observer)
        Observable.interval(3,TimeUnit.SECONDS).take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    fun testNetRequest() {
        getView()!!.showProgress()
        var observer=object :DisposableObserver<ApiResponse<ArticleTypeBean>>(){
            override fun onError(e: Throwable?) {
                Log.e("here",e.toString())
            }
            override fun onComplete() {
                getView()!!.hideProgress()
            }

            override fun onNext(t: ApiResponse<ArticleTypeBean>?) {
                Toast.makeText(getContext(),"haha",Toast.LENGTH_SHORT).show()
            }
        }
        compositeDisposable.add(observer)
        NetApi.instance.gainArticleResult().compose(SchedulersUtil.iotomain())
                .subscribe(observer)
    }
}