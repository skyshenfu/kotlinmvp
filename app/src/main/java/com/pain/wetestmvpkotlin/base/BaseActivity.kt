package com.pain.wetestmvpkotlin.base

import android.support.v7.app.AppCompatActivity

/**
 * Created by zty
 *个人github地址：http://www.github.com/skyshenfu
 *日期：2017/6/20
 *版本：1.0.0
 *描述：基类bindDestory是一个防止内存泄露的方法
 */
abstract class BaseActivity :AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        bindDestroy()
    }
    abstract fun bindDestroy()
}