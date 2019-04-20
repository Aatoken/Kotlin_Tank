package com.atoken.game.business

import com.atoken.game.enums.Direction

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des
 */
interface IHandMovable :IMovable{

    fun move(direction: Direction)


}