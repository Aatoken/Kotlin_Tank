package com.atoken.game.business

import com.atoken.game.enums.Direction
import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des 自动移动
 */
interface IAutoMovable:IView {
    //当前的方向
    val currentDirection: Direction
    //速度
    val speed: Int
    //自动移动
    fun autoMove()


}