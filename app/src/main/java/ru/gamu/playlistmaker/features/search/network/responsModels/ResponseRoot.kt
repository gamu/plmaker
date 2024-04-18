package ru.gamu.playlistmaker.features.search.network.responsModels

import ru.gamu.plmaker.core.IResponse
import ru.gamu.plmaker.core.Track


data class ResponseRoot(val resultCount: Long,
                        val results: List<SearchItem>,
                        val isError: Boolean = false)


enum class Response: IResponse<List<Track>> {

    SUCCESS {
        private var result: List<Track> = listOf()

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

