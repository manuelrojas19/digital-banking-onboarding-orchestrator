package com.manuelr.banking.digital.onboarding.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun launchAsync(function: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.IO) {
            function()
        }
    }
}