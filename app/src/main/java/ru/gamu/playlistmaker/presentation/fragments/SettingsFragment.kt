package ru.gamu.playlistmaker.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.FragmentSettingsBinding
import ru.gamu.playlistmaker.presentation.viewmodel.settings.SettingsViewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentSettingsBinding.inflate(inflater, container, false).let{
            val view = it.root
            it.vm = viewModel
            addEventHandlers(it)
            view
        }
    }

    private fun addEventHandlers(binding: FragmentSettingsBinding){
        binding.btnShare.setOnClickListener {
            viewModel.shareApplication(){
                startActivity(Intent.createChooser(it, getString(R.string.optionsMessage)))
            }
        }

        binding.btnSupport.setOnClickListener {
            viewModel.sendMail(){
                startActivity(it)
            }
        }

        binding.btnBrowser.setOnClickListener {
            viewModel.showAgreement(){
                startActivity(it)
            }
        }

        viewModel.addThelmeChangeListener(this){
            viewModel.toggleTheme(it)
        }
    }
}