package com.ogonzalezm.testgepsi.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.ogonzalezm.test_gepsi.R
import com.ogonzalezm.testgepsi.domain.model.Item

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val paging = viewModel.items.collectAsLazyPagingItems()
    val onIntent = viewModel::handleIntent

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        value = state.keyword,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true,
                        onValueChange = {
                        onIntent(HomeIntent.EnterKeyword(it))
                    })
                    IconButton(onClick = { onIntent(HomeIntent.Search) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }
                if(state.errorMessage.isNotBlank()) {
                    Text(modifier = Modifier.fillMaxWidth()
                        .padding(20.dp),
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error)
                }

                LazyColumn {
                    items(
                        count = paging.itemCount,
                        key = { index -> "placeholder-$index" },
                        contentType = { "item" }
                    ) { index ->
                        val item = paging[index]
                        if (item != null) {
                            ItemCard(item)
                        }
                    }
                    item {
                        when (val pageStatus = paging.loadState.append) {
                            is LoadState.Loading -> PageLoading()
                            is LoadState.Error -> PageError(paging)
                            else -> {}
                        }
                    }
                }
            }
        }
    )

}

@Composable
fun ItemCard(item: Item) {
    if(item.id == null) return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    item.name ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "$ ${item.price}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item.imageInfo?.thumbnailUrl?.let {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .weight(1f),
                    model = it,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
fun PageLoading() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PageError(paging: LazyPagingItems<Item>) {
    TextButton({ paging.retry() }) {
        Text(
            stringResource(R.string.error_retry),
            color = MaterialTheme.colorScheme.error
        )
    }
}