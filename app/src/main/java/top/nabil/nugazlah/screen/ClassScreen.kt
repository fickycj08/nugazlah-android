package top.nabil.nugazlah.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import top.nabil.nugazlah.data.TaskCardState
import top.nabil.nugazlah.ui.component.CustomTopAppBar
import top.nabil.nugazlah.ui.component.FloatingActionButtonCustom
import top.nabil.nugazlah.ui.component.TaskCard
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.theme.WhitePlain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isTutorialDialogOpen by remember { mutableStateOf(false) }
    val onDismissRequest = { isTutorialDialogOpen = !isTutorialDialogOpen }

    val tasks = listOf(
        TaskCardState(
            id = "1", title = "PengProf (Pengembangan Profesionalisme)",
            description = "Biar kita profesional dalam menguli bang, ntar bistu kita kaya",
            deadline = "15:00 Minggu 01 Jan 2024",
            reminderIcon = "☠\uFE0F",
            assignmentTypeIcon = "\uD83D\uDDF3\uFE0F"
        ),
        TaskCardState(
            id = "2", title = "Tugas UI/UX untuk project PT.2",
            description = "Baca artikel ilmiah yang berkaitan dengan mata pelajaran yang sedang dipelajari.\n" +
                    "Tulis analisis tentang metodologi penelitian, temuan utama, implikasi, dan kelemahan atau kekuatan artikel tersebut.\n" +
                    "Diskusikan relevansi artikel dengan topik yang telah dipelajari dalam kelas.",
            deadline = "15:00 Minggu 01 Jan 2024",
            reminderIcon = "☠\uFE0F",
            assignmentTypeIcon = "\uD83D\uDDF3\uFE0F"
        ),
        TaskCardState(
            id = "3", title = "PengProf (Pengembangan Profesionalisme)",
            description = "Biar kita profesional dalam menguli bang, ntar bistu kita kaya",
            deadline = "15:00 Minggu 01 Jan 2024",
            reminderIcon = "☠\uFE0F",
            assignmentTypeIcon = "\uD83D\uDDF3\uFE0F"
        ),
        TaskCardState(
            id = "4", title = "Tugas UI/UX untuk project PT.2",
            description = "Baca artikel ilmiah yang berkaitan dengan mata pelajaran yang sedang dipelajari.\n" +
                    "Tulis analisis tentang metodologi penelitian, temuan utama, implikasi, dan kelemahan atau kekuatan artikel tersebut.\n" +
                    "Diskusikan relevansi artikel dengan topik yang telah dipelajari dalam kelas.",
            deadline = "15:00 Minggu 01 Jan 2024",
            reminderIcon = "☠\uFE0F",
            assignmentTypeIcon = "\uD83D\uDDF3\uFE0F"
        ),
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
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "PengProf (Pengembangan Profesionalisme Pengembangan)",
                        color = WhitePlain,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        maxLines = 2,
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButtonCustom(
                modifier = Modifier
                    .padding(end = 8.dp),
                actionLogo = Icons.Rounded.Add
            ) { navController.navigate(Screen.AddTaskScreen) }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(items = tasks, key = { it.id }) {
                TaskCard(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.DetailTaskScreen)
                        }
                        .padding(bottom = 8.dp),
                    data = it,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }

    if (isTutorialDialogOpen) {
        TutorialDialog {
            onDismissRequest()
        }
    }
}
