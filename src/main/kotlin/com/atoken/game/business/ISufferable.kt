package com.atoken.game.business

import com.atoken.game.model.IView

/**
 * Auther Aatoken
 * Date 2019/4/20
 * Des 遭受攻击的接口
 */
interface ISufferable : IView {

    /**
     * 生命值
     */
    val blood: Int

    /**
     * 被打击后的视图显示
     */
    fun notifySuffer(attackable: IAttackable): Array<IView>?



}