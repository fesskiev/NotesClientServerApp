package com.fesskiev.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * https://github.com/JetBrains/Exposed
 * http://h2database.com/html/tutorial.html#android
 */
object DatabaseFactory {

    fun init(jdbcUrl: String) {
        Database.connect(hikari(jdbcUrl))
        transaction {
            create(NotesTable)
            create(UsersTable)
        }
    }

    private fun hikari(jdbcUrl: String): HikariDataSource {
        val config = HikariConfig()
        with(config) {
            this.jdbcUrl = jdbcUrl
            driverClassName = "org.h2.Driver"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
}
