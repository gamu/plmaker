package ru.gamu.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gamu.playlistmaker.data.db.dao.PlaylistFacade
import ru.gamu.playlistmaker.data.db.dao.TracksFacade
import ru.gamu.playlistmaker.data.db.entities.PlaylistItem
import ru.gamu.playlistmaker.data.db.entities.TrackEntity

@Database(entities = [TrackEntity::class, PlaylistItem::class],
    version = 4, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksFacade(): TracksFacade
    abstract fun playlistFacade(): PlaylistFacade
}