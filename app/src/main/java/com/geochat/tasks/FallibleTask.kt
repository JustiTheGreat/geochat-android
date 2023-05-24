package com.geochat.tasks

interface FallibleTask {
    val errorMessage: String?
}