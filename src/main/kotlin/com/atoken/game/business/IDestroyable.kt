package com.atoken.game.business

import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des 可以被销毁的
 */
interface IDestroyable :IView {


    /**
     * 判断是否销毁了 true 就是销毁了
     */
    fun isDestroyed(): Boolean



}