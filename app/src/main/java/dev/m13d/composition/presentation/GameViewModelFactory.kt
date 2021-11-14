package dev.m13d.composition.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.m13d.composition.domain.entity.Level
import java.lang.Appendable

class GameViewModelFactory(
    private val application: Application,
    private val level: Level
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, level) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}
