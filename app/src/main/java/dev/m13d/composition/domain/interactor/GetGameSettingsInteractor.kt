package dev.m13d.composition.domain.interactor

import dev.m13d.composition.domain.entity.GameSettings
import dev.m13d.composition.domain.entity.Level
import dev.m13d.composition.domain.repository.GameRepository

class GetGameSettingsInteractor(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}
