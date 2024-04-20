package ru.gamu.plmaker.core

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.gamu.plmaker.core.cq.ICommandHandler
import ru.gamu.plmaker.core.cq.IQueryHandler

@Suppress("UNCHECKED_CAST")
class SearchTest {
    private lateinit var trackHistorySlot: CapturingSlot<Set<Track>>
    private lateinit var search: TrackList
    @Before
    fun initAll(){
        val tracks = listOf(Track("t0", "a0", "2:30", ""),
            Track("t1", "a1", "2:30", ""),
            Track("t2", "a2", "2:30", ""),
            Track("t3", "a3", "2:30", ""),
            Track("t4", "a4", "2:30", ""),
            Track("t5", "a5", "2:30", ""))

        val tracksCahe = setOf(Track("t1", "a1", "2:30", ""),
            Track("t10", "a10", "2:30", ""),
            Track("t9", "a9", "2:30", ""))

        val mockSearchReader = mockk<IQueryHandler<List<Track>?, String>>()
        val mockCaheWriter = mockk<ICommandHandler<Set<Track>>>()
        val mockCaheReader = mockk<IQueryHandler<Set<Track>, Unit>>()

        trackHistorySlot = slot<Set<Track>>()

        every { mockSearchReader.getData("") } returns tracks
        every { mockCaheReader.getData(Unit) } returns tracksCahe
        every { mockCaheWriter.Execute(capture(trackHistorySlot)) } returns Unit

        search = TrackList(mockSearchReader, mockCaheWriter, mockCaheReader)
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
            .map { Track("t${it}", "a${it}", "2:30", "") }
            .forEach { search.addTrackToHistory(it) }
        val capturedArgument = trackHistorySlot.captured
        Assert.assertEquals(10, capturedArgument.count())
    }
    @Test
    fun bubbling_similar_track(){
        search.addTrackToHistory(Track("t9", "a9", "2:30", ""))
        val firstTrack = trackHistorySlot.captured.first()
        Assert.assertEquals("t9", firstTrack.trackName)
        Assert.assertEquals(3, trackHistorySlot.captured.size)
    }

    @Test
    fun new_track_is_first(){
        search.addTrackToHistory(Track("tN", "aN", "", ""))
        val firstTrack = trackHistorySlot.captured.first()
        Assert.assertEquals("tN", firstTrack.trackName)
    }
}