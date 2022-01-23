package com.betterlifeapps.chessclock.common

import android.content.Context
import com.betterlifeapps.std.common.BaseDialogManager
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DialogManager @Inject constructor(@ActivityContext private val context: Context) :
    BaseDialogManager(context)