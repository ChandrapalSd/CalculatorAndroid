package com.github.chandrapalsd.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.github.chandrapalsd.calculator.databinding.FragmentGeneralCalculatorBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class GeneralCalculatorFragment : Fragment() {

    private var _binding: FragmentGeneralCalculatorBinding? = null
    private val binding: FragmentGeneralCalculatorBinding get() = _binding!!

    private var isLastDigit = false
    private var isErrorState = false
    private var inputString: String = ""
    private var resultString: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralCalculatorBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListerners()
    }

    fun setOnClickListerners() {
        binding.apply {

            val digitButtons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            digitButtons.forEach { it.setOnClickListener(::onDigitClick) }
            val operatorButtons = listOf(btnAdd, btnSubtract, btnMultiply, btnDivide, btnModulo)
            operatorButtons.forEach { it.setOnClickListener(::onOperatorClick) }

            btnDecimal.setOnClickListener(::onDecimalClick)
            btnEqual.setOnClickListener(::onEqualClick)
            btnClear.setOnClickListener(::onClearClick)
            btnAllclear.setOnClickListener(::onAllClearClick)
            btnBackspace.setOnClickListener(::onBackspaceClick)

        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
    private fun updateDisplay() {
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
                Toast.makeText(activity, "Unknown Error while evaluating", Toast.LENGTH_SHORT).show()
                ""
            }
        }
    }


}