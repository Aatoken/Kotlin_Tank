package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.IAttackable
import com.atoken.game.business.IBlockable
import com.atoken.game.business.IDestroyable
import com.atoken.game.business.ISufferable
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 砖墙 障碍物，可以被挨打，可以销毁，
 */

class Wall(override val x: Int, override val y: Int) :
        IBlockable ,ISufferable,IDestroyable{

    override val width: Int = Config.block
    override val height: Int = Config.block
    override fun draw() {
        Painter.drawImage("img/wall.gif", x, y)
    }

    //是否被销毁
    override fun isDestroyed(): Boolean =blood<=0


    //血条
    override var blood: Int=4

    /**
     * 实时刷新
     */
    override fun notifySuffer(attackable: IAttackable): Array<IView>? {
        blood -= attackable.attackPower

        //喊疼
        Composer.play("snd/hit.wav")

        return null
    }



}