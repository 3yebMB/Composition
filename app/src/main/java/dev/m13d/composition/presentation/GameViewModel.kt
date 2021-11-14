package dev.m13d.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.composition.R
import dev.m13d.composition.data.GameRepositoryImpl
import dev.m13d.composition.domain.entity.GameResult
import dev.m13d.composition.domain.entity.GameSettings
import dev.m13d.composition.domain.entity.Level
import dev.m13d.composition.domain.entity.Question
import dev.m13d.composition.domain.interactor.GetGameSettingsInteractor
import dev.m13d.composition.domain.interactor.GetNewQuestionInteractor

class GameViewModel(app: Application) : AndroidViewModel(app) {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val context = app
    private val repository = GameRepositoryImpl

    private val getGameSettingsInteractor = GetGameSettingsInteractor(repository)
    private val getNewQuestionInteractor = GetNewQuestionInteractor(repository)

    private var timer: CountDownTimer? = null


    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: MutableLiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoghCount = MutableLiveData<Boolean>()
    val enoghCount: LiveData<Boolean>
        get() = _enoghCount

    private val _enoghPercent = MutableLiveData<Boolean>()
    val enoghPercent: LiveData<Boolean>
        get() = _enoghPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    private fun getNewQuestion() {
        _question.value = getNewQuestionInteractor(gameSettings.maxSumValue)
    }

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        getNewQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        getNewQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswer.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoghCount.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoghPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            ++countOfRightAnswers
        }
        ++countOfQuestions
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsInteractor(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gemeTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoghCount.value == true && enoghPercent.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        const val MILLIS_IN_SECONDS = 1000L
        const val SECONDS_IN_MINUTES = 60
    }
}
