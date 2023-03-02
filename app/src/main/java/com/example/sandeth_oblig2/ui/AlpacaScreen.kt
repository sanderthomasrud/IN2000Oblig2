package com.example.sandeth_oblig2.ui

import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sandeth_oblig2.ui.components.PartyCard
import com.example.sandeth_oblig2.viewmodels.AlpacaViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sandeth_oblig2.R
import com.example.sandeth_oblig2.data.AlpacaUiState


@Composable
fun AlpacaScreen(alpacaViewModel: AlpacaViewModel = viewModel()) {

    val alpacaUiState by alpacaViewModel.alpacaUiState.collectAsState()

    Column {
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
fun ViewVotingResults(alpacaUiState: AlpacaUiState, viewModel: AlpacaViewModel) {
    val options = stringArrayResource(R.array.DistriktListe)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Distrikt 1") }
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
                .padding(top = 50.dp, bottom = 20.dp),

            ) {
            TextField(
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                value = selectedOptionText,
                onValueChange = { },
                label = { Text(stringResource(R.string.DDLText)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
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