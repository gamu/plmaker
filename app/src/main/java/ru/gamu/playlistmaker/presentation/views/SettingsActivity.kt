package ru.gamu.playlistmaker.presentation.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.gamu.playlistmaker.R
import ru.gamu.playlistmaker.databinding.ActivitySettingsBinding
import ru.gamu.playlistmaker.presentation.viewmodel.settings.SettingsViewModel
import ru.gamu.playlistmaker.utils.dsl.getDataBinding

class SettingsActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getDataBinding<ActivitySettingsBinding>(this, R.layout.activity_settings).apply {
            vm = viewModel
        }

        addEventHandlers()
    }

    private fun addEventHandlers(){
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