package org.lanzadera.proyectos

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

// Definición de colores para el tema claro
val LightColorPalette = lightColors(
    primary = Color(0xFFD32F2F),        // Rojo principal
    primaryVariant = Color(0xFFB71C1C),  // Rojo más oscuro
    secondary = Color(0xFF757575),       // Gris secundario
    background = Color(0xFFFFFFFF),      // Blanco
    surface = Color(0xFFFFFFFF),         // Blanco
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

// Definición de colores para el tema oscuro
val DarkColorPalette = darkColors(
    primary = Color(0xFFEF5350),        // Rojo más claro para contraste
    primaryVariant = Color(0xFFD32F2F),  // Rojo principal
    secondary = Color(0xFF90A4AE),       // Gris azulado
    background = Color(0xFF121212),      // Negro carbón
    surface = Color(0xFF1E1E1E),         // Gris muy oscuro
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

// Tema Neon - Inspirado en los años 80/90 con colores vibrantes
val NeonColorPalette = darkColors(
    primary = Color(0xFFFF0080),        // Rosa neón vibrante
    primaryVariant = Color(0xFFE91E63), // Rosa más oscuro
    secondary = Color(0xFF00FFFF),      // Cian neón
    background = Color(0xFF0A0A0A),     // Negro profundo
    surface = Color(0xFF1A1A2E),        // Azul muy oscuro
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFF00FFFF),   // Texto cian
    onSurface = Color(0xFF00FFFF),      // Texto cian
    error = Color(0xFFFF0040),          // Rojo neón
    onError = Color.Black
)

// Tema Retro - Inspirado en los años 70 con colores tierra
val RetroColorPalette = lightColors(
    primary = Color(0xFFD2691E),        // Naranja chocolate (muy retro)
    primaryVariant = Color(0xFF8B4513),  // Marrón silla de montar
    secondary = Color(0xFFCD853F),       // Beige dorado
    background = Color(0xFFFDF5E6),      // Crema antiguo
    surface = Color(0xFFF5DEB3),         // Trigo
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF654321),    // Marrón oscuro para texto
    onSurface = Color(0xFF654321),       // Marrón oscuro para texto
    error = Color(0xFFDC143C),           // Carmesí
    onError = Color.White
)