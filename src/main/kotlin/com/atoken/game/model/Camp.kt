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
 * Date 2019/4/22
 * Des 大本营，可以被攻击的，可以被销毁的，障碍物
 */
class Camp(override var x: Int, override var y: Int) :
        IView,ISufferable ,IDestroyable,IBlockable{


    override var blood: Int=12

    override fun notifySuffer(attackable: IAttackable): Array<IView>? {
        blood -= attackable.attackPower

        //喊疼
        Composer.play("snd/hit.wav")


        if (blood == 3 || blood == 6) {
            val x = x - 32
            val y = y - 32
            return arrayOf(Blast(x, y)
                    , Blast(x + 32, y)
                    , Blast(x + Config.block, y)
                    , Blast(x + Config.block + 32, y)
                    , Blast(x + Config.block * 2, y)
                    , Blast(x, y + 32)
                    , Blast(x, y + Config.block)
                    , Blast(x, y + Config.block + 32)
                    , Blast(x + Config.block * 2, y + 32)
                    , Blast(x + Config.block * 2, y + Config.block)
                    , Blast(x + Config.block * 2, y + Config.block + 32))
        }

        //显示爆炸物
        return  null
    }

    override fun isDestroyed(): Boolean=blood<=0


    override var width: Int = Config.block * 2
    override var height: Int = Config.block + 32

    override fun draw() {
        //血量低于 6个时画的时 砖墙
        //血量低于 3个时画的时 没有墙
        when {
            blood <= 3 -> {
                width = Config.block
                height = Config.block
                x = (Config.gameWidth - Config.block) / 2
                y = Config.gameHeight - Config.block
                Painter.drawImage("img/camp.gif", x, y)

            }
            blood <= 6 -> {

                //绘制外围的砖块
                Painter.drawImage("img/wall_small.gif", x, y)
                Painter.drawImage("img/wall_small.gif", x + 32, y)
                Painter.drawImage("img/wall_small.gif", x + 64, y)
                Painter.drawImage("img/wall_small.gif", x + 96, y)

                Painter.drawImage("img/wall_small.gif", x, y + 32)
                Painter.drawImage("img/wall_small.gif", x, y + 64)

                Painter.drawImage("img/wall_small.gif", x + 96, y + 32)
                Painter.drawImage("img/wall_small.gif", x + 96, y + 64)

                Painter.drawImage("img/camp.gif", x + 32, y + 32)

            }
            else -> {

                //绘制外围的砖块
                Painter.drawImage("img/steel_small.gif", x, y)
                Painter.drawImage("img/steel_small.gif", x + 32, y)
                Painter.drawImage("img/steel_small.gif", x + 64, y)
                Painter.drawImage("img/steel_small.gif", x + 96, y)

                Painter.drawImage("img/steel_small.gif", x, y + 32)
                Painter.drawImage("img/steel_small.gif", x, y + 64)

                Painter.drawImage("img/steel_small.gif", x + 96, y + 32)
                Painter.drawImage("img/steel_small.gif", x + 96, y + 64)

                Painter.drawImage("img/camp.gif", x + 32, y + 32)

            }
        }
    }
    override fun showDestroy(): Array<IView>? {
        return arrayOf(Blast(x - 32, y - 32)
                , Blast(x, y - 32)
                , Blast(x + 32, y - 32)

                , Blast(x - 32, y)
                , Blast(x, y)
                , Blast(x + 32, y)

                , Blast(x - 32, y + 32)
                , Blast(x, y + 32)
                , Blast(x + 32, y + 32))
    }


}