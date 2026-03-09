package com.shrey.currencyx.domain.model

data class Currency(
    val code: String,
    val name: String,
    val flagEmoji: String
) {
    companion object {
        private val allCurrencies = listOf(
            Currency("USD", "US Dollar", "🇺🇸"),
            Currency("EUR", "Euro", "🇪🇺"),
            Currency("GBP", "British Pound", "🇬🇧"),
            Currency("INR", "Indian Rupee", "🇮🇳"),
            Currency("JPY", "Japanese Yen", "🇯🇵"),
            Currency("AUD", "Australian Dollar", "🇦🇺"),
            Currency("CAD", "Canadian Dollar", "🇨🇦"),
            Currency("CHF", "Swiss Franc", "🇨🇭"),
            Currency("CNY", "Chinese Yuan", "🇨🇳"),
            Currency("HKD", "Hong Kong Dollar", "🇭🇰"),
            Currency("SGD", "Singapore Dollar", "🇸🇬"),
            Currency("VND", "Vietnamese Dong", "🇻🇳"),
            Currency("KRW", "South Korean Won", "🇰🇷"),
            Currency("MXN", "Mexican Peso", "🇲🇽"),
            Currency("BRL", "Brazilian Real", "🇧🇷"),
            Currency("ZAR", "South African Rand", "🇿🇦"),
            Currency("THB", "Thai Baht", "🇹🇭"),
            Currency("MYR", "Malaysian Ringgit", "🇲🇾"),
            Currency("PHP", "Philippine Peso", "🇵🇭"),
            Currency("IDR", "Indonesian Rupiah", "🇮🇩"),
            Currency("NZD", "New Zealand Dollar", "🇳🇿"),
            Currency("SEK", "Swedish Krona", "🇸🇪"),
            Currency("NOK", "Norwegian Krone", "🇳🇴"),
            Currency("DKK", "Danish Krone", "🇩🇰"),
            Currency("PLN", "Polish Zloty", "🇵🇱"),
            Currency("TRY", "Turkish Lira", "🇹🇷"),
            Currency("ILS", "Israeli Shekel", "🇮🇱"),
            Currency("CZK", "Czech Koruna", "🇨🇿"),
            Currency("HUF", "Hungarian Forint", "🇭🇺"),
            Currency("RON", "Romanian Leu", "🇷🇴"),
            Currency("BGN", "Bulgarian Lev", "🇧🇬"),
            Currency("ISK", "Icelandic Krona", "🇮🇸")
        )

        // Frankfurter API supported currencies (31 ECB currencies)
        private val frankfurterSupported = setOf(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK",
            "EUR", "GBP", "HKD", "HUF", "IDR", "ILS", "INR", "ISK",
            "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
            "RON", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
        )

        fun getAll(): List<Currency> = allCurrencies

        fun getFrankfurterSupported(): List<Currency> =
            allCurrencies.filter { it.code in frankfurterSupported }

        fun getDefault(code: String): Currency =
            allCurrencies.find { it.code == code } ?: allCurrencies.first()

        fun getByCode(code: String): Currency? =
            allCurrencies.find { it.code == code }
    }
}
