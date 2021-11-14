package dev.m13d.composition.domain.interactor

import dev.m13d.composition.domain.entity.Question
import dev.m13d.composition.domain.repository.GameRepository

class GetNewQuestionInteractor(
    private val repository: GameRepository
) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.getNewQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {

        private const val COUNT_OF_OPTIONS = 6
    }
}
