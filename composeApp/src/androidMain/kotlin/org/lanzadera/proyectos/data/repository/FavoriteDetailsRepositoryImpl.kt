package org.lanzadera.proyectos.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.storage.room.FavoriteMovieDao
import org.lanzadera.proyectos.data.storage.room.FavoriteMovieEntity
import org.lanzadera.proyectos.data.storage.room.FavoriteTvShowDao
import org.lanzadera.proyectos.data.storage.room.FavoriteTvShowEntity
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.data.dto.tvshow.SeasonDto
import org.lanzadera.proyectos.data.mapper.toDto
import org.lanzadera.proyectos.data.mapper.toDomain

class FavoriteDetailsRepositoryImpl(
    private val tvShowDao: FavoriteTvShowDao,
    private val movieDao: FavoriteMovieDao,
    private val json: Json
) : FavoriteDetailsRepository {

    override fun observeFavoriteTvShows(): Flow<List<TvShow>> {
        return tvShowDao.observeAll().map { entities ->
            entities.map { it.toTvShow(json) }
        }
    }

    override suspend fun saveFavoriteTvShow(tvShow: TvShow) {
        val entity = tvShow.toEntity(json)
        tvShowDao.upsert(entity)
    }

    override suspend fun removeFavoriteTvShow(tvShowId: String) {
        tvShowDao.deleteById(tvShowId)
    }

    override suspend fun getFavoriteTvShow(tvShowId: String): TvShow? {
        return tvShowDao.getById(tvShowId)?.toTvShow(json)
    }

    override fun observeFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.observeAll().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    override fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>> {
        return movieDao.observeUpcoming(today).map { entities ->
            entities.map { it.toMovie() }
        }
    }

    override suspend fun saveFavoriteMovie(movie: Movie) {
        val entity = movie.toEntity()
        movieDao.upsert(entity)
    }

    override suspend fun removeFavoriteMovie(movieId: String) {
        movieDao.deleteById(movieId)
    }

    override suspend fun getFavoriteMovie(movieId: String): Movie? {
        return movieDao.getById(movieId)?.toMovie()
    }

    private fun TvShow.toEntity(json: Json): FavoriteTvShowEntity {
        val seasonsJson = seasons?.let { 
            // Convert to DTOs for serialization
            val seasonDtos = it.map { season -> season.toDto() }
            json.encodeToString(seasonDtos) 
        }
        return FavoriteTvShowEntity(
            id = id.toString(),
            name = name ?: "",
            originalName = originalName,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            firstAirDate = firstAirDate,
            lastAirDate = lastAirDate,
            numberOfSeasons = numberOfSeasons,
            numberOfEpisodes = numberOfEpisodes,
            status = status,
            inProduction = inProduction,
            voteAverage = voteAverageDouble(),
            voteCount = voteCount,
            popularity = popularity,
            addedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            seasonsJson = seasonsJson
        )
    }

    private fun FavoriteTvShowEntity.toTvShow(json: Json): TvShow {
        val seasons = seasonsJson?.let {
            try {
                // Deserialize as DTOs, then convert to domain
                val seasonDtos = json.decodeFromString<List<SeasonDto>>(it)
                seasonDtos.map { dto -> dto.toDomain() }
            } catch (e: Exception) {
                null
            }
        }
        return TvShow(
            id = id.toIntOrNull(),
            name = name,
            originalName = originalName,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            firstAirDate = firstAirDate,
            lastAirDate = lastAirDate,
            numberOfSeasons = numberOfSeasons,
            numberOfEpisodes = numberOfEpisodes,
            status = status,
            inProduction = inProduction,
            voteAverageDouble = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            seasons = seasons
        )
    }

    private fun Movie.toEntity(): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = id.toString(),
            title = title ?: "",
            originalTitle = originalTitle,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            runtime = runtime,
            status = status,
            voteAverage = voteAverageDouble(),
            voteCount = voteCount,
            popularity = popularity,
            budget = budget,
            revenue = revenue,
            addedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun FavoriteMovieEntity.toMovie(): Movie {
        return Movie(
            id = id.toIntOrNull(),
            title = title,
            originalTitle = originalTitle,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            runtime = runtime,
            status = status,
            voteAverageDouble = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            budget = budget,
            revenue = revenue
        )
    }

    private fun TvShow.voteAverageDouble(): Double? = try {
        voteAverage.toDoubleOrNull()
    } catch (e: Exception) {
        null
    }

    private fun Movie.voteAverageDouble(): Double? = try {
        voteAverage.toDoubleOrNull()
    } catch (e: Exception) {
        null
    }
}
