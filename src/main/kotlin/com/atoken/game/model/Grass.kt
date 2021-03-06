package com.atoken.game.model

import com.atoken.game.Config
import org.itheima.kotlin.game.core.Painter

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 草
 */
class Grass(override val x: Int, override val y: Int) : IView {

    override val width: Int = Config.block
    override val height: Int = Config.block


    override fun draw() {
        Painter.drawImage("img/grass.gif", x, y)
    }
}