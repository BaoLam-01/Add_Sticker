package vn.tapbi.sample2021kotlin.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object MyFormatter {
    public fun String.toMyDouble(): Double {
        if (this.length == 1 && this[0] == '-') {
            return 0.0;
        }
        if (this.isNotEmpty()) {
            return this.toDouble()
        }
        return 0.0
    }
    fun doubleToString2(d: Double,numberFaction:Int): String {
        val symbols = DecimalFormatSymbols.getInstance(Locale.ITALIAN)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat()
        formatter.maximumFractionDigits = numberFaction
        formatter.minimumFractionDigits = numberFaction
        formatter.isGroupingUsed = true
        return formatter.format(d)
    }
    fun doubleToString3(d: Double,max:Int): String {

        val formatter = DecimalFormat()
        val symbols = formatter.decimalFormatSymbols
        val decimal = symbols.decimalSeparator
        val group = symbols.groupingSeparator
        formatter.maximumFractionDigits = max
        formatter.isGroupingUsed = true
        return formatter.format(d).replace(decimal,'^').replace(group,'.').replace('^',',')
    }
    public fun doubleToString(value: Double): String {
        val symbols = DecimalFormatSymbols.getInstance(Locale.ITALIAN)
        val df = DecimalFormat("#,###,###.######", symbols)
        // df.roundingMode = RoundingMode.HALF_UP
        return df.format(value).trim()
    }
}