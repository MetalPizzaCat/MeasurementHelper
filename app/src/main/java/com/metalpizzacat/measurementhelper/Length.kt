package com.metalpizzacat.measurementhelper

import kotlin.math.floor

data class Length(val meters: Double) {
    /**
     * Total amount of inches
     */
    val totalInch: Double = meters * 100.0 / 2.54

    /**
     * Total amount of inches
     */
    val feet : Double =  floor(totalInch / 12.0)

    /**
     * Inch part of the foot inch display
     */
    val inch : Double = totalInch - feet * 12.0

    val cm : Double = meters * 100.0

    constructor(feet : Double, inch : Double) : this((feet * 30.48 + inch * 2.54) / 100.0)
}