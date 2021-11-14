package dev.m13d.composition.domain.entity

data class GameResult(

    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gemeSettings: GameSettings
)
