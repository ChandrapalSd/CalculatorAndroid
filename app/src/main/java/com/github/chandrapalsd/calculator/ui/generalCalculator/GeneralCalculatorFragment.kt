package com.github.chandrapalsd.calculator.ui.generalCalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.github.chandrapalsd.calculator.databinding.FragmentGeneralCalculatorBinding

class GeneralCalculatorFragment : Fragment() {

    private var _binding: FragmentGeneralCalculatorBinding? = null
    private val binding: FragmentGeneralCalculatorBinding get() = _binding!!

    private val viewModel: GeneralCalculatorViewModel by viewModels<GeneralCalculatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralCalculatorBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()

        viewModel.uiState.observe(viewLifecycleOwner) {
            updateDisplay()
        }
    }

    private fun setOnClickListeners() {
        binding.apply {

            val digitButtons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            digitButtons.forEach {
                it.setOnClickListener { view ->
                    viewModel.onDigitClick((view as Button).text.toString())
                }
            }

            val operatorButtons = listOf(btnAdd, btnSubtract, btnMultiply, btnDivide, btnModulo)
            operatorButtons.forEach {
                it.setOnClickListener { view ->
                    viewModel.onOperatorClick((view as Button).text.toString())
                }
            }

            btnDecimal.setOnClickListener{ viewModel.onDecimalClick(".") }
            btnEqual.setOnClickListener{ viewModel.onEqualClick() }
            btnClear.setOnClickListener { viewModel.onClearClick() }
            btnAllclear.setOnClickListener { viewModel.onAllClearClick() }
            btnBackspace.setOnClickListener{viewModel.onBackspaceClick()}

        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun updateDisplay() {
        viewModel.uiState.value!!.apply {
            binding.tvInput.text = inputString
            val result = "=$resultString"
            binding.tvResult.text = result
        }
    }

}