package top.nabil.nugazlah.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import top.nabil.nugazlah.R
import top.nabil.nugazlah.data.ClassCardState
import top.nabil.nugazlah.ui.component.ClassCard
import top.nabil.nugazlah.ui.component.CustomButton
import top.nabil.nugazlah.ui.component.CustomInput
import top.nabil.nugazlah.ui.component.CustomTextArea
import top.nabil.nugazlah.ui.component.FloatingActionButtonCustom
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.theme.JetbrainsMono
import top.nabil.nugazlah.ui.theme.PurpleSurface
import top.nabil.nugazlah.ui.theme.WhitePlain
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isDialogJoinRoomOpen by remember { mutableStateOf(false) }
    var isDialogMakeRoomOpen by remember { mutableStateOf(false) }

    var classCode by remember { mutableStateOf("") }

    var classLecturer by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var classDescription by remember { mutableStateOf("") }
    var classIcon by remember { mutableStateOf("") }

    val classes = listOf(
        ClassCardState(
            id = "1", title = "PengProf (Pengembangan Profesionalisme)",
            description = "Biar kita profesional dalam menguli bang, ntar bistu kita kaya",
            lecturer = "Rahayu siapa gitu - RHP",
            icon = "\uD83D\uDE80"
        ),
        ClassCardState(
            id = "2", title = "PengProf (Pengembangan Profesionalisme)",
            description = "Biar kita profesional dalam menguli bang, ntar bistu kita kaya",
            lecturer = "Rahayu siapa gitu - RHP",
            icon = "\uD83D\uDE80"
        ),
        ClassCardState(
            id = "3", title = "PengProf (Pengembangan Profesionalisme)",
            description = "Biar kita profesional dalam menguli bang, ntar bistu kita kaya",
            lecturer = "Rahayu siapa gitu - RHP",
            icon = "\uD83D\uDE80"
        )
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.logo),
                    color = WhitePlain,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            })
        },
        floatingActionButton = {
            FloatingActionButtonCustom(
                modifier = Modifier
                    .padding(end = 8.dp),
                actionLogo = Icons.Rounded.Add
            ) { isDialogOpen = !isDialogOpen }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(items = classes, key = { it.id }) {
                ClassCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    data = it,
                    onClick = {
                        navController.navigate(Screen.ClassScreen)
                    }
                )
            }
        }
    }

    if (isDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { isDialogOpen = !isDialogOpen },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isDialogOpen = !isDialogOpen
                        isDialogMakeRoomOpen = !isDialogMakeRoomOpen
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.make_room),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isDialogOpen = !isDialogOpen
                        isDialogJoinRoomOpen = !isDialogJoinRoomOpen
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.join_room),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    if (isDialogJoinRoomOpen) {
        BasicAlertDialog(
            onDismissRequest = { isDialogJoinRoomOpen = !isDialogJoinRoomOpen },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_code_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.class_code_placeholder),
                    text = classCode,
                    textStyle = MaterialTheme.typography.labelLarge,
                    onChange = {
                        if (it.length <= 6) {
                            classCode = it.uppercase(Locale.ROOT)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomButton(
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            isDialogJoinRoomOpen = !isDialogJoinRoomOpen
                        },
                    )
                }
            }
        }
    }

    if (isDialogMakeRoomOpen) {
        BasicAlertDialog(
            onDismissRequest = { isDialogMakeRoomOpen = !isDialogMakeRoomOpen },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.lecturer_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.lecturer_input_placeholder),
                    text = classLecturer,
                    onChange = {
                        classLecturer = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_name_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.class_name_placeholder),
                    text = className,
                    onChange = {
                        className = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_description_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomTextArea(
                    modifier = Modifier.padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.class_description_placeholder),
                    text = classDescription,
                    height = 100.dp,
                    onChange = {
                        classDescription = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 2.dp),
                            text = stringResource(id = R.string.class_icon_label),
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = JetbrainsMono,
                            color = WhitePlain
                        )
                        CustomInput(
                            hint = stringResource(id = R.string.class_icon_placeholder),
                            text = classIcon,
                            onChange = {
                                if (it.length <= 3) {
                                    classIcon = it
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            isDialogMakeRoomOpen = !isDialogMakeRoomOpen
                        },
                    )
                }
            }
        }
    }
}
