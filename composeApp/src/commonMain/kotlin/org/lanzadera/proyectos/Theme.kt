package org.lanzadera.proyectos

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val FilmWayLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF8C42),             // Cinematic Amber
    onPrimary = Color(0xFF3C1E1E),           // Deep Maroon
    primaryContainer = Color(0xFFFFC176),    // Golden Hour
    onPrimaryContainer = Color(0xFF3C1E1E),

    secondary = Color(0xFF6B4E34),           // Café Reel
    onSecondary = Color(0xFFFFE9D6),
    secondaryContainer = Color(0xFFB7A99A),  // Film Dust
    onSecondaryContainer = Color(0xFF3C1E1E),

    tertiary = Color(0xFFF5A7A0),            // Rose Fade
    onTertiary = Color(0xFF3C1E1E),
    tertiaryContainer = Color(0xFFFFDFAE),   // Honey Light
    onTertiaryContainer = Color(0xFF3C1E1E),

    background = Color(0xFFFAF4EE),          // Cream Frame
    onBackground = Color(0xFF3C1E1E),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF3C1E1E),

    surfaceVariant = Color(0xFFB7A99A),      // Film Dust
    onSurfaceVariant = Color(0xFF3C1E1E),

    surfaceTint = Color(0xFFFF8C42),
    inverseSurface = Color(0xFF3C1E1E),
    inverseOnSurface = Color(0xFFFFE9D6),

    error = Color(0xFFE05C2F),               // Burnt Orange (error cálido)
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFC1A6),
    onErrorContainer = Color(0xFF3C1E1E),

    outline = Color(0xFF8B7E75),             // Warm Gray
    outlineVariant = Color(0xFFD6C8BC),
    scrim = Color(0x33000000),

    surfaceBright = Color(0xFFFAF4EE),
    surfaceDim = Color(0xFFF1E9E1),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAF4EE),
    surfaceContainer = Color(0xFFF5EDE4),
    surfaceContainerHigh = Color(0xFFEDE3DA),
    surfaceContainerHighest = Color(0xFFE5DACF)
)

val FilmWayDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC176),            // Golden Hour — luz cálida en la oscuridad
    onPrimary = Color(0xFF3C1E1E),          // Deep Maroon
    primaryContainer = Color(0xFFE05C2F),   // Burnt Orange — energía emocional
    onPrimaryContainer = Color(0xFFFFE9D6),

    secondary = Color(0xFFB7A99A),          // Film Dust — neutro cálido
    onSecondary = Color(0xFF1E1B1A),
    secondaryContainer = Color(0xFF6B4E34), // Café Reel — textura de película
    onSecondaryContainer = Color(0xFFFFE9D6),

    tertiary = Color(0xFFF5A7A0),           // Rose Fade — acento emocional
    onTertiary = Color(0xFF1E1B1A),
    tertiaryContainer = Color(0xFF854B3B),
    onTertiaryContainer = Color(0xFFFFE9D6),

    background = Color(0xFF1E1B1A),         // Indigo Shadow — sala de cine profunda
    onBackground = Color(0xFFFFE9D6),       // Texto cálido y legible

    surface = Color(0xFF262220),            // Paneles y contenedores
    onSurface = Color(0xFFFFE9D6),

    surfaceVariant = Color(0xFF6B4E34),
    onSurfaceVariant = Color(0xFFFFE9D6),

    surfaceTint = Color(0xFFFFC176),
    inverseSurface = Color(0xFFFFE9D6),
    inverseOnSurface = Color(0xFF1E1B1A),

    error = Color(0xFFE05C2F),
    onError = Color(0xFFFFE9D6),
    errorContainer = Color(0xFF662C20),
    onErrorContainer = Color(0xFFFFE9D6),

    outline = Color(0xFF8B7E75),
    outlineVariant = Color(0xFF5F524B),
    scrim = Color(0x66000000),

    surfaceBright = Color(0xFF2D2826),
    surfaceDim = Color(0xFF1A1716),
    surfaceContainerLowest = Color(0xFF141210),
    surfaceContainerLow = Color(0xFF1E1B1A),
    surfaceContainer = Color(0xFF262220),
    surfaceContainerHigh = Color(0xFF2F2A28),
    surfaceContainerHighest = Color(0xFF383330)
)


val NewLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF8C42),
    onPrimary = Color(0xFF3C1E1E),
    primaryContainer = Color(0xFFFFC176),
    onPrimaryContainer = Color(0xFF3C1E1E),
    secondary = Color(0xFF6B4E34),
    onSecondary = Color(0xFFFAF4EE),
    background = Color(0xFFFAF4EE),
    onBackground = Color(0xFF3C1E1E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF3C1E1E),
    surfaceVariant = Color(0xFFB7A99A),
    onSurfaceVariant = Color(0xFF3C1E1E),
    outline = Color(0xFF8B7E75)
)

val NewDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC176),
    onPrimary = Color(0xFF3C1E1E),
    primaryContainer = Color(0xFFE05C2F),
    onPrimaryContainer = Color(0xFFFFE9D6),
    secondary = Color(0xFFB7A99A),
    onSecondary = Color(0xFF1E1B1A),
    background = Color(0xFF1E1B1A),
    onBackground = Color(0xFFFFE9D6),
    surface = Color(0xFF262220),
    onSurface = Color(0xFFFFE9D6),
    surfaceVariant = Color(0xFF6B4E34),
    onSurfaceVariant = Color(0xFFFFE9D6),
    outline = Color(0xFF8B7E75)
)


val FilmDarkColorPalette = darkColorScheme(
    primary = Sunset80,
    onPrimary = Sunset10,
    primaryContainer = Sunset30,
    onPrimaryContainer = Sunset90,

    secondary = Gold80,
    onSecondary = Gold10,
    secondaryContainer = Gold30,
    onSecondaryContainer = Gold90,

    tertiary = FilmBrown80,
    onTertiary = FilmBrown10,
    tertiaryContainer = FilmBrown30,
    onTertiaryContainer = FilmBrown90,

    background = ShadowBlack,
    onBackground = CreamWhite,

    surface = CinematicGray,
    onSurface = CreamWhite,

    surfaceVariant = FilmBrown30,
    onSurfaceVariant = FilmBrown90,

    inverseSurface = CreamWhite,
    inverseOnSurface = ShadowBlack,
    outline = Gold30
)


val FilmLightColorPalette = lightColorScheme(
    primary = Sunset40,
    onPrimary = Color.White,
    primaryContainer = Sunset90,
    onPrimaryContainer = Sunset10,

    secondary = Gold40,
    onSecondary = Color.White,
    secondaryContainer = Gold90,
    onSecondaryContainer = Gold10,

    tertiary = FilmBrown40,
    onTertiary = Color.White,
    tertiaryContainer = FilmBrown90,
    onTertiaryContainer = FilmBrown10,

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = CreamWhite,
    onBackground = CinematicGray,

    surface = LightDust,
    onSurface = ShadowBlack,

    surfaceVariant = FilmBrown80,
    onSurfaceVariant = FilmBrown20,

    inverseSurface = ShadowBlack,
    inverseOnSurface = CreamWhite,
    outline = FilmBrown40
)


// Definición de colores para el tema claro
val LightColorPalette = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray95,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray10,
    inverseSurface = DarkGreenGray10,
    inverseOnSurface = DarkGreenGray99,
    outline = GreenGray60
)

// Definición de colores para el tema oscuro
val NeonDarkColorPalette = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

// Tema Neon - Inspirado en los años 80/90 con colores vibrantes
val NeonColorPalette = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60
)

// Tema Retro - Inspirado en los años 70 con colores tierra
val RetroColorPalette = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray95,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray10,
    inverseSurface = DarkGreenGray10,
    inverseOnSurface = DarkGreenGray99,
    outline = GreenGray60
)