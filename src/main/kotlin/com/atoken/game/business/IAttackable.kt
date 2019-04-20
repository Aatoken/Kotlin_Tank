package com.atoken.game.business

import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des 具备攻击的能力
 */
interface IAttackable:IView {
    /**
     * 所有者
     */
    val owner: IView

    /**
     * 攻击力
     */
    val attackPower: Int

    //判断是否碰撞
    fun isCollision(sufferable: Sufferable): Boolean

    fun notifyAttack(sufferable: Sufferable)
}