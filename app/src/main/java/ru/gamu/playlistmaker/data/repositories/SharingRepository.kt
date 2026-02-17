package ru.gamu.playlistmaker.data.repositories

import android.content.Context
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.data.dto.EmailData
import ru.gamu.playlistmaker.data.handlers.ExternalNavigator
import ru.gamu.playlistmaker.domain.repository.SharingRepository


class SharingRepositoryImpl(private val context: Context, private val externalNavigator: ExternalNavigator) :
    SharingRepository {
    override fun shareLink(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun shareApp() {
        externalNavigator.shareLink(context.getString(R.string.link_message))
    }

    override fun openEmail() {
        externalNavigator.openEmail(
            EmailData(
                title = context.getString(R.string.subject_message),
                text = context.getString(R.string.dev_message),
                emailAddress = context.getString(R.string.email_message)
            )
        )
    }

    override fun openTerms() {
        externalNavigator.openLink(context.getString(R.string.uri_message))
    }


}