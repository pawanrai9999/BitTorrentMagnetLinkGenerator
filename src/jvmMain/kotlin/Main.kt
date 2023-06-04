/*
 * Copyright (C) 2023 Pawan Rai<pawanrai9999@gmail.com>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            magnetHash()
            Spacer(modifier = Modifier.size(4.dp))
            torrentName()
            Spacer(modifier = Modifier.size(4.dp))
            Row {
                selectTrackers()
                Button(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) { Text(text = "Genereate Magnet Link") }
            }
            Spacer(modifier = Modifier.size(4.dp))
            customTrackers()
        }
    }
}

@Composable
fun selectTrackers() {
    val trackersList = arrayOf("STABLE", "UDP", "HTTP", "LIVE", "ALL")
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedTracker by remember { mutableStateOf(trackersList[0]) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var icon = if (isDropdownExpanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedTracker,
            onValueChange = { selectedTracker = it },
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
            label = { Text(text = "Choose Tracker Group") },
            trailingIcon = {
                Icon(icon, "dropdown", Modifier.clickable { isDropdownExpanded = !isDropdownExpanded })
            },
            readOnly = true
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) { textFieldSize.width.toDp() }
            )
        ) {
            trackersList.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedTracker = label
                        isDropdownExpanded = false
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun magnetHash() {
    var hash by remember { mutableStateOf("") }

    TextField(
        value = hash,
        onValueChange = {
            hash = it
        },
        label = { Text(text = "Magnet Hash") },
        placeholder = { Text(text = "443c7602b4fde83d1154d6d9da48808418b181b6") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )
}

@Composable
fun customTrackers() {
    var trackers by remember { mutableStateOf("") }

    OutlinedTextField(
        value = trackers,
        onValueChange = { trackers = it },
        label = { Text(text = "Custom Tracker(one per line)") },
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp)
    )
}

@Composable
fun torrentName() {
    var name by remember { mutableStateOf("") }

    TextField(
        value = name,
        onValueChange = {
            name = it
        },
        label = { Text(text = "Name") },
        placeholder = { Text(text = "Ubuntu.iso") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )
}

fun main() = application {
    Window(title = "BitTorrent Magnet Link Generator", onCloseRequest = ::exitApplication) {
        App()
    }
}
