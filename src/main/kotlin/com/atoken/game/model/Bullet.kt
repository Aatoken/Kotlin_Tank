package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.*
import com.atoken.game.enums.Direction
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
        ISufferable,IBlockable {

    //子弹的位置
    override var x: Int = 0
    override var y: Int = 0
    //子弹的速度
    override val speed: Int = 32

    /**
     * 将要遇到障碍物的方向
     */
    override var badDirection: Direction? = null
    /**
     * 将要遇到的障碍物
     */
    override var blockable: IBlockable? = null


    /**
     * 实时刷新子弹遇到的障碍物的方向与障碍物
     */
    override fun notifyCollision(direction: Direction?, block: IBlockable?) {
        //接收到碰撞信息
        this.badDirection = direction
        this.blockable = block
    }


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
    }

    /**
     * 绘画子弹
     */
    override fun draw() {
        Painter.drawImage(imagePath, x, y)
    }


    //移动
    override fun move(direction: Direction) {
        if (badDirection != null) {
            this.isDestroyed = blockable !is Water
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
    override val blood: Int = 1

    /**
     * 实时更新血条
     */
    override fun notifySuffer(attackable: IAttackable): Array<IView>? {

        return null
    }




    //攻击力为2
    override val attackPower: Int=2


    /**
     * 是否发生碰撞
     */
    override fun isCollision(ISufferable: ISufferable): Boolean {
        return false
    }

    override fun notifyAttack(ISufferable: ISufferable) {

    }


}