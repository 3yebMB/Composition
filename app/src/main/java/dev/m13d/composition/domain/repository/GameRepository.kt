package dev.m13d.composition.domain.repository

import dev.m13d.composition.domain.entity.GameSettings
import dev.m13d.composition.domain.entity.Level
import dev.m13d.composition.domain.entity.Question

interface GameRepository {

    fun getNewQuestion(maxSumValue: Int, countOfOptions: Int): Question

    fun getGameSettings(level: Level): GameSettings
}
