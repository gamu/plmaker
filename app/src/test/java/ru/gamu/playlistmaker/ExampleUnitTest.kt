package ru.gamu.playlistmaker

import org.junit.Test

import ru.gamu.playlistmaker.features.search.network.SearchRequest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val searchTracks = SearchRequest()
        searchTracks.makeCall("test"){
        }
    }
}