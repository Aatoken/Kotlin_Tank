package com.atoken.game

import com.atoken.game.business.*
import com.atoken.game.enums.Direction
import com.atoken.game.model.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Window
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Auther Aatoken
 * Date 2019/4/19
 * Des 坦克大战游戏的窗体
 */
class GameWindow : Window(title = "坦克大战1.0"
        , icon = "img/logo.jpg"
        , width = Config.gameWidth
        , height = Config.gameHeight) {

    //private val views = arrayListOf<IView>()
    //线程安全集合
    private val views = CopyOnWriteArrayList<IView>()
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
                    '敌' -> views.add(Enemy(columnNum * Config.block, lineNum * Config.block))
                }
                columnNum++
            }
            lineNum++
        }

        //添加我方坦克
        tank = Tank(Config.block * 3, Config.block * 9)
        views.add(tank)

        //添加大本营
        //添加大本营
        views.add(Camp(Config.gameWidth / 2 - Config.block, Config.gameHeight - 96))
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

    /**
     * 一直刷新界面
     */
    override fun onRefresh() {

        //业务逻辑

        //检测物体是否发生碰撞
        //1.找到运动的物体
        views.filter { it is IMovable }.forEach { move ->
            move as IMovable
            var badDirection: Direction? = null
            var badBlock: IBlockable? = null

            //2.找到阻碍物
            views.filter { (it is IBlockable) and (move != it) }
                    .forEach blockTag@{ block ->
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

        /**
         * 检测自动移动能力的物体，让他们自己动起来
         */
        views.filter { it is IAutoMovable }.forEach {
            (it as IAutoMovable).autoMove()
        }


        /**
         * 检测依据被销毁的移除
         */
        views.filter { it is IDestroyable }.forEach {
            if ((it as IDestroyable).isDestroyed()) {
                views.remove(it)
            }
        }


        /**
         * 获取具有攻击的
         */
        views.filter { it is IAttackable }.forEach { attack ->
            attack as IAttackable

            views.filter { (it is ISufferable) and (attack != it) and (attack.owner != it) }
                    .forEach sufferTag@{ suffer ->
                        suffer as ISufferable

                        //判断子弹是否打到被打击物体
                        if (attack.isCollision(suffer)) {
                            //提示子弹的刷新
                            attack.notifyAttack(suffer)
                            //通知被打攻击者，产生碰撞
                            val sufferView = suffer.notifySuffer(attack)
                            sufferView?.let {
                                //显示挨打的效果
                                views.addAll(sufferView)
                            }
                            return@sufferTag
                        }
                    }

        }

        /**
         * 自动射击
         */
        views.filter { it is IAutoShot }.forEach { autoshot->
            autoshot as IAutoShot

           val shot=autoshot.autoShot()
            shot?.let {
                views.add(shot)
            }

        }



    }


}
