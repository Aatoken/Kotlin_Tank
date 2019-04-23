package com.atoken.game

import com.atoken.game.Config.block
import com.atoken.game.business.*
import com.atoken.game.enums.Direction
import com.atoken.game.model.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import org.itheima.kotlin.game.core.Window
import java.io.BufferedReader
import java.io.InputStreamReader
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
    //游戏是否结束
    private var gameOver: Boolean = false
    //敌方坦克数量
    private var enemyTotalSize = 8
    //敌方坦克显示的多少
    private var enmeyActiveSize = 4
    //敌方的初始点
    private val enemyBornLocation = arrayListOf<Pair<Int, Int>>()
    //出生地点下标
    private var bornIndex = 0


    override fun onCreate() {

        //获取地图文件
        //val file = File(javaClass.getResource("/map/1.map").path)
        val resourceAsStream = javaClass.getResourceAsStream("/map/1.map")
        val reader = BufferedReader(InputStreamReader(resourceAsStream, "utf-8"))
        //获取文件内部的行
        val lines: List<String> = reader.readLines()
        var lineNum = 0
        //循环遍历
        lines.forEach { line ->
            var columnNum = 0
            line.toCharArray().forEach { column ->
                when (column) {
                    '砖' -> views.add(Wall(columnNum * block, lineNum * block))
                    '铁' -> views.add(Steel(columnNum * block, lineNum * block))
                    '水' -> views.add(Water(columnNum * block, lineNum * block))
                    '草' -> views.add(Grass(columnNum * Config.block, lineNum * Config.block))
                    '敌' -> {
                        enemyBornLocation.add(Pair(columnNum * Config.block, lineNum * Config.block))
                        views.add(Enemy(columnNum * Config.block, lineNum * Config.block))
                    }
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
        if (!gameOver) {
            when (event.code) {
                KeyCode.W -> tank.move(Direction.UP)
                KeyCode.A -> tank.move(Direction.LEFT)
                KeyCode.D -> tank.move(Direction.RIGHT)
                KeyCode.S -> tank.move(Direction.DOWN)
                KeyCode.J -> {
                    val bullet = tank.shot()
                    views.add(bullet)
                }
                else -> {
                }
            }
        }

    }

    /**
     * 一直刷新界面
     */
    override fun onRefresh() {

        //业务逻辑

        //先判断是否被销毁再判断游戏是否结束
        /**
         * 检测依据被销毁的移除
         */
        views.filter { it is IDestroyable }.forEach {
            if ((it as IDestroyable).isDestroyed()) {
                views.remove(it)

                //  检测敌方坦克被销毁
                if (it is Enemy) {
                    enemyTotalSize--
                }

                //只有大本营
                val destroy = it.showDestroy()
                destroy?.let {
                    views.addAll(destroy)
                }


            }


        }

        if (gameOver) return

        //检测物体是否发生碰撞
        //1.找到运动的物体
        views.filter { it is IMovable }.forEach { move ->
            move as IMovable
            var badDirection: Direction? = null
            var badBlock: IBlockable? = null

            //2.找到阻碍物 (不要和自己比较)
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
         * 获取具有攻击的
         */
        views.filter { it is IAttackable }.forEach { attack ->
            attack as IAttackable
            //过滤 受攻击能力的(攻击方的源不可以是发射方)
            // 攻击方如果也是受攻击方时，是不可以打自己的
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
        views.filter { it is IAutoShot }.forEach { autoshot ->
            autoshot as IAutoShot

            val shot = autoshot.autoShot()
            shot?.let {
                views.add(shot)
            }

        }

        //判断大本营是否存在 或者敌方坦克是否都消失了
        if (views.none { it is Camp } or
                (enemyTotalSize <= 0) or
                views.none { it is Tank }) {
            gameOver = true
        }


        //去生成新的坦克
        if ((enemyTotalSize - enmeyActiveSize >= 0) and
                (views.filter { it is Enemy }.size < enmeyActiveSize)) {

            //找到坦克的出生地
            val index = bornIndex % enemyBornLocation.size
            val pair = enemyBornLocation[index]
            val born = Born(pair.first, pair.second)

            //生产敌机
            //是否生产
            var show = true
            views.filter { it is IMovable }.forEach bornTag@{ move ->
                move as IMovable
                //println("生产基地:(${born.x},${born.y})")
                //println("坦克的地址:(${move.x},${move.y},${move.currentDirection})")
                //println("是否可以生产${born.isBorn(move)}")
                if (!born.isBorn(move)) {
                    show = false
                    return@bornTag
                }
            }

            if (show) {
                val enemy = Enemy(born.x, born.y)
                views.add(enemy)
                bornIndex++
            }


        }


    }


}
