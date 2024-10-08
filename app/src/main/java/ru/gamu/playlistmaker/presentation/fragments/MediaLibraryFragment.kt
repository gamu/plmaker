package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.databinding.FragmentMediaLibraryBinding
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.MediaLibraryViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.ViewPagerAdapter

class MediaLibraryFragment : Fragment() {
    private val viewModel by viewModel<MediaLibraryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentMediaLibraryBinding.inflate(inflater, container, false).let{
            val view = it.root
            val viewPager = it.viewPager
            val tabLayout = it.tabLayout
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, viewModel.tabs)
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = viewModel.tabs[position].title
            }.attach()
            view
        }
    }
}