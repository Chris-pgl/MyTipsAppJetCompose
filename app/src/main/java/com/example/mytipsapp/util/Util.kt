package com.example.mytipsapp.util

import androidx.collection.buildIntFloatMap

fun calculateTotalTip(totalBill: Double, tiPercentage: Int):Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tiPercentage) / 100 else 0.0
}

fun calculateTotalPerson(totalBill: Double, splitBy: Int, tiPercentage: Int): Double{
    val bill = calculateTotalTip(totalBill = totalBill, tiPercentage = tiPercentage) + totalBill
    return (bill/splitBy)
}

