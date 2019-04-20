package com.atoken.game.business

import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des 遭受攻击的接口
 */
interface Sufferable : IView {

    /**
     * 生命值
     */
    val blood: Int

    fun notifySuffer(attackable: IAttackable): Array<IView>?



}