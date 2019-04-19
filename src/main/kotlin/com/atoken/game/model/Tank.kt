package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.enums.Direction
import org.itheima.kotlin.game.core.Painter

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 坦克
 */
class Tank(override var x: Int, override var y: Int) : View {

    override val width: Int = Config.block
    override val height: Int = Config.block

    //方向
    var currentDirection: Direction = Direction.UP
    val speed: Int = 16
    override fun draw() {

        val imagePath: String = when (currentDirection) {
            Direction.UP -> "img/tank_u.gif"
            Direction.DOWN -> "img/tank_d.gif"
            Direction.LEFT -> "img/tank_l.gif"
            Direction.RIGHT -> "img/tank_r.gif"
        }

        Painter.drawImage(imagePath, x, y)

    }


    /**
     * 坦克移动
     */
    fun move(direction: Direction) {
        if (direction == currentDirection) {
            when (currentDirection) {
                Direction.UP -> y -= speed
                Direction.DOWN -> y += speed
                Direction.LEFT -> x -= speed
                Direction.RIGHT -> x += speed

            }
        } else {
            this.currentDirection = direction
        }

        if (x < 0) x = 0
        if (x > Config.gameWidth - width) x = Config.gameWidth - width
        if (y < 0) y = 0
        if (y > Config.gameHeight - height) y = Config.gameHeight - height
    }
}