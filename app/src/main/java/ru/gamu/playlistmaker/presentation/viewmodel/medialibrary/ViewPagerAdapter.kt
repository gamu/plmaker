package ru.gamu.playlistmaker.presentation.viewmodel.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.gamu.playlistmaker.domain.models.MediaLibraryTab
import ru.gamu.playlistmaker.presentation.fragments.FavoriteFragment
import ru.gamu.playlistmaker.presentation.fragments.PlaylistFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val tabs: List<MediaLibraryTab>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        val tab = tabs[position]
        if(tab.key == MediaLibraryTab.TabKeys.Playlists)
            return PlaylistFragment.newInstance()
        else
            return FavoriteFragment.newInstance()
    }
}