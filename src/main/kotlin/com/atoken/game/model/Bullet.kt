package com.atoken.game.model

import com.atoken.game.business.IBlockable
import com.atoken.game.business.IMovable
import com.atoken.game.enums.Direction
import org.itheima.kotlin.game.core.Painter


/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 子弹
 */
class Bullet(override val currentDirection: Direction,
             create: (width: Int, height: Int) -> Pair<Int, Int>) : IMovable {

    override var x: Int = 0

    override var y: Int = 0


    override val speed: Int = 32


    override fun notifyCollision(direction: Direction?, block: IBlockable?) {

    }


    override var width: Int = 100

    override var height: Int = 100


    //子弹的路径
    private var imagePath: String = when (currentDirection) {
        Direction.UP -> "img/shot_u.gif"
        Direction.DOWN -> "img/shot_d.gif"
        Direction.LEFT -> "img/shot_l.gif"
        Direction.RIGHT -> "img/shot_r.gif"
    }

    init {
        //先计算子弹的宽度与高度
        val size=Painter.size(imagePath)
        width=size[0]
        height=size[1]
        //计算出子弹的位置
        val pair = create.invoke(width,height)
        x = pair.first
        y = pair.second
    }

    /**
     * 绘画子弹
     */
    override fun draw() {
        Painter.drawImage(imagePath,x,y)
    }


}