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
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


val API_URL = "https://newtrackon.com/api"

fun makeRequest(URL: String): String? {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$API_URL/stable"))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    println(response.body())
    return response.body()
}

fun getTrackers(trackerGroup: String): String? {

    when (trackerGroup) {
        "STABLE" -> {
            return makeRequest("$API_URL/stable")
        }

        "UDP" -> {
            return makeRequest("$API_URL/udp")
        }

        "HTTP" -> {
            return makeRequest("$API_URL/http")
        }

        "LIVE" -> {
            return makeRequest("$API_URL/live")
        }

        "ALL" -> {
            return makeRequest("$API_URL/all")
        }

        else -> return null
    }
}

@Composable
@Preview
fun App() {
    val trackerGroups = arrayOf("STABLE", "UDP", "HTTP", "LIVE", "ALL")
    var hash by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedTrackerGroup by remember { mutableStateOf(trackerGroups[0]) }
    var customTrackerList by remember { mutableStateOf("") }


    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            magnetHash(hash, onHashChange = { hash = it })
            Spacer(modifier = Modifier.size(4.dp))
            torrentName(name, onNameChange = { name = it })
            Spacer(modifier = Modifier.size(4.dp))
            Row {
                selectTrackers(
                    trackerGroups = trackerGroups,
                    selectedTrackerGroup = selectedTrackerGroup,
                    onTrackerGroupChange = { selectedTrackerGroup = it }
                )
                Button(
                    onClick = {
                        getTrackers(selectedTrackerGroup)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) { Text(text = "Genereate Magnet Link") }
            }
            Spacer(modifier = Modifier.size(4.dp))
            customTrackers(customTrackerList = customTrackerList, onCustomTrackersChange = { customTrackerList = it })
        }
    }
}

@Composable
fun selectTrackers(trackerGroups: Array<String>, selectedTrackerGroup: String, onTrackerGroupChange: (String) -> Unit) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (isDropdownExpanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedTrackerGroup,
            onValueChange = onTrackerGroupChange,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
            label = { Text(text = "Choose Tracker Group") },
            trailingIcon = {
                Icon(icon, "dropdown", Modifier.clickable { isDropdownExpanded = !isDropdownExpanded })
            },
            readOnly = true,
            singleLine = true
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) { textFieldSize.width.toDp() }
            )
        ) {
            trackerGroups.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        onTrackerGroupChange(label)
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
fun magnetHash(hash: String, onHashChange: (String) -> Unit) {
    TextField(
        value = hash,
        onValueChange = onHashChange,
        label = { Text(text = "Magnet Hash") },
        placeholder = { Text(text = "443c7602b4fde83d1154d6d9da48808418b181b6") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun customTrackers(customTrackerList: String, onCustomTrackersChange: (String) -> Unit) {
    OutlinedTextField(
        value = customTrackerList,
        onValueChange = onCustomTrackersChange,
        label = { Text(text = "Custom Tracker(one per line)") },
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp)
    )
}

@Composable
fun torrentName(name: String, onNameChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text(text = "Name") },
        placeholder = { Text(text = "Ubuntu.iso") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

fun main() = application {
    Window(title = "BitTorrent Magnet Link Generator", onCloseRequest = ::exitApplication) {
        App()
    }
}
