package com.example.sandeth_oblig2.data

import com.example.sandeth_oblig2.model.AlpacaParty
import com.example.sandeth_oblig2.model.District

data class AlpacaUiState(
    val parties: List<AlpacaParty>,
    var currentDistrict: District?,
    val allDistricts: List<District>?
)