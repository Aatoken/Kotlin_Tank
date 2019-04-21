package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.*
import com.atoken.game.enums.Direction
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import java.util.*
import javax.xml.transform.Templates


/**
 * Auther Aatoken
 * Date 2019/4/21
 * Des 可以的移动的(避开障碍物)，被挨打，可以被销毁的，障碍物，自己可以动
 */
class Enemy(override var x: Int, override var y: Int)
    : IMovable, ISufferable, IDestroyable, IBlockable
        , IAutoMovable ,IAutoShot{


    override val width: Int = Config.block

    override val height: Int = Config.block


    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/enemy_1_u.gif"
            Direction.DOWN -> "img/enemy_1_d.gif"
            Direction.LEFT -> "img/enemy_1_l.gif"
            Direction.RIGHT -> "img/enemy_1_r.gif"
        }
        Painter.drawImage(imagePath, x, y)
    }


    override var currentDirection: Direction = Direction.DOWN

    override val speed: Int = 5

    override var badDirection: Direction? = null

    override var blockable: IBlockable? = null

    /**
     * 实时更新遇到障碍物
     */
    override fun notifyCollision(direction: Direction?, block: IBlockable?) {
        //接收到碰撞信息
        this.badDirection = direction
        this.blockable = block
    }

    /**
     * 可以避开障碍物
     */
    override fun move(direction: Direction) {

    }


    //是否存活
    override fun isDestroyed(): Boolean = blood <= 0

    //血量
    override var blood: Int = 2

    //受到攻击刷新
    override fun notifySuffer(attackable: IAttackable): Array<IView>? {
        blood -= attackable.attackPower

        //喊疼
        Composer.play("snd/hit.wav")

        //显示爆炸物
        return arrayOf(Blast(x, y))
    }

    /**
     * 自己移动
     */
    override fun autoMove() {

        // 判断是否是往要碰撞的方向走
        if (currentDirection == badDirection) {
            //不往下执行，不走了
            //改变方向
            currentDirection=rdmDirection(badDirection)
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

    }

    /**
     * 随机返回一个方向
     */
    private fun rdmDirection(bad: Direction?): Direction {
        val i = Random().nextInt(4)
        val direction: Direction = when (i) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            else -> Direction.UP
        }

        if (direction == bad) {
            rdmDirection(bad)
        }

        return direction
    }

    /**
     * 自动射击
     */
    override fun autoShot(): IView? {
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



}