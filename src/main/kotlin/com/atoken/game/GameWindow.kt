package com.atoken.game

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

    private val views = arrayListOf<View>()
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

        }
    }

    override fun onRefresh() {
    }


}
