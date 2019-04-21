package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.*
import com.atoken.game.enums.Direction
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter


/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 坦克 可以移动，可以被销毁，障碍物，可以被挨打
 */
class Tank(override var x: Int, override var y: Int) :
        IMovable, IBlockable, ISufferable {


    override val width: Int = Config.block
    override val height: Int = Config.block

    //方向
    override var currentDirection: Direction = Direction.UP
    override val speed: Int = 16

    //坦克不可以走的方向
    override var badDirection: Direction? = null
    override var blockable: IBlockable? = null


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
    override fun move(direction: Direction) {

        // 判断是否是往要碰撞的方向走
        if (direction == badDirection) {
            //不往下执行，不走了
            return
        }
        //当前的方向，和希望移动的方向不一致时，只做方向改变
        if (this.currentDirection != direction) {
            this.currentDirection = direction
            return
        }

        //坦克的坐标需要发生变化
        //根据不同的方向，改变对应的坐标
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
        //越界的判断
        if (x < 0) x = 0
        if (x > Config.gameWidth - width) x = Config.gameWidth - width
        if (y < 0) y = 0
        if (y > Config.gameHeight - height) y = Config.gameHeight - height
    }

    /**
     * 更新坦克的移动方向
     */
    override fun notifyCollision(direction: Direction?, block: IBlockable?) {
        //接收到碰撞信息
        this.badDirection = direction
        this.blockable = block
    }

    /**
     * 发射子弹
     */
    fun shot(): Bullet {

        return Bullet(currentDirection, { bulletWidth, bulletHeight ->

            //计算子弹真实的坐标
            val tankX = x
            val tankY = y
            val tankWidth = width
            val tankHeight = height

            var bulletX = 0
            var bulletY = 0

            when (currentDirection) {
                Direction.UP -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY - bulletHeight / 2
                }
                Direction.DOWN -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY + tankHeight - bulletHeight / 2
                }
                Direction.LEFT -> {
                    bulletX = tankX - bulletWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }
                Direction.RIGHT -> {
                    bulletX = tankX + tankWidth - bulletWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }
            }
            Pair(bulletX, bulletY)

        }, this)
    }




    //坦克的血量
    override var blood: Int = 4

    //被攻击之后
    override fun notifySuffer(attackable: IAttackable): Array<IView>? {
        blood -= attackable.attackPower

        //喊疼
        Composer.play("snd/hit.wav")
        return null
    }

}