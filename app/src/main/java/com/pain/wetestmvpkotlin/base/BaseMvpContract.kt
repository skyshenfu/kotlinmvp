package com.pain.wetestmvpkotlin.base

import android.content.Context
import android.util.Log
import com.pain.wetestkotlin.utils.Event
import com.pain.wetestmvpkotlin.utils.RxBus
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by zty
 *个人github地址：http://www.github.com/skyshenfu
 *日期：2017/6/19
 *版本：1.0.0
 *描述：一个Contract的基类包含了mvp三大核心元素
 */
interface BaseMvpPresenter{
    fun initMvpView()
    fun destroyMvpView()
}
interface BaseMvpView{
    fun showProgress()
    fun hideProgress()
    fun showData(model: BaseMvpModel)
}
abstract class BaseMvpModel
open class BaseMvpPresenterImpl<V:BaseMvpView>(context: Context,view: BaseMvpView):BaseMvpPresenter{

    protected var viewRefs:WeakReference<V> ?= null
    protected var contextRefs:WeakReference<Context> ?=null
    //等下写成懒加载形式的
    protected val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    //一些初始化操作
    override fun initMvpView() {

    }
    init {
        viewRefs=WeakReference<V>(view as V)
        contextRefs=WeakReference<Context>(context)
    }
    /**
     * bindRxbus需要接受变更的一端才使用，参数为接收端更新本身的方法
     */
    protected fun bindRxbus(consumer:Consumer<Event>){
        compositeDisposable.add(RxBus.getInstance().toObservable(Event::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer))
    }

    /**
     * 解绑方法，写在基类当中是为了自动调用，防止内存泄漏以及Rxbus重复接收时间
     */
    override fun destroyMvpView() {
        if (viewRefs!=null){
            viewRefs!!.clear()
            viewRefs=null
        }
        if (contextRefs!=null){
            contextRefs!!.clear()
            contextRefs=null
        }
            compositeDisposable.clear()
        Log.e("here","sssss1")
    }
     fun getView(): V? {
        if (viewRefs!=null){
            return viewRefs!!.get() as V
        }else{
            return  null
        }
    }
    fun getContext(): Context? {
        if (contextRefs!=null){
            return contextRefs!!.get() as Context
        }else{
            return  null
        }
    }




}