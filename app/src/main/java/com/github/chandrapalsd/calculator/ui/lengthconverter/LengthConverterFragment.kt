package com.github.chandrapalsd.calculator.ui.lengthconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.github.chandrapalsd.calculator.databinding.FragmentLengthConverterBinding

class LengthConverterFragment : Fragment() {

    private var _binding: FragmentLengthConverterBinding? = null
    private val binding: FragmentLengthConverterBinding get() = _binding!!


    private val viewModel: LengthConverterViewModel by viewModels<LengthConverterViewModel> {
        LengthConverterViewModelFactory(
            requireActivity().applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLengthConverterBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            viewModel.lengthUnitsName
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerUnit1.adapter = it
            binding.spinnerUnit2.adapter = it
        }

        binding.spinnerUnit1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setUnit(unit1Index=position)
                binding.tvUnit1Symbol.text = viewModel.lengthUnitsSymbols[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        binding.spinnerUnit2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setUnit(unit2Index=position)
                binding.tvUnit2Symbol.text = viewModel.lengthUnitsSymbols[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        viewModel.inputValue.observe(viewLifecycleOwner){
            viewModel.updateResult()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}