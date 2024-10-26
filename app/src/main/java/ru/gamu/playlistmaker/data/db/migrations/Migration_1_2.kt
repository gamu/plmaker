package ru.gamu.playlistmaker.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val cursor = database.query("PRAGMA table_info(tracks)")
        var columnExists = false

        while (cursor.moveToNext()) {
            val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            if (columnName == "timestamp") {
                columnExists = true
                break
            }
        }
        cursor.close()

        if (!columnExists){
            database.execSQL("ALTER TABLE tracks ADD COLUMN timestamp INTEGER not null DEFAULT CURRENT_TIMESTAMP")
            database.execSQL("UPDATE tracks SET timestamp = strftime('%s','now') WHERE timestamp IS NULL")
        }
    }
}

