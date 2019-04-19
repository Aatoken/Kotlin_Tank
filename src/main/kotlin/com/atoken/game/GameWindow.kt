package com.atoken.game

import com.atoken.game.business.IBlockable
import com.atoken.game.business.IMovable
import com.atoken.game.enums.Direction
import com.atoken.game.model.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Window
import java.io.File


/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 坦克大战游戏的窗体
 */
class GameWindow : Window(title = "坦克大战1.0"
        , icon = "img/logo.jpg"
        , width = Config.gameWidth
        , height = Config.gameHeight) {

    private val views = arrayListOf<IView>()
    private lateinit var tank: Tank
    override fun onCreate() {

        //获取地图文件
        val file = File(javaClass.getResource("/map/1.map").path)
        //获取文件内部的行
        val lines: List<String> = file.readLines()
        var lineNum = 0
        //循环遍历
        lines.forEach { line ->
            var columnNum = 0
            line.toCharArray().forEach { column ->
                when (column) {
                    '砖' -> views.add(Wall(columnNum * Config.block, lineNum * Config.block))
                    '铁' -> views.add(Steel(columnNum * Config.block, lineNum * Config.block))
                    '水' -> views.add(Water(columnNum * Config.block, lineNum * Config.block))
                    '草' -> views.add(Grass(columnNum * Config.block, lineNum * Config.block))
                }
                columnNum++
            }
            lineNum++
        }

        //添加我方坦克
        tank = Tank(Config.block * 3, Config.block * 9)
        views.add(tank)


    }

    override fun onDisplay() {

        //绘画出所有的物体
        views.forEach {
            it.draw()
        }


    }

    override fun onKeyPressed(event: KeyEvent) {

        when (event.code) {
            KeyCode.W -> tank.move(Direction.UP)
            KeyCode.A -> tank.move(Direction.LEFT)
            KeyCode.D -> tank.move(Direction.RIGHT)
            KeyCode.S -> tank.move(Direction.DOWN)
            KeyCode.J -> {
                val bullet = tank.shot()
                views.add(bullet)
            }
        }


    }

    override fun onRefresh() {

        //业务逻辑

        //检测物体是否发生碰撞
        //1.找到运动的物体
        views.filter { it is IMovable }.forEach { move ->
            move as IMovable
            var badDirection: Direction? = null
            var badBlock: IBlockable? = null

            //2.找到阻碍物
            views.filter { it is IBlockable }.forEach blockTag@{ block ->
                //3.检测是否发生碰撞
                block as IBlockable
                // 获得碰撞的方向
                val direction = move.willCollision(block)
                direction?.let {
                    //碰到阻碍物，跳出循环
                    badDirection = direction
                    badBlock = block
                    return@blockTag
                }

            }

            //找到和move碰撞的block，找到会碰撞的方向
            //通知可以移动的物体，会在哪个方向和哪个物体碰撞
            move.notifyCollision(badDirection, badBlock)
        }


    }


}
