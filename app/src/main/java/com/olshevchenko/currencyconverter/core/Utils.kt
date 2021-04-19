package com.olshevchenko.currencyconverter.core

class Utils {

    /**
     * Process currency codes:
     * 1) split 6-character "quotes" key to pair of 3-character currency ("From-", "To-") code
     * 2) back, merge pair of 3-ch currency code into 6-character conversion code (i.e. "quotes" key)
     */
    object CurrencyCodes {

        fun split(key6: String?): List<String>? {
            return key6?.let {
                if (it.length == 6)
                    it.chunked(3) // [0] for "From-", [1] for "To-" currency code
                else
                    null
            }
        }

        fun merge(codeFrom: String?, codeTo: String?): String? {
            if (codeFrom?.length == 3)
                if (codeTo?.length == 3)
                    return codeFrom + codeTo
            return null
        }
    }
}