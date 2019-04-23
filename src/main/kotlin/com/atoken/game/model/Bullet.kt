package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.*
import com.atoken.game.enums.Direction
import com.atoken.game.ext.checkCollision
import org.itheima.kotlin.game.core.Painter


/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 子弹: 可移动的，障碍物，具有攻击，能承受攻击，可以被销毁
 */
class Bullet(override val currentDirection: Direction,
             create: (width: Int, height: Int) -> Pair<Int, Int>,
             override val owner: IView) :
        IAutoMovable, IAttackable, IDestroyable,
        ISufferable {


    //子弹的位置
    override var x: Int = 0
    override var y: Int = 0


    //子弹的宽度与高度
    override var width: Int = 100
    override var height: Int = 100

    //子弹是否被销毁
    private var isDestroyed: Boolean = false

    //子弹的路径
    private var imagePath: String = when (currentDirection) {
        Direction.UP -> "img/shot_u.gif"
        Direction.DOWN -> "img/shot_d.gif"
        Direction.LEFT -> "img/shot_l.gif"
        Direction.RIGHT -> "img/shot_r.gif"
    }

    init {
        //先计算子弹的宽度与高度
        val size = Painter.size(imagePath)
        width = size[0]
        height = size[1]
        //计算出子弹的位置
        val pair = create.invoke(width, height)
        x = pair.first
        y = pair.second
        //设置敌我坦克的子弹的攻击力与血量

    }

    /**
     * 绘画子弹
     */
    override fun draw() {
        Painter.drawImage(imagePath, x, y)
    }

    //子弹的速度
    override val speed: Int = 15

    override fun autoMove() {
        //根据自己的方向，来改变自己的x和y
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
    }


    /**
     * 检测是否被销毁了
     */
    override fun isDestroyed(): Boolean {
        if (this.isDestroyed) return true
        //子弹在脱离了屏幕后，需要被销毁
        if (x < -width) return true
        if (x > Config.gameWidth) return true
        if (y < -height) return true
        if (y > Config.gameHeight) return true

        return false
    }


    //血条
    override var blood: Int = 1

    /**
     * 实时更新血条
     */
    override fun notifySuffer(attackable: IAttackable): Array<IView>? {

        return null
    }


    //攻击力为2
    override val attackPower: Int = 1


    /**
     * 是否发生碰撞
     */
    override fun isCollision(sufferable: ISufferable): Boolean {
        return checkCollision(sufferable)
    }

    override fun notifyAttack(sufferable: ISufferable) {
        //自己人打自己人子弹不消失
        isDestroyed =
                if ((sufferable is Enemy) and (this.owner is Enemy)) {
            false
        } else !((sufferable is Tank) and (this.owner is Tank))


    }


}