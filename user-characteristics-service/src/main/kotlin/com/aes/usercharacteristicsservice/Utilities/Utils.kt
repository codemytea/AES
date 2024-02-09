package com.aes.usercharacteristicsservice.Utilities

import kotlin.math.exp

object Utils {

    /** Gives a sigmoid function in the form of a logistic curve to scale the confidence probability
     * and calculates it.
     * Returns number between 0 and 1 where 0 is 'low' and 1 is 'high'
     * This can be inversed by setting inverse result to true
     * */
    fun scaleProbability(actual: Double, bound: Double, inverseResult: Boolean = false) : Double {
        if (inverseResult) return 1.0-(1.0/(1.0+ exp(-0.01*(actual-bound))))
        return 1.0/(1.0+ exp(-0.01*(actual-bound)))

    }
}