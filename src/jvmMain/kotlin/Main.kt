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
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            magnetHash()
            Spacer(modifier = Modifier.size(4.dp))
            torrentName()

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
