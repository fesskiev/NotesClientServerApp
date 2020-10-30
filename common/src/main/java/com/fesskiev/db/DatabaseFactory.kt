package com.fesskiev.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(url: String) {
        Database.connect(url = url, driver = "org.h2.Driver")
        transaction {
            addLogger(StdOutSqlLogger)
            create(NotesTable)
            create(UsersTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
}
