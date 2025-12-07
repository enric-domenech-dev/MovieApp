package org.lanzadera.proyectos.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    /**
     * Zona horaria de TMDB (Pacific Time)
     * Las fechas de release/air_date en TMDB están en PT (Los Angeles)
     */
    private val TMDB_TIMEZONE = TimeZone.of("America/Los_Angeles")

    /**
     * Meses en español abreviados
     */
    private val SPANISH_MONTHS = mapOf(
        Month.JANUARY to "Ene",
        Month.FEBRUARY to "Feb",
        Month.MARCH to "Mar",
        Month.APRIL to "Abr",
        Month.MAY to "May",
        Month.JUNE to "Jun",
        Month.JULY to "Jul",
        Month.AUGUST to "Ago",
        Month.SEPTEMBER to "Sep",
        Month.OCTOBER to "Oct",
        Month.NOVEMBER to "Nov",
        Month.DECEMBER to "Dic"
    )

    /**
     * Obtiene la fecha actual en la zona horaria de TMDB
     * Esto asegura que comparemos correctamente con las fechas de TMDB
     */
    fun getTodayInTMDBTimezone(): LocalDate {
        return Clock.System.now()
            .toLocalDateTime(TMDB_TIMEZONE)
            .date
    }

    /**
     * Obtiene la fecha actual en la zona horaria del usuario
     */
    fun getTodayInUserTimezone(): LocalDate {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    /**
     * Verifica si una fecha (string en formato "YYYY-MM-DD") ya ha pasado
     * considerando que el contenido en TMDB se emite en horario nocturno de LA
     * (típicamente 8-11 PM PT = madrugada del día siguiente en Europa)
     *
     * Para episodios que se emiten "hoy" en LA, en España será mañana en la madrugada.
     * Por eso comparamos con la fecha del usuario y restamos 1 día si estamos
     * comparando contenido que se emite el mismo día en LA.
     *
     * @param dateString Fecha en formato "YYYY-MM-DD"
     * @return true si la fecha ya ha pasado completamente, false si es futura o hoy
     */
    fun hasDatePassed(dateString: String?): Boolean {
        if (dateString.isNullOrBlank()) return true

        return try {
            val targetDate = parseDate(dateString) ?: return true
            val userToday = getTodayInUserTimezone()

            // El contenido de TMDB se emite en horario nocturno de LA (8-11 PM)
            // Para usuarios fuera de PT (ej: España), esto significa madrugada del día siguiente
            // Por eso, consideramos que el contenido está disponible solo si la fecha
            // de emisión es ANTERIOR a hoy (no igual)
            targetDate < userToday
        } catch (e: Exception) {
            true // En caso de error, asumir que ha pasado
        }
    }

    /**
     * Calcula los días hasta una fecha desde hoy (en zona horaria del usuario)
     *
     * @param dateString Fecha en formato "YYYY-MM-DD"
     * @param adjustForTimezone Si true, suma 1 día a la fecha para reflejar emisión real en Europa
     * @return Días hasta la fecha, null si la fecha no es válida
     */
    fun daysUntilDate(dateString: String?, adjustForTimezone: Boolean = false): Int? {
        if (dateString.isNullOrBlank()) return null

        return try {
            val targetDate = parseDate(dateString) ?: return null
            val adjustedDate = if (adjustForTimezone) {
                targetDate.plus(1, DateTimeUnit.DAY)
            } else {
                targetDate
            }
            val today = getTodayInUserTimezone()
            (adjustedDate.toEpochDays() - today.toEpochDays()).toInt()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parsea una fecha en formato "YYYY-MM-DD" a LocalDate
     *
     * @param dateString Fecha en formato "YYYY-MM-DD"
     * @return LocalDate o null si no es válida
     */
    fun parseDate(dateString: String?): LocalDate? {
        if (dateString.isNullOrBlank()) return null

        return try {
            val parts = dateString.split("-")
            if (parts.size != 3) return null

            LocalDate(
                year = parts[0].toInt(),
                monthNumber = parts[1].toInt(),
                dayOfMonth = parts[2].toInt()
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formatea una fecha a un formato amigable en español
     * Ejemplos: "30 Ago 2025", "15 Dic 2024"
     *
     * @param dateString Fecha en formato "YYYY-MM-DD"
     * @param adjustForTimezone Si true, suma 1 día para reflejar la emisión real en Europa
     * @return Fecha formateada o null si no es válida
     */
    fun formatDateFriendly(dateString: String?, adjustForTimezone: Boolean = false): String? {
        val date = parseDate(dateString) ?: return null
        val adjustedDate = if (adjustForTimezone) {
            date.plus(1, DateTimeUnit.DAY)
        } else {
            date
        }

        return try {
            val day = adjustedDate.dayOfMonth
            val month = SPANISH_MONTHS[adjustedDate.month] ?: adjustedDate.month.name.take(3)
            val year = adjustedDate.year

            "$day $month $year"
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formatea una fecha a formato corto con año
     * Ejemplo: "30 Ago 2025"
     *
     * @param dateString Fecha en formato "YYYY-MM-DD"
     * @param adjustForTimezone Si true, suma 1 día para reflejar la emisión real en Europa
     * @return Fecha formateada o null si no es válida
     */
    fun formatDateShort(dateString: String?, adjustForTimezone: Boolean = false): String? {
        val date = parseDate(dateString) ?: return null
        val adjustedDate = if (adjustForTimezone) {
            date.plus(1, DateTimeUnit.DAY)
        } else {
            date
        }

        return try {
            val day = adjustedDate.dayOfMonth
            val month = SPANISH_MONTHS[adjustedDate.month] ?: adjustedDate.month.name.take(3)
            val year = adjustedDate.year

            "$day $month $year"
        } catch (e: Exception) {
            null
        }
    }
}
