package com.atoken.game.business

import com.atoken.game.Config
import com.atoken.game.enums.Direction
import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 移动的能力
 */

interface IMovable : IView {


    /**
     * 可移动的物体存在方向
     */
    val currentDirection: Direction

    /**
     * 可移动的物体需要右移动的速度
     */
    val speed: Int

    /**
     * 检测到遇到障碍物的方向
     */
    var badDirection: Direction?

    /**
     * 什么样的障碍物
     */
    var blockable: IBlockable?

    /**
     * 检测在那个方向那个将会发生碰撞
     */
    fun willCollision(block: IBlockable): Direction? {

        //未来的坐标
        var x = this.x
        var y = this.y

        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }

        if (x < 0) return Direction.LEFT
        if (x > Config.gameWidth - width) return Direction.RIGHT
        if (y < 0) return Direction.UP
        if (y > Config.gameHeight - height) return Direction.DOWN

        val collision = checkCollision(block.x, block.y, block.width, block.height
                , x, y, width, height)


        return if (collision) currentDirection else null
    }


    /**
     * 通知碰撞
     */
    fun notifyCollision(direction: Direction?, block: IBlockable?)



}