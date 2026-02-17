package ru.gamu.playlistmaker.domain.usecases

import ru.gamu.playlistmaker.domain.repository.SharingRepository

class SharingService(private val sharingRepository: SharingRepository) {
    fun shareApp() {
        sharingRepository.shareApp()
    }

    fun openTerms() {
        sharingRepository.openTerms()
    }

    fun openEmail() {
        sharingRepository.openEmail()
    }

    fun share(link: String) {
        sharingRepository.shareLink(link)
    }
}