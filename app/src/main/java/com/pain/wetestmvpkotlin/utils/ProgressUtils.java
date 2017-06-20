package com.pain.wetestmvpkotlin.utils;

import android.content.Context;
import android.widget.Toast;


import com.pain.wetestmvpkotlin.widgets.LoadingDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zty
 * 个人github地址：http://www.github.com/skyshenfu
 * 日期：2017/3/16
 * 版本号：1.0.0
 * 描述：
 */

public class ProgressUtils {
    private LoadingDialog progress;
    private Context context;
    private DisposableObserver<Long> disposableObserver;

    public DisposableObserver<Long> getDisposableObserver() {
        return disposableObserver;
    }

    public ProgressUtils(Context context) {
        this.context = context;
    }

    public  void progressShow(){
        if (progress==null){
            initProgress();
        }
        progress.show();
        disposableObserver=new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                progressDismiss();
                Toast.makeText(context, "网络情况太差，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(8, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    private void initProgress() {
        progress=new LoadingDialog(context);
    }
    public void progressDismiss(){
        if (progress!=null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
        if (disposableObserver!=null&&!disposableObserver.isDisposed()){
            disposableObserver.dispose();
        }
    }
}
