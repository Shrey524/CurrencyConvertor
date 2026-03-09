package com.shrey.currencyx.domain.usecase

import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor() {
    operator fun invoke(amount: Double, fromRate: Double, toRate: Double): Double =
        amount * (toRate / fromRate)
}
