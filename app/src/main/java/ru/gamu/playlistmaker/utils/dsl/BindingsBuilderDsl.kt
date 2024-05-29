package ru.gamu.playlistmaker.utils.dsl

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


class BindingsBuilder(val activity: AppCompatActivity, val layoutId: Int) {
    fun <T: ViewDataBinding> build(): T {
        var binding = DataBindingUtil.setContentView<T>(activity, layoutId)
        binding.apply {
            lifecycleOwner = activity
        }
        return binding
    }
}

inline fun <T: ViewDataBinding> getDataBinding(activity: AppCompatActivity, layoutId: Int): T =
    BindingsBuilder(activity, layoutId)
        .build<T>()