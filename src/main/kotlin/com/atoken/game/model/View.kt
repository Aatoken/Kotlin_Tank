package com.atoken.game.model

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 显示的视图，定义显示规范
 */
interface View {

    //位置
    val x: Int
    val y: Int
    //宽高
    val width: Int
    val height: Int
    //draw
    fun draw()


}