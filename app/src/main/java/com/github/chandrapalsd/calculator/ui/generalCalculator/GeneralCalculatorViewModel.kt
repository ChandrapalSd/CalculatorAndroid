package com.github.chandrapalsd.calculator.ui.generalCalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException


class GeneralCalculatorViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState
    private var isLastDigit: Boolean = false
    private var isErrorState: Boolean = false

    init {
        _uiState.value = UiState()
    }

    private fun setUiState(
        inputString: String? = null,
        resultString: String? = null
    ) {
        _uiState.value = UiState(
            inputString ?: uiState.value!!.inputString,
            resultString ?: uiState.value!!.resultString
        )
    }

    private fun evaluateExpr(exprString: String): String? {
        if (isLastDigit) {
            val expr = ExpressionBuilder(exprString).build()
            return try {
                isErrorState = false
                expr.evaluate().toString()
            } catch (e: ArithmeticException) {
                isErrorState = true
                ""
            } catch (e: Throwable) {
                isErrorState = true
                "Unknown Error!"
            }
        }

        return null
    }

    fun onDigitClick(digitStr: String) {
        isLastDigit = true
        val newInputStr = uiState.value!!.inputString + digitStr
        val newResultStr = evaluateExpr(newInputStr)
        setUiState(newInputStr, newResultStr)

    }


    fun onOperatorClick(opStr: String) {
        if (isLastDigit) {
            isLastDigit = false
            val newInputStr = uiState.value!!.inputString + opStr
            val newResultStr = ""
            setUiState(newInputStr, newResultStr)
        }
    }

    fun onBackspaceClick() {
        var newInputString = uiState.value!!.inputString

        if (newInputString.isNotEmpty()) {
            newInputString = newInputString.dropLast(1)

            if (newInputString.isEmpty()) {
                onAllClearClick()
            } else {
                val lastChar = newInputString.last()

                val newResultString =
                    if (lastChar.isDigit()) {
                        isLastDigit = true
                        evaluateExpr(newInputString)
                    } else {
                        isLastDigit = false
                        ""
                    }

                setUiState(newInputString, newResultString)
            }
        }


    }

    fun onDecimalClick(decimal: String) {

        uiState.value!!.apply {
            if (!isLastDigit) {
                onDigitClick("0$decimal")
            } else {
                onDigitClick(decimal)
            }
        }

    }

    fun onEqualClick() {
        var newInputString: String? = null
        var newResultString: String? = null

        if (!isErrorState) {
            if (isLastDigit && uiState.value!!.resultString.isNotEmpty()) {
                newInputString = uiState.value!!.resultString
                newResultString = ""
            }
        } else {
            newResultString = "Error!"
        }
        setUiState(newInputString, newResultString)
    }


    fun onAllClearClick() {
        isLastDigit = false
        isErrorState = false
        setUiState("", "")
    }

    fun onClearClick() {
        val shouldClearResult = isErrorState
        isErrorState = false
        isLastDigit = false

        if (shouldClearResult) {
            setUiState("", "")
        } else {
            setUiState(inputString = "")
        }

    }


}

data class UiState(
    var inputString: String = "",
    var resultString: String = ""
)