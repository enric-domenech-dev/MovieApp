package org.lanzadera.proyectos.data.storage.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.lanzadera.proyectos.utils.AppContextProvider

@Database(
    entities = [
        RoomFavoriteEntity::class,
        WatchedEpisodeEntity::class,
        WatchedMovieEntity::class,
        FavoriteTvShowEntity::class,
        FavoriteMovieEntity::class,
        SettingsEntity::class
    ],
    version = 5,
    exportSchema = true
)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun watchedEpisodeDao(): WatchedEpisodeDao
    abstract fun watchedMovieDao(): WatchedMovieDao
    abstract fun favoriteTvShowDao(): FavoriteTvShowDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun settingsDao(): SettingsDao

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

