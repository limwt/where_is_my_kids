package com.jeff.lim.wimk.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}