package org.lanzadera.proyectos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.unicorn
import org.jetbrains.compose.resources.painterResource
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.navigation.NavigationStore
import org.lanzadera.proyectos.utils.Constants

@Composable
fun BookItem(
    nav: NavHostController,
    book: Book,
    modifier: Modifier = Modifier.wrapContentHeight(),
    showMeta: Boolean = true
) {
    Column(
        modifier = modifier
            .clickable {
                // clear any selected movie and set the selected book, then navigate
                NavigationStore.selectedMovie = null
                NavigationStore.selectedBook = book
                println("SYNCRO BookItem: clicking book, thumbnail=${book.thumbnail}")
                nav.navigate(Constants.Screen.Detail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.unicorn)
            )
        }

        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            book.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            book.authors?.joinToString(", ")?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun BookDetail(book: Book?) {
    if (book == null) return
    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = book.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().size(240.dp),
                placeholder = painterResource(Res.drawable.unicorn)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                book.title?.let {
                    Text(text = it, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                book.authors?.joinToString(", ")?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).navigationBarsPadding()) {
            item {
                book.description?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = book.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BookHeader(modifier: Modifier = Modifier, nav: NavHostController, book: Book) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedMovie = null
                NavigationStore.selectedBook = book
                nav.navigate(Constants.Screen.Detail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.unicorn)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        book.title?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        book.authors?.firstOrNull()?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun BookSubheader(modifier: Modifier = Modifier, nav: NavHostController, book: Book, showMeta: Boolean) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable {
                NavigationStore.selectedMovie = null
                NavigationStore.selectedBook = book
                nav.navigate(Constants.Screen.Detail.route)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(Res.drawable.unicorn)
            )
        }
        if (showMeta) {
            Spacer(modifier = Modifier.height(6.dp))
            book.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            book.authors?.firstOrNull()?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}
