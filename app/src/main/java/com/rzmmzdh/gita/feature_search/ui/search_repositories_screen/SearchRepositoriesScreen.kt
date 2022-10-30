package com.rzmmzdh.gita.feature_search.ui.search_repositories_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rzmmzdh.gita.feature_search.domain.model.Item
import com.rzmmzdh.gita.feature_search.ui.core.colorTransition
import com.rzmmzdh.gita.core.theme.jbMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRepositoriesScreen(
    navController: NavHostController,
    state: SearchRepositoriesViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            GitaSearchBar(query = state.searchQuery.value, onQueryChange = {
                state.onEvent(SearchRepositoriesEvent.OnSearchQueryChange(it))
            }, isLoading = state.searchResult.value.isLoading)
        }
    ) { paddingValues ->
        SearchResult(paddingValues = paddingValues, result = state.searchResult.value.data?.items)

    }
}

@Composable
private fun SearchResult(
    paddingValues: PaddingValues,
    result: List<Item>?
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(result ?: emptyList()) { result ->
            ElevatedCard(
                modifier = Modifier
                    .size(192.dp)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                            "⭐ ${result.stargazers_count}", style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = jbMono,
                                fontWeight = FontWeight.Light
                            )
                        )
                        Text(
                            text =
                            "🧑‍🌾 ${result.forks_count}", style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = jbMono,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }

                    Text(
                        text = result.full_name,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = jbMono
                        ), maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = result.description ?: "description",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = jbMono,
                            fontSize = 12.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                    Box(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Text(
                            text = result.language ?: "Language-agnostic",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = jbMono,
                                fontWeight = FontWeight.Light
                            ),
                            modifier = Modifier.padding(4.dp)
                        )

                    }

                }

            }


        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GitaSearchBar(
    query: String, onQueryChange: (String) -> Unit, isLoading: Boolean
) =
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Column {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                    singleLine = true,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = jbMono
                    ),
                    placeholder = {
                        Text(
                            text = "Gita",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontFamily = jbMono,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = colorTransition(
                                    initialColor = MaterialTheme.colorScheme.primary,
                                    targetColor = MaterialTheme.colorScheme.secondary,
                                    tweenAnimationDuration = 3000
                                )
                            )
                        )
                    }
                )
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

            }

        })