package com.github.chandrapalsd.calculator.ui.lengthConverterFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class LengthUnit(val name: String, val unit: String, val mf: Double)

class LengthConverterViewModel(private val appContext: Context) : ViewModel() {

    private lateinit var lengthUnits: Array<LengthUnit>
    lateinit var lengthUnitsName: Array<String>
    lateinit var lengthUnitsSymbols: Array<String>

    var firstSelectedUnitIndex: Int = 0
        private set
    var secondSelectedUnitIndex: Int = 0
        private set

    var inputValue = MutableLiveData<String>("110.0")

    val convertedValue = MutableLiveData<String>()

    init {
        loadLengthUnits()
    }

    fun setUnit(
        unit1Index: Int = firstSelectedUnitIndex,
        unit2Index: Int = secondSelectedUnitIndex
    ) {
        firstSelectedUnitIndex = unit1Index
        secondSelectedUnitIndex = unit2Index
        updateResult()
    }

    fun updateResult() {
        convertedValue.value =
            if (inputValue.value != null && inputValue.value!!.isNotEmpty()) {
                val valueInBase =
                    inputValue.value!!.toDouble() * lengthUnits[firstSelectedUnitIndex].mf
                (valueInBase / lengthUnits[secondSelectedUnitIndex].mf).toString()
            } else {
                ""
            }
    }

    private fun loadLengthUnits() {

        try {
            val jsonStream = appContext.assets.open("length_units.json")
            val rawJson = jsonStream.bufferedReader().use { it.readText() }
            lengthUnits = Json.decodeFromString<Array<LengthUnit>>(rawJson)

            lengthUnitsName = Array(lengthUnits.size) { lengthUnits[it].name }
            lengthUnitsSymbols = Array(lengthUnits.size) { lengthUnits[it].unit }
        } catch (e: Exception) {
            // TODO: Do proper error handling
            Log.e("LengthConverterViewModel", "Error while creating objects from json")
        }

    }

}

class LengthConverterViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //  TODO
        // return if (modelClass.isAssignableFrom(LengthConverterViewModel::class.java)){
        //
        //     LengthConverterViewModel(appContext) as T
        // }
        // else{
        //     throw IllegalArgumentException("ViewModel Not Found")
        // }
        return LengthConverterViewModel(appContext) as T
    }
}
