package com.example.sandeth_oblig2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.example.sandeth_oblig2.R
import com.example.sandeth_oblig2.model.AlpacaParty
import com.example.sandeth_oblig2.model.District

@Composable
fun PartyCard(partyData: AlpacaParty, district: District) {

    val percentage: Double = ((district.results[partyData])!!.toDouble()*100/(district.totalVotes))
    val roundedPercentage = String.format("%.1f", percentage)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(Color(partyData.color.toColorInt()))
            )

            Text(
                text = partyData.name,
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 20.dp),
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                model = partyData.img,
                contentDescription = stringResource(R.string.pictureDescription),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Text(
                text = partyData.leader,
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 20.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Stemmer: ${(district.results[partyData])} \nProsent: $roundedPercentage",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

        }
    }
}