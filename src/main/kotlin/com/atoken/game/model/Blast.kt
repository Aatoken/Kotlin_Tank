package com.atoken.game.model

import com.atoken.game.Config
import com.atoken.game.business.IDestroyable
import org.itheima.kotlin.game.core.Painter


/**
 * Auther Aatoken
 * Date 2019/4/21
 * Des 爆炸物
 */
class Blast(override val x: Int, override val y: Int) :IDestroyable{


    override val width: Int=Config.block

    override val height: Int=Config.block
    //定义爆炸物的索引值
    private var index: Int = 0
    //所有图片的集合
    private val imagePaths = arrayListOf<String>()

    init {
        (1..32).forEach{
            imagePaths.add("img/blast_$it.png")
        }
    }

    override fun draw() {
        //获取当前的位置
        val i = index % imagePaths.size
        Painter.drawImage(imagePaths[i], x, y)
        index++
    }
    override fun isDestroyed(): Boolean {
        return index>imagePaths.size
    }

}