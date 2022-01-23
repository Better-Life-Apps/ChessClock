package com.betterlifeapps.chessclock.ui

import android.os.Bundle
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.std.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}