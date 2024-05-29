package ru.gamu.plmaker.core

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.gamu.playlistmaker.data.ICommandHandler
import ru.gamu.playlistmaker.data.IQueryHandler

@Suppress("UNCHECKED_CAST")
class SearchTest {
    private lateinit var trackHistorySlot: CapturingSlot<Set<ru.gamu.playlistmaker.domain.models.Track>>
    private lateinit var search: ru.gamu.playlistmaker.data.TrackList
    @Before
    fun initAll(){
        val tracks = listOf(
            ru.gamu.playlistmaker.domain.models.Track("t0", "a0", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t1", "a1", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t2", "a2", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t3", "a3", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t4", "a4", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t5", "a5", "2:30", "")
        )

        val tracksCahe = setOf(
            ru.gamu.playlistmaker.domain.models.Track("t1", "a1", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t10", "a10", "2:30", ""),
            ru.gamu.playlistmaker.domain.models.Track("t9", "a9", "2:30", "")
        )

        val mockSearchReader = mockk<ru.gamu.playlistmaker.data.IQueryHandler<List<ru.gamu.playlistmaker.domain.models.Track>?, String>>()
        val mockCaheWriter = mockk<ru.gamu.playlistmaker.data.ICommandHandler<Set<ru.gamu.playlistmaker.domain.models.Track>>>()
        val mockCaheReader = mockk<ru.gamu.playlistmaker.data.IQueryHandler<Set<ru.gamu.playlistmaker.domain.models.Track>, Unit>>()

        trackHistorySlot = slot<Set<ru.gamu.playlistmaker.domain.models.Track>>()

        every { mockSearchReader.getData("") } returns tracks
        every { mockCaheReader.getData(Unit) } returns tracksCahe
        every { mockCaheWriter.Execute(capture(trackHistorySlot)) } returns Unit

        search =
            ru.gamu.playlistmaker.data.TrackList(mockSearchReader, mockCaheWriter, mockCaheReader)
    }
    @After
    fun tearDown(){
        search.clearHistory()
    }
    @Test
    fun can_return_search_result_with_joined_similar_tracks_from__cache(){
        val results = search.searchItems("")
        Assert.assertEquals(6, results?.count())
        Assert.assertEquals("t1", results?.first()?.trackName)
    }
    @Test
    fun can_add_only_10_tracks_to_history(){
        (11..25)
            .map { ru.gamu.playlistmaker.domain.models.Track("t${it}", "a${it}", "2:30", "") }
            .forEach { search.addTrackToHistory(it) }
        val capturedArgument = trackHistorySlot.captured
        Assert.assertEquals(10, capturedArgument.count())
    }
    @Test
    fun bubbling_similar_track(){
        search.addTrackToHistory(ru.gamu.playlistmaker.domain.models.Track("t9", "a9", "2:30", ""))
        val firstTrack = trackHistorySlot.captured.first()
        Assert.assertEquals("t9", firstTrack.trackName)
        Assert.assertEquals(3, trackHistorySlot.captured.size)
    }

    @Test
    fun new_track_is_first(){
        search.addTrackToHistory(ru.gamu.playlistmaker.domain.models.Track("tN", "aN", "", ""))
        val firstTrack = trackHistorySlot.captured.first()
        Assert.assertEquals("tN", firstTrack.trackName)
    }
}