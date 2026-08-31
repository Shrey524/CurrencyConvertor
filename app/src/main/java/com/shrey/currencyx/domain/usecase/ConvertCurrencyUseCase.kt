package com.shrey.currencyx.domain.usecase

import javax.inject.Inject

/** Converts an amount between currencies using their rates relative to the same base. */
class ConvertCurrencyUseCase @Inject constructor() {
    operator fun invoke(amount: Double, fromRate: Double, toRate: Double): Double =
        amount * (toRate / fromRate)
}
