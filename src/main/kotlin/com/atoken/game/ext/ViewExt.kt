package com.atoken.game.ext

import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/21
 * Des  Iview的外部方法扩展
 */

fun IView.checkCollision(view: IView): Boolean {


    return checkCollision(x, y, width, height,
            view.x, view.y, view.width, view.height)
}