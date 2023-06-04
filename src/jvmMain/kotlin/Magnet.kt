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

import java.net.URLEncoder

class Magnet(val infoHash: String) {
    var trackers = mutableListOf<String>()
    var name: String = ""

    fun addTracker(trackerURL: String) {
        this.trackers.add(trackerURL.replace("\\s".toRegex(), ""))
    }

    fun addName(name: String) {
        this.name = URLEncoder.encode(name, Charsets.UTF_8)
    }

    fun getMagnetLink(): String {
        if (!this.name.isBlank()) {
            this.name = this.infoHash
        }
        var magnetURI = "magnet:?xt=urn:btih:${this.infoHash}&dn=${this.name}"
        trackers.forEach { tracker ->
            magnetURI = "$magnetURI&tr=${URLEncoder.encode(tracker, Charsets.UTF_8)}"
        }
        return magnetURI
    }

    companion object {
        fun createMagnetURL(infoHash: String, name: String, trackers: List<String>): String {
            var magnetURI = "magnet:?xt=urn:btih:${infoHash}&dn=${name}"
            trackers.forEach { tracker ->
                if (tracker.isNotBlank()) {
                    magnetURI = "$magnetURI&tr=${URLEncoder.encode(tracker, Charsets.UTF_8)}"
                }
            }
            return magnetURI
        }
    }
}