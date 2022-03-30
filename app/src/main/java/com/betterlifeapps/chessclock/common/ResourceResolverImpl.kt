package com.betterlifeapps.chessclock.common

import android.content.Context
import androidx.annotation.StringRes
import com.betterlifeapps.std.ResourceResolver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceResolverImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ResourceResolver {
    override fun getString(resId: Int): String =
        context.resources.getString(resId)

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        context.resources.getString(resId, *formatArgs)
}