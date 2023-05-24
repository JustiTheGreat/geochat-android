package com.geochat.ui.fragments

interface ICallbackContext {
    fun callback(caller: Any?, result: Any?)
    fun timedOut(caller: Any?)
}