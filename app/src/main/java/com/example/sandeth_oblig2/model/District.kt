package com.example.sandeth_oblig2.model

data class District(
    val results: Map<AlpacaParty?, Int>,
    val totalVotes: Int,
    val text: String
)