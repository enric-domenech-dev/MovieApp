package org.lanzadera.proyectos.data.storage.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.lanzadera.proyectos.utils.AppContextProvider

@Database(entities = [RoomFavoriteEntity::class], version = 1, exportSchema = true)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        val instance: FavoritesDatabase by lazy {
            Room.databaseBuilder(
                AppContextProvider.context(),
                FavoritesDatabase::class.java,
                "favorites.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}

