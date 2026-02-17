package ru.gamu.playlistmaker.domain.repository

interface SharingRepository {
    fun shareLink(link: String)
    fun shareApp()
    fun openEmail()
    fun openTerms()
}