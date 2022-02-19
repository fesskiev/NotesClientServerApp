package com.fesskiev.db

import com.fesskiev.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object UsersTable : Table() {
    val uid = integer("uid").autoIncrement()
    val email = varchar("email", 255)
    val displayName = varchar("displayName", 255)
    val password = varchar("password", 255)
    override val primaryKey = PrimaryKey(uid)
}

suspend fun insertUser(emailValue: String, displayNameValue: String, passwordValue: String): User? =
    DatabaseFactory.dbQuery {
        val insertStatement = UsersTable.insert {
            it[email] = emailValue
            it[displayName] = displayNameValue
            it[password] = passwordValue
        }
        val result = insertStatement.resultedValues?.get(0)
        if (result != null) {
            toUser(result)
        } else {
            null
        }
    }

suspend fun selectUserById(uidValue: Int): User? =
    DatabaseFactory.dbQuery {
        UsersTable.select { UsersTable.uid eq uidValue }
            .map { toUser(it) }
            .firstOrNull()
    }

suspend fun selectUserByEmail(emailValue: String): User? =
    DatabaseFactory.dbQuery {
        UsersTable.select { UsersTable.email eq emailValue }
            .map { toUser(it) }
            .firstOrNull()
    }


private fun toUser(row: ResultRow): User = User(
    row[UsersTable.uid],
    row[UsersTable.email],
    row[UsersTable.displayName],
    row[UsersTable.password],
)
