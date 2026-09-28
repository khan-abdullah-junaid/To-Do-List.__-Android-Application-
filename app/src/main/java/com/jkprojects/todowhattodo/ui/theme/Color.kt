package com.jkprojects.todowhattodo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jkprojects.todowhattodo.model.Priority

// Material 3 Light Color Scheme Tokens
val PrimaryLight = Color(0xFF3B5199)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFDCE1FF)
val OnPrimaryContainerLight = Color(0xFF00154F)

val SecondaryLight = Color(0xFF006A60)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFF74F8E5)
val OnSecondaryContainerLight = Color(0xFF00201C)

val TertiaryLight = Color(0xFF7B5300)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFDEAC)
val OnTertiaryContainerLight = Color(0xFF281900)

val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val BackgroundLight = Color(0xFFF9F9FF)
val OnBackgroundLight = Color(0xFF1A1B20)
val SurfaceLight = Color(0xFFF9F9FF)
val OnSurfaceLight = Color(0xFF1A1B20)
val SurfaceVariantLight = Color(0xFFE2E1EC)
val OnSurfaceVariantLight = Color(0xFF45464F)
val OutlineLight = Color(0xFF767680)
val OutlineVariantLight = Color(0xFFC6C5D0)

val SurfaceContainerLowestLight = Color(0xFFFFFFFF)
val SurfaceContainerLowLight = Color(0xFFF3F3FA)
val SurfaceContainerLight = Color(0xFFEDEEF4)
val SurfaceContainerHighLight = Color(0xFFE8E8EE)
val SurfaceContainerHighestLight = Color(0xFFE2E2E8)


// Material 3 Dark Color Scheme Tokens
val PrimaryDark = Color(0xFFB5C4FF)
val OnPrimaryDark = Color(0xFF00277E)
val PrimaryContainerDark = Color(0xFF203A80)
val OnPrimaryContainerDark = Color(0xFFDCE1FF)

val SecondaryDark = Color(0xFF53DBC9)
val OnSecondaryDark = Color(0xFF003731)
val SecondaryContainerDark = Color(0xFF005049)
val OnSecondaryContainerDark = Color(0xFF74F8E5)

val TertiaryDark = Color(0xFFFBBB49)
val OnTertiaryDark = Color(0xFF412D00)
val TertiaryContainerDark = Color(0xFF5E4000)
val OnTertiaryContainerDark = Color(0xFFFFDEAC)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

val BackgroundDark = Color(0xFF121318)
val OnBackgroundDark = Color(0xFFE2E2E9)
val SurfaceDark = Color(0xFF121318)
val OnSurfaceDark = Color(0xFFE2E2E9)
val SurfaceVariantDark = Color(0xFF45464F)
val OnSurfaceVariantDark = Color(0xFFC6C5D0)
val OutlineDark = Color(0xFF8F909A)
val OutlineVariantDark = Color(0xFF45464F)

val SurfaceContainerLowestDark = Color(0xFF0D0E13)
val SurfaceContainerLowDark = Color(0xFF1A1B20)
val SurfaceContainerDark = Color(0xFF1E1F25)
val SurfaceContainerHighDark = Color(0xFF292A30)
val SurfaceContainerHighestDark = Color(0xFF34343B)


// Priority Specific Color Definitions (High = Red/Orange, Medium = Amber/Yellow, Low = Blue/Green)
data class PriorityColorSet(
    val container: Color,
    val onContainer: Color,
    val border: Color,
    val accent: Color,
)

val PriorityHighLight = PriorityColorSet(
    container = Color(0xFFFFDAD6),
    onContainer = Color(0xFF410002),
    border = Color(0xFFFFB4AB),
    accent = Color(0xFFD32F2F)
)

val PriorityHighDark = PriorityColorSet(
    container = Color(0xFF93000A),
    onContainer = Color(0xFFFFDAD6),
    border = Color(0xFFBA1A1A),
    accent = Color(0xFFFF5252)
)

val PriorityMediumLight = PriorityColorSet(
    container = Color(0xFFFFF0D4),
    onContainer = Color(0xFF4E3200),
    border = Color(0xFFFFC107),
    accent = Color(0xFFF57C00)
)

val PriorityMediumDark = PriorityColorSet(
    container = Color(0xFF5A3B00),
    onContainer = Color(0xFFFFE0B2),
    border = Color(0xFFFFB300),
    accent = Color(0xFFFFB74D)
)

val PriorityLowLight = PriorityColorSet(
    container = Color(0xFFE0F2FE),
    onContainer = Color(0xFF0369A1),
    border = Color(0xFF7DD3FC),
    accent = Color(0xFF0284C7)
)

val PriorityLowDark = PriorityColorSet(
    container = Color(0xFF0C4A6E),
    onContainer = Color(0xFFBAE6FD),
    border = Color(0xFF38BDF8),
    accent = Color(0xFF38BDF8)
)

@Composable
fun getPriorityColorSet(priority: Priority, isDarkTheme: Boolean = isSystemInDarkTheme()): PriorityColorSet {
    return when (priority) {
        Priority.HIGH -> if (isDarkTheme) PriorityHighDark else PriorityHighLight
        Priority.MEDIUM -> if (isDarkTheme) PriorityMediumDark else PriorityMediumLight
        Priority.LOW -> if (isDarkTheme) PriorityLowDark else PriorityLowLight
    }
}
