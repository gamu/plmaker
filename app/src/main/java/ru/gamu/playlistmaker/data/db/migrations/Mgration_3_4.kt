package ru.gamu.playlistmaker.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""CREATE TABLE playlist (
            playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            title TEXT NOT NULL,
            coverUri TEXT,
            description TEXT NOT NULL,
            tracks TEXT NOT NULL
        );""")
    }
}