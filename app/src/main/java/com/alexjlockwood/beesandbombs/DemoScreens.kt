package com.alexjlockwood.beesandbombs

import android.os.Parcelable
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.android.parcel.Parcelize

sealed class DemoScreen : Parcelable {

    @Parcelize
    object DemoList : DemoScreen()

    @Immutable
    @Parcelize
    data class DemoDetails(val title: String) : DemoScreen()
}

@Composable
fun DemoList(demoListState: LazyListState, onDemoSelected: (title: String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.surface,
            )
        }
    ) {
        LazyColumnFor(items = DemoRegistry.keys.sorted(), state = demoListState) {
            ListItem(
                text = {
                    Text(
                        modifier = Modifier.preferredHeight(56.dp).wrapContentSize(Alignment.Center),
                        text = it,
                    )
                },
                modifier = Modifier.clickable { onDemoSelected.invoke(it) },
            )
        }
    }
}

@Composable
fun DemoDetails(demoDetails: DemoScreen.DemoDetails) {
    Box(modifier = Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val modifier = Modifier.aspectRatio(1f).fillMaxSize().padding(16.dp)
        DemoRegistry.getValue(demoDetails.title)(modifier)
    }
}
