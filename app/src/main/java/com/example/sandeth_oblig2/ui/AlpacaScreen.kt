package com.example.sandeth_oblig2.ui.theme

import android.util.Log
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sandeth_oblig2.ui.components.PartyCard
import com.example.sandeth_oblig2.viewmodels.AlpacaViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.sandeth_oblig2.data.AlpacaUiState


@Composable
fun AlpacaScreen(alpacaViewModel: AlpacaViewModel = viewModel()) {

    val alpacaUiState by alpacaViewModel.alpacaUiState.collectAsState()

    Log.d("alpacaUiState123",alpacaUiState.currentDistrict.toString())
    Log.d("sjekk1234", alpacaUiState.allDistricts.toString())

    Column() {
        ViewVotingResults(alpacaUiState, alpacaViewModel)
        AlpacaPartyView(alpacaUiState)
    }

}

@Composable
fun AlpacaPartyView(alpacaUiState: AlpacaUiState) {
    val parties = alpacaUiState.parties

    LazyColumn {
        items(parties) {partyData ->
            PartyCard(partyData = partyData, district = alpacaUiState.currentDistrict!!)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewVotingResults(alpacaUiState: AlpacaUiState, viewModel: AlpacaViewModel) { //legg til parametre
    val options = listOf<String>("Distrikt 1", "Distrikt 2", "Distrikt 3")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") } // valgt alternativ
    var selectedDistrict by remember { mutableStateOf(alpacaUiState.currentDistrict) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .padding(top = 50.dp, bottom = 50.dp),

            ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedOptionText,
                onValueChange = { selectedOptionText = it },
                label = { Text("Velg distrikt...") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            // filter options based on text field value
            val filteringOptions = options.filter { it.contains(selectedOptionText, ignoreCase = true) }
            if (filteringOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    filteringOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                focusManager.clearFocus()

                                alpacaUiState.allDistricts?.forEach {
                                    if (it.text == selectedOptionText) {
                                        selectedDistrict = it
                                        viewModel.changeSelectedDistrict(selectedDistrict!!)
                                        Log.d("partyViewSjekk", alpacaUiState.toString())
                                    }
                                }

                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }

}