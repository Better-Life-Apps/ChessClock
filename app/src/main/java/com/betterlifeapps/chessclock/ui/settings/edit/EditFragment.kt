package com.betterlifeapps.chessclock.ui.settings.edit

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.ui.settings.edit.TimerMode.ConstantTime
import com.betterlifeapps.chessclock.ui.settings.edit.TimerMode.NoAddition
import com.betterlifeapps.chessclock.ui.settings.edit.TimerMode.TimeAddition
import com.betterlifeapps.std.BaseComposeFragment
import com.betterlifeapps.std.ui.UiTextField
import com.betterlifeapps.std.ui.composables.UiButton
import com.betterlifeapps.std.ui.composables.UiToolbar
import com.betterlifeapps.std.ui.composables.VSpacer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditFragment : BaseComposeFragment() {

    val viewModel: EditViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.commandFlow
            .onEach {
                when (it) {
                    is EditViewModel.EditScreenCommands.Finish -> findNavController().navigateUp()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @Composable
    override fun View() {
        EditScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditScreen(viewModel: EditViewModel) {
    val player1Mode by viewModel.player1Mode.collectAsState()
    val onPlayer1ModeChanged = { newMode: TimerMode ->
        viewModel.updatePlayer1Mode(newMode)
    }
    val player2Mode by viewModel.player2Mode.collectAsState()
    val onPlayer2ModeChanged = { newMode: TimerMode ->
        viewModel.updatePlayer2Mode(newMode)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UiToolbar(
            text = stringResource(id = R.string.new_game_mode),
            onBackButtonClick = viewModel::onBackClicked
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val name by viewModel.name.collectAsState()
            UiTextField(
                value = name,
                onValueChange = viewModel::updateName,
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(R.string.name),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                )
            )
            VSpacer(height = 8)
            PlayerContainer(R.string.player_1, player1Mode, onPlayer1ModeChanged)
            VSpacer(height = 8)
            PlayerContainer(R.string.player_2, player2Mode, onPlayer2ModeChanged)
            VSpacer(height = 32)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                UiButton(stringRes = R.string.confirm, onClick = viewModel::onDoneButtonClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun PlayerContainer(
    @StringRes titleRes: Int,
    mode: TimerMode,
    onModeChanged: (TimerMode) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(text = stringResource(id = titleRes))
    VSpacer(height = 16)
    val items = listOf(ConstantTime(), TimeAddition(), NoAddition())
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded
        }
    ) {
        UiTextField(
            readOnly = true,
            value = stringResource(id = mode.titleRes),
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onModeChanged(item)
                        isExpanded = false
                    }
                ) {
                    Text(text = stringResource(id = item.titleRes))
                }
            }
        }
    }
    Column(Modifier.fillMaxWidth()) {
        when (mode) {
            is ConstantTime -> {
                UiTextField(
                    value = mode.timePerTurn,
                    onValueChange = {
                        val newString = it.substringBefore(':') +
                                ':' +
                                it.substringAfter(':').take(2)

                        if (newString.matches(regex)) {
                            onModeChanged(mode.copy(timePerTurn = newString))
                        }
                    },
                    label = {
                        Text(
                            text =
                            stringResource(R.string.label_time_per_turn)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
            is TimeAddition -> {
                UiTextField(
                    value = mode.startTime, onValueChange = {
                        val newString = it.substringBefore(':') +
                                ':' +
                                it.substringAfter(':').take(2)

                        if (newString.matches(regex)) {
                            onModeChanged(mode.copy(startTime = newString))
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.label_start_time)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                UiTextField(
                    value = mode.addition, onValueChange = {
                        val newString = it.substringBefore(':') +
                                ':' +
                                it.substringAfter(':').take(2)

                        if (newString.matches(regex)) {
                            onModeChanged(mode.copy(addition = newString))
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.label_time_addition)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
            is NoAddition -> {
                UiTextField(
                    value = mode.startTime, onValueChange = {
                        val newString = it.substringBefore(':') +
                                ':' +
                                it.substringAfter(':').take(2)

                        if (newString.matches(regex)) {
                            onModeChanged(mode.copy(startTime = newString))
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.label_start_time)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
        }
    }
}

//Optional any digit, then optional any digit, then any digit, then :, then digit 1-5, then any digit
private val regex = "^\\d?\\d?\\d:[0-5]\\d\$".toRegex()

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEditScreen() {
    EditScreen(hiltViewModel())
}