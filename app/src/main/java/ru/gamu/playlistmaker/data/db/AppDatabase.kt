package ru.gamu.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gamu.playlistmaker.data.db.dao.TracksFacade
import ru.gamu.playlistmaker.data.db.entities.TrackEntity

@Database(entities = [TrackEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksFacade(): TracksFacade
}