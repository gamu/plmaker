package ru.gamu.playlistmaker.presentation.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivityMediaLibraryBinding
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.MediaLibraryViewModel
import ru.gamu.playlistmaker.presentation.viewmodel.medialibrary.ViewPagerAdapter
import ru.gamu.playlistmaker.utils.dsl.getDataBinding

class MediaLibraryActivity : AppCompatActivity() {
    private var binding: ActivityMediaLibraryBinding? = null
    private val viewModel by viewModel<MediaLibraryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getDataBinding(this, R.layout.activity_media_library)

        val viewPager = binding?.viewPager!!
        val tabLayout = binding?.tabLayout!!


        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, viewModel.tabs)
        viewPager?.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.tabs[position]?.title
        }.attach()
    }

    fun backButtonClick(view: View){
        finish()
    }
}