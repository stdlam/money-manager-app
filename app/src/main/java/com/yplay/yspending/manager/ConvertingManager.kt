package com.yplay.yspending.manager

import java.util.*

object ConvertingManager {

    private const val SPLIT_CHAR = ','

    fun priceFormat(p: Double?) : String {
        p?.let {
            var price = it.toLong().toString().reversed()
            var stop = false
            while (price.length > 3) {
                var start = 0
                if (price.contains(SPLIT_CHAR)) {
                    start = price.lastIndexOf(SPLIT_CHAR) + 1
                }
                for (i in start..price.length - 2) {
                    if (i - start == 2 && price[i + 1] != SPLIT_CHAR) {
                        price = "${price.substring(0, i + 1)}$SPLIT_CHAR${price.substring(i + 1, price.length)}"
                        if (price.subSequence(price.lastIndexOf(SPLIT_CHAR) + 1, price.length).length <= 3) stop = true
                        break
                    }
                }
                if (stop) break
            }
            return "${price.reversed()} ${getCurrencyCode()}"
        }
        return ""
    }

    private fun getCurrencyCode(): String {
        val locale = Locale.getDefault()
        return Currency.getInstance(locale).currencyCode
    }
}