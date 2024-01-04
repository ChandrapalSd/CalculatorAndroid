package com.github.chandrapalsd.calculator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.github.chandrapalsd.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLastDigit = false
    private var isErrorState = false
    private var inputString: String = ""
    private var resultString: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun updateDisplay() {
        binding.tvInput.text = inputString
        binding.tvResult.text = "=" + resultString
    }

    fun onDigitClick(view: View) {
        inputString += (view as Button).text
        isLastDigit = true
        evaluateExpr()
        updateDisplay()
    }
    fun onDecimalClick(view: View) {
        if(!isLastDigit) {
            inputString += "0"
        }
        onDigitClick(view)
    }

    fun onEqualClick(view: View) {
        if (!isErrorState) {
            if (isLastDigit && resultString.isNotEmpty()) {
                inputString = resultString
                resultString = ""
            }
        } else {
            resultString = "Error!"
        }
        updateDisplay()
    }

    fun onAllClearClick(view: View) {
        inputString = ""
        resultString = ""
        isLastDigit = false
        isErrorState = false
        updateDisplay()
    }

    fun onClearClick(view: View) {
        if (isErrorState) {
            resultString = ""
        }

        isErrorState = false
        inputString = ""
        isLastDigit = false

        updateDisplay()
    }

    fun onOperatorClick(view: View) {
        if (isLastDigit) {
            inputString += (view as Button).text
            isLastDigit = false
            resultString = ""
            updateDisplay()
        }
    }

    fun onBackspaceClick(view: View) {
        if (inputString.isNotEmpty()) {
            inputString = inputString.dropLast(1)
            if (inputString.isEmpty()) {
                onAllClearClick(view)
            } else {
                val lastChar = inputString.last()
                if (lastChar.isDigit()) {
                    isLastDigit = true
                    evaluateExpr()
                    updateDisplay()
                } else {
                    resultString = ""
                    isLastDigit = false
                    updateDisplay()
                }
            }
        }
    }

    private fun evaluateExpr() {
        if (isLastDigit) {
            val expr = ExpressionBuilder(inputString).build()
            resultString = try {
                isErrorState = false
                expr.evaluate().toString()
            } catch (e: ArithmeticException) {
                isErrorState = true
                ""
            } catch (e: Throwable) {
                isErrorState = true
                Toast.makeText(this, "Unknown Error while evaluating", Toast.LENGTH_SHORT).show()
                ""
            }
        }
    }


}