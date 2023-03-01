package com.example.sandeth_oblig2.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandeth_oblig2.data.AlpacaUiState
import com.example.sandeth_oblig2.data.DataSource
import com.example.sandeth_oblig2.model.AlpacaParty
import com.example.sandeth_oblig2.model.District
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlpacaViewModel : ViewModel() {

    private val dataSource = DataSource(
        "https://www.uio.no/studier/emner/matnat/ifi/IN2000/v23/obligatoriske-oppgaver/alpacaparties.json",
        "https://in2000-proxy.ifi.uio.no/alpacaapi/district1",
        "https://in2000-proxy.ifi.uio.no/alpacaapi/district2",
        "https://in2000-proxy.ifi.uio.no/alpacaapi/district3"
    )

    private var selectedDistrict by mutableStateOf(District(mapOf(),0, "" ))

    private val _alpacaUiState = MutableStateFlow(AlpacaUiState(parties = listOf(), currentDistrict = selectedDistrict, allDistricts = null)) //rett opp

    val alpacaUiState: StateFlow<AlpacaUiState> = _alpacaUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val parties = loadParties()
            val partyMap = mapOf(1 to parties[0], 2 to parties[1], 3 to parties[2], 4 to parties[3])

            val results = loadResults(partyMap)

            _alpacaUiState.value = AlpacaUiState(parties = parties, currentDistrict = results[0], results)
        }
    }

    private suspend fun loadParties(): List<AlpacaParty> {
        return dataSource.fetchAlpacaParties()
    }

    private suspend fun loadResults(parties: Map<Int, AlpacaParty>): List<District> {

        val dis1 = dataSource.fetchDistrict12(dataSource.district1, parties, "Distrikt 1")
        val dis2 = dataSource.fetchDistrict12(dataSource.district2, parties, "Distrikt 2")
        val dis3 = dataSource.fetchDistrict3(dataSource.district3, parties, "Distrikt 3")

        return listOf(dis1, dis2, dis3)
    }


    public fun changeSelectedDistrict(district: District) {
        _alpacaUiState.value = alpacaUiState.value.copy(currentDistrict = district)
        Log.d("selectedDistrictSJEKK", selectedDistrict!!.text)
    }


}