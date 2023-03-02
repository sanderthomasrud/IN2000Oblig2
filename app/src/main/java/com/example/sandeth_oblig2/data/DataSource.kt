package com.example.sandeth_oblig2.data

import android.util.Log
import com.example.sandeth_oblig2.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import java.io.InputStream

class DataSource(private val partyPath: String, val district1: String, val district2: String, val district3: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchAlpacaParties(): List<AlpacaParty> {

        val body: AlpacaPartyList = client.get(partyPath).body()

        return body.parties
    }

    suspend fun fetchDistrictJSON(path: String, parties: Map<Int, AlpacaParty>, text: String): District {

        val body: List<Vote> = client.get(path).body()
        var party1 = 0
        var party2 = 0
        var party3 = 0
        var party4 = 0

        body.forEach {
            when (it.id) {
                1 -> party1 += 1
                2 -> party2 += 1
                3 -> party3 += 1
                4 -> party4 += 1
            }
        }

        val results = mapOf(parties[1] to party1, parties[2] to party2, parties[3] to party3, parties[4] to party4)

        val totalVotes = party1 + party2 + party3 + party4

        return District(results, totalVotes, text)
    }

    suspend fun fetchDistrictXML(path: String, parties: Map<Int, AlpacaParty>, text: String): District {
        val body: String = client.get(path).body()

        val inputStream: InputStream = body.byteInputStream()

        val results: List<PartyResult> = XmlParser().parse(inputStream)

        Log.d("results", results.toString())

        var totalVotes = 0

        results.forEach {
            //map[it.id!!] = it
            totalVotes += it.votes!!
        }

        val map = mapOf(parties[1] to results[0].votes!!, parties[2] to results[1].votes!!, parties[3] to results[2].votes!!, parties[4] to results[3].votes!!)

        return District(map, totalVotes, text)

    }

}