package com.olshevchenko.currencyconverter.core

interface BaseMapper<in A, out B> {

    fun map(type: A): B
}

interface BaseParamMapper<in A, out B, in Params> {

    fun map(type: A, params: Params): B
}