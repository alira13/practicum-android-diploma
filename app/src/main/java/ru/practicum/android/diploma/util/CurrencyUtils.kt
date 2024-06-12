package ru.practicum.android.diploma.util

private const val RUR = "RUR"
private const val USD = "USD"
private const val EUR = "EUR"
private const val KZT = "KZT"
private const val KGS = "KGS"
private const val BYR = "BYR"
private const val UAH = "UAH"
private const val AZN = "AZN"
private const val GEL = "GEL"
fun currencyUTF(currency: String?): String? {
    return when (currency) {
        RUR -> "\u20BD"
        USD -> "\u0024"
        EUR -> "\u20AC"
        KZT -> "\u20B8"
        KGS -> "с"
        BYR -> "p."
        UAH -> "\u20B4"
        AZN -> "\u20BC"
        GEL -> "₾"
        null -> null
        else -> currency
    }
}

