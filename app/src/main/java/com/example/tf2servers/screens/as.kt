package com.example.tf2servers.screens

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class `as`: Fragment() {
    private val openLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
        try {
            uri?.let { print(it) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}