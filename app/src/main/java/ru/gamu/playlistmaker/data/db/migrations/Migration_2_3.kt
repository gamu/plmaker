package ru.gamu.playlistmaker.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase){
        val ddlTempTracks= """create table tracks_new
            (
                trackId     INTEGER                           not null primary key,
                fileUrl     TEXT                              not null,
                coverUrl    TEXT                              not null,
                trackName   TEXT                              not null,
                artistName  TEXT                              not null,
                albumName   TEXT                              not null,
                releaseYear TEXT                              not null,
                genre       TEXT                              not null,
                country     TEXT                              not null,
                duration    TEXT                              not null,
                timestamp   INTEGER default CURRENT_TIMESTAMP not null
        )"""
        database.execSQL(ddlTempTracks)
        database.execSQL("INSERT INTO tracks_new SELECT * FROM tracks")
        database.execSQL("DROP TABLE tracks")
        database.execSQL("ALTER TABLE tracks_new RENAME TO tracks")
        database.execSQL("UPDATE tracks SET releaseYear = '1970-01-01T00:00:00Z'")
    }
}