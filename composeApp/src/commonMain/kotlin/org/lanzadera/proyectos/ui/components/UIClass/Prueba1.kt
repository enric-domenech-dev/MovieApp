package org.lanzadera.proyectos.ui.components.UIClass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.huella
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewspaperFrontPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC)) // color papel antiguo
            .padding(16.dp)
    ) {
        // Nombre del diario
        Text(
            text = "LA VOZ DEL TIEMPO",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Titular principal con imagen
        Column {
            Text(
                text = "Impactante hallazgo en la ciudad perdida",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoadImage("main_article.jpg", Modifier.fillMaxWidth().height(180.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Una expedición arqueológica descubre estructuras milenarias bajo la arena.",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Artículos secundarios
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryArticle(
                title = "Nueva teoría sobre el tiempo",
                imageUrl = "article1.jpg",
                description = "Científicos debaten sobre la posibilidad de doblar el tiempo espacio."
            )
            SecondaryArticle(
                title = "Luces misteriosas en el cielo",
                imageUrl = "article2.jpg",
                description = "Se registraron luces extrañas en varias partes del mundo."
            )
            SecondaryArticle(
                title = "Vuelve la escritura a mano",
                imageUrl = "article3.jpg",
                description = "Estudios revelan beneficios del papel en la memoria."
            )
        }
    }
}

@Composable
fun SecondaryArticle(title: String, imageUrl: String, description: String) {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(4.dp))
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(4.dp))
        LoadImage(imageUrl, Modifier.height(80.dp).fillMaxWidth())
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 12.sp,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
fun LoadImage(imageName: String, modifier: Modifier = Modifier) {
    val imageBitmap = painterResource(Res.drawable.huella)
    Image(
        painter = imageBitmap,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(4.dp))
    )
}


