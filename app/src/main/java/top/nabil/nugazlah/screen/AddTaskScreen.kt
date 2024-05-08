package top.nabil.nugazlah.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import top.nabil.nugazlah.R
import top.nabil.nugazlah.data.TaskData
import top.nabil.nugazlah.data.taskTypeIcon
import top.nabil.nugazlah.ui.component.CustomButton
import top.nabil.nugazlah.ui.component.CustomInput
import top.nabil.nugazlah.ui.component.CustomTextArea
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.component.TutorialIcon
import top.nabil.nugazlah.ui.theme.BlackTypo
import top.nabil.nugazlah.ui.theme.GreenCard
import top.nabil.nugazlah.ui.theme.Inter
import top.nabil.nugazlah.ui.theme.PurpleSurface
import top.nabil.nugazlah.ui.theme.PurpleType
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isTutorialDialogOpen by remember { mutableStateOf(false) }
    var isTaskTypeDialogOpen by remember { mutableStateOf(false) }
    val onDismissRequest = { isTutorialDialogOpen = !isTutorialDialogOpen }

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskDetail by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf("Proposal") }
    var taskSubmission by remember { mutableStateOf("") }
    var taskDeadline by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.add_task),
                        color = WhitePlain,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        maxLines = 2,
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_title),
                style = MaterialTheme.typography.labelMedium
            )
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_title_placeholder),
                text = taskTitle,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { taskTitle = it }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_description),
                style = MaterialTheme.typography.labelMedium
            )
            CustomTextArea(
                hint = stringResource(id = R.string.task_description_placeholder),
                text = taskDescription,
                onChange = { taskDescription = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_source),
                style = MaterialTheme.typography.labelMedium
            )
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_detail_placeholder),
                text = taskDetail,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { taskDetail = it }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_submission),
                style = MaterialTheme.typography.labelMedium
            )
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_submission_placeholder),
                text = taskSubmission,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { taskSubmission = it }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_type),
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        isTaskTypeDialogOpen = !isTaskTypeDialogOpen
                    }
                    .clip(MaterialTheme.shapes.medium)
                    .background(WhitePlain)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TutorialIcon(
                    modifier = Modifier.padding(end = 16.dp),
                    icon = taskTypeIcon[taskType] ?: ""
                ) {
                    onDismissRequest()
                }
                Text(
                    text = taskType,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(id = R.string.task_deadline),
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .background(WhitePlain, MaterialTheme.shapes.medium)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 16.dp),
                    text = "\uD83D\uDD53",
                    fontSize = 28.sp
                )
                Text(
                    text = stringResource(id = R.string.task_deadline_placeholder),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Inter,
                    color = WhitePlaceholder
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    text = stringResource(id = R.string.button_text_confirm),
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 16.sp,
                        fontFamily = Inter,
                        color = PurpleType
                    ),
                    onClick = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }

    if (isTutorialDialogOpen) {
        TutorialDialog {
            onDismissRequest()
        }
    }

    if (isTaskTypeDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { isTaskTypeDialogOpen = !isTaskTypeDialogOpen },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (t in taskTypeIcon.keys) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        onClick = {
                            isTaskTypeDialogOpen = !isTaskTypeDialogOpen
                            taskType = t
                        }
                    ) {
                        TutorialIcon(
                            modifier = Modifier.padding(end = 16.dp),
                            icon = taskTypeIcon[t] ?: "",
                        ) {
                            onDismissRequest()
                        }
                        Text(
                            text = t,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

