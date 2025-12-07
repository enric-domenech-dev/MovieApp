# 🐛 Análisis: ¿Por qué los tests no detectaron los bugs?

**Bugs:** Favoritos no mostraban series + Tab de películas vacío

## Resumen

Los tests NO detectaron los bugs porque solo hay **tests unitarios con Fakes** (en memoria), pero los bugs estaban en:
- **Serialización Room** (FavoriteDetailsRepositoryImpl)
- **Deserialización HTTP** (LoadInitialDataImpl)

## Root Causes

### Bug #1: Series no aparecían en favoritos
- `Season` perdió `@Serializable` en Task 1.10
- `json.encodeToString(seasons)` fallaba silenciosamente
- Room guardaba `seasonsJson = null`
- Test con Fake no serializa → no detecta el bug

### Bug #2: Películas no cargaban
- `MovieResponse` perdió `@Serializable` en Task 1.9
- `json.decodeFromString<MovieResponse>()` lanzaba excepción
- Test con Fake no deserializa HTTP → no detecta el bug

## Tests que existen
- ✅ 6 ViewModels (con Fakes)
- ✅ 3 Use Cases
- ✅ 8 Fakes

## Tests que NO existen
- ❌ FavoriteDetailsRepositoryImplTest (Room serialization)
- ❌ LoadInitialDataImplTest (HTTP deserialization)
- ❌ MovieMapperTest (DTO serialization)
- ❌ TvShowMapperTest (DTO serialization)

## Lecciones

| Test Type | Prueba | NO prueba |
|-----------|--------|-----------|
| Unitario (Fake) | Lógica | Serialización, Persistencia, Red |
| Integración | Todo | - |

**Principio:** Si eliminas `@Serializable` → Los tests de serialización deben fallar

## Recomendaciones

1. Agregar tests de serialización para cada DTO
2. Agregar tests de integración para repositorios reales
3. Considerar Robolectric para tests de Room sin emulador
4. Agregar CI check: "Cada repository tiene ≥1 test"
