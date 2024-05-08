package top.nabil.nugazlah.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import top.nabil.nugazlah.R
import top.nabil.nugazlah.data.TaskData
import top.nabil.nugazlah.data.taskTypeIcon
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.component.TutorialIcon
import top.nabil.nugazlah.ui.theme.BlackTypo
import top.nabil.nugazlah.ui.theme.DarkSurface
import top.nabil.nugazlah.ui.theme.GreenCard
import top.nabil.nugazlah.ui.theme.WhitePlain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isTutorialDialogOpen by remember { mutableStateOf(false) }

    val onDismissRequest = { isTutorialDialogOpen = !isTutorialDialogOpen }

    val task = TaskData(
        id = "88sds7d6sd522323m23",
        title = "Tugas UI/UX untuk project PT.2",
        description = "Baca artikel ilmiah yang berkaitan dengan mata pelajaran yang sedang dipelajari. Tulis analisis tentang metodologi penelitian, temuan utama, implikasi, dan kelemahan atau kekuatan artikel tersebut. Diskusikan relevansi artikel dengan topik yang telah dipelajari dalam kelas.",
        deadline = "23:59 20 April 2024", // Kurang 2 jam lagi bang
        taskDetail = "https://google.com",
        taskSubmission = "https://google.com",
        taskType = "Proposal"
    )

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
                actions = {
                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = { }) {
                        Text(
                            text = "✅",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                shadow = Shadow(
                                    color = BlackTypo,
                                    offset = Offset(4.0f, 8.0f),
                                    blurRadius = 3f
                                )
                            )
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.task_detail),
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
            TextBox(text = task.title, textStyle = MaterialTheme.typography.titleLarge)

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
            TextBox(
                text = task.description,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.1.sp
                )
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
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(GreenCard, MaterialTheme.shapes.small)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(id = R.string.task_source_wording),
                    style = MaterialTheme.typography.labelLarge
                )
            }

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
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(GreenCard, MaterialTheme.shapes.small)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(id = R.string.task_submission_wording),
                    style = MaterialTheme.typography.labelLarge
                )
            }
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
                    .clip(MaterialTheme.shapes.medium)
                    .background(WhitePlain)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TutorialIcon(
                    modifier = Modifier.padding(end = 16.dp),
                    icon = taskTypeIcon[task.taskType] ?: ""
                ) {
                    onDismissRequest()
                }
                Text(
                    text = task.taskType,
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
            TextBox(
                modifier = Modifier
                    .fillMaxWidth(),
                text = task.deadline,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    if (isTutorialDialogOpen) {
        TutorialDialog {
            onDismissRequest()
        }
    }
}

@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(WhitePlain)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text, style = textStyle
        )
    }
}
