package com.fesskiev.compose.data.local

import android.content.Context

import com.fesskiev.compose.SqlDelight
import com.squareup.sqldelight.android.AndroidSqliteDriver

fun provideSqlDelight(context: Context): SqlDelight {
    val driver = AndroidSqliteDriver(SqlDelight.Schema, context, "database.db")
    return SqlDelight(driver)
}