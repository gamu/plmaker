package ru.gamu.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gamu.playlistmaker.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var binding: FragmentFavoriteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}