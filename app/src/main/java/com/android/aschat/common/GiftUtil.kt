package com.android.aschat.common

import com.android.aschat.R

/**
 * 礼物枚举
 */
enum class Gift {
    biao,
    bieshu,
    chungao,
    chuang,
    erhuan,
    feiji,
    youlun,
    huangguan,
    hunsha,
    jiezi,
    kiss,
    meigui,
    paoche,
    qiaokeli,
    qinggua,
    rose,
    shouzhuo,
    shuijingxie,
    xiangbing,
    xiangjiao,
    xiangshui,
    xiong,
    yongyi,
    xianglian,
    chengbao,
    aimashibao
}

fun giftStr2Gift(giftStr: String): Gift {
    return when (giftStr) {
        "biao" -> Gift.biao
        "bieshu" -> Gift.bieshu
        "chungao" -> Gift.chungao
        "chuang" -> Gift.chuang
        "erhuan" -> Gift.erhuan
        "feiji" -> Gift.feiji
        "youlun" -> Gift.youlun
        "huangguan" -> Gift.huangguan
        "hunsha" -> Gift.hunsha
        "jiezi" -> Gift.jiezi
        "kiss" -> Gift.kiss
        "meigui" -> Gift.meigui
        "paoche" -> Gift.paoche
        "quaikeli" -> Gift.qiaokeli
        "qinggua" -> Gift.qinggua
        "rose" -> Gift.rose
        "shouzhuo" -> Gift.shouzhuo
        "shuijingxie" -> Gift.shuijingxie
        "xiangbing" -> Gift.xiangbing
        "xiangjiao" -> Gift.xiangjiao
        "xiangshui" -> Gift.xiangshui
        "xiong" -> Gift.xiong
        "yongyi" -> Gift.yongyi
        "xianglian" -> Gift.xianglian
        "chengbao" -> Gift.chengbao
        "aimashibao" -> Gift.aimashibao
        else -> Gift.biao
    }
}

fun gift2ImageId(gift: Gift): Int {
    return when (gift) {
        Gift.biao -> R.mipmap.watches
        Gift.bieshu -> R.mipmap.villa
        Gift.chungao -> R.mipmap.lipstick
        Gift.chuang -> R.mipmap.bed
        Gift.erhuan -> R.mipmap.earrings
        Gift.feiji -> R.mipmap.helicopter
        Gift.youlun -> R.mipmap.cruise_ship
        Gift.huangguan -> R.mipmap.crown
        Gift.hunsha -> R.mipmap.wedding_dress
        Gift.jiezi -> R.mipmap.diamond_ring
        Gift.kiss -> R.mipmap.kiss
        Gift.meigui -> R.mipmap.rose
        Gift.paoche -> R.mipmap.car
        Gift.qiaokeli -> R.mipmap.chocolate
        Gift.qinggua -> R.mipmap.cucumber
        Gift.rose -> R.mipmap.rose
        Gift.shouzhuo -> R.mipmap.bracelet
        Gift.shuijingxie -> R.mipmap.glass_shoes
        Gift.xiangbing -> R.mipmap.champagne
        Gift.xiangjiao -> R.mipmap.banana
        Gift.xiangshui -> R.mipmap.perfume
        Gift.xiong -> R.mipmap.teddy_bear
        Gift.yongyi -> R.mipmap.bikini
        Gift.xianglian -> R.mipmap.necklace
        Gift.chengbao -> R.mipmap.fortress
        Gift.aimashibao -> R.mipmap.bag
    }
}

/**
 * 往map中存入礼物和数量的关系
 */
fun giftAndNum2Gift(map: MutableMap<Gift, String>, giftAndNum: String) {
    val list = giftAndNum.split(":")
    if (list.size != 2) return
    val gift = giftStr2Gift(list[0])
    map[gift] = list[1]
}