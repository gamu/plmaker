package ru.gamu.playlistmaker.data.models

import ru.gamu.playlistmaker.data.dto.IResponse
import ru.gamu.playlistmaker.data.dto.SearchItem
import ru.gamu.playlistmaker.domain.models.Track


data class ResponseRoot(val resultCount: Long,
                        val results: List<SearchItem>,
                        val isError: Boolean = false)


enum class Response: IResponse<List<Track>> {

    SUCCESS {
        private var result: List<Track> = listOf()
        private var error: Exception? =null;

        override fun getError(): Exception? {
            return error
        }

        override fun setError(ex: Exception) {
            error = ex
        }

        override fun getIsError(): Boolean {
            return false;
        }

        override fun getCount(): Int {
            return this.result.size
        }

        override fun setResult(result: List<Track>) {
            this.result = result
        }


        override fun getResult(): List<Track> {
            return this.result
        }
    },
    ERROR {
        private var result: List<Track> = listOf()
        private var error: Exception? =null;

        override fun getError(): Exception? {
            return error
        }

        override fun setError(ex: Exception) {
            error = ex
        }

        override fun getIsError(): Boolean {
            return true
        }

        override fun getCount(): Int {
            return this.result.size
        }

        override fun setResult(result: List<Track>) {
            this.result = result
        }

        override fun getResult(): List<Track> {
            return this.result
        }
    }
}

