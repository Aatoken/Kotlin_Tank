package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.IAttackable
import com.atoken.game.business.IBlockable
import com.atoken.game.business.ISufferable
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 铁 : 障碍物
 */

class Steel (override val x: Int, override val y: Int)
    :IBlockable,ISufferable {
    override val blood: Int=1


    override fun notifySuffer(attackable: IAttackable): Array<IView>? {
        //喊疼
        Composer.play("snd/hit.wav")
         return null
    }

    override val width: Int = Config.block
    override val height: Int = Config.block


    override fun draw() {
        Painter.drawImage("img/steel.gif",x,y)
    }


}