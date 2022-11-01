package com.rzmmzdh.gita.feature_search.ui.repository_detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rzmmzdh.gita.R
import com.rzmmzdh.gita.common.theme.jbMono
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    navController: NavHostController,
    state: RepositoryDetailViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        state.repoDetailState.detail?.let {
            RepoDetail(
                paddingValues,
                stars = it.stargazersCount,
                forks = it.forksCount,
                avatar = it.owner.avatar_url,
                name = it.fullName,
                description = it.description
                    ?: stringResource(R.string.description),
                cloneUrl = it.cloneUrl,
                onCloneUrlClick = {
                    clipboardManager.setText(
                        AnnotatedString(
                            it.cloneUrl
                        )
                    )
                    snackbarScope.launch { snackbarHostState.showSnackbar(message = "Clone URL copied to clipboard.") }
                },
                language = it.language ?: "Language-agnostic",
                license = it.license?.name ?: "Licenseless"
            )
        }
    }
}

@Composable
private fun RepoDetail(
    paddingValues: PaddingValues,
    stars: Int,
    forks: Int,
    avatar: String,
    name: String,
    description: String,
    cloneUrl: String,
    onCloneUrlClick: () -> Unit,
    language: String?,
    license: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(avatar)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Stars(stars)
            Forks(forks)
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Name(name)
        Description(description)
        CloneUrl(cloneUrl = cloneUrl, onClick = onCloneUrlClick)
        license?.let { License(it) }
        Spacer(modifier = Modifier.height(8.dp))
        language?.let { Language(it) }
    }
}

@Composable
private fun Language(language: String = "Language") {
    Box(
        modifier = Modifier
            .border(
                width = DividerDefaults.Thickness,
                color = DividerDefaults.color,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = language,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = jbMono,
                fontWeight = FontWeight.Light,
            ),
            modifier = Modifier.padding(6.dp)
        )

    }
}

@Composable
private fun License(license: String = "License") {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = license,
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        ),
    )
}

@Composable
private fun Description(description: String = "Description") {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = description,
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        ),
        maxLines = 6
    )
}

@Composable
private fun Name(name: String = "Name") {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = name,
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        ),
        maxLines = 2
    )
}

@Composable
private fun Forks(forks: Int = 0) {
    Text(
        text = "🧑‍🌾 $forks",
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    )
}

@Composable
private fun Stars(stars: Int = 0) {
    Text(
        text = "⭐ $stars",
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    )
}

@Composable
private fun Avatar(avatar: String) {
    Card(
        modifier = Modifier
            .size(124.dp)
            .clip(CircleShape)
            .border(
                width = DividerDefaults.Thickness,
                color = DividerDefaults.color,
                shape = CircleShape
            )
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = "owner_avatar",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )
    }
}

@Composable
private fun CloneUrl(
    cloneUrl: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        text = cloneUrl,
        style = TextStyle(
            fontFamily = jbMono,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.LineThrough
        ),
    )
}