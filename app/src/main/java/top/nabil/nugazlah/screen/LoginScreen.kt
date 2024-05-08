package top.nabil.nugazlah.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import top.nabil.nugazlah.R
import top.nabil.nugazlah.ui.theme.WhitePlain
import top.nabil.nugazlah.ui.theme.WhiteTypo


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(R.drawable.bg_landing),
                contentDescription = null
            )
            Row(
                modifier = Modifier
                    .shadow(
                        elevation = 24.dp,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .background(WhitePlain)
                    .clickable {
                        navController.navigate(Screen.HomeScreen)
                    }
                    .padding(vertical = 4.dp, horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.google_logo),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(id = R.string.login_text),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.headline_landing_top),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WhiteTypo
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.headline_landing_bottom),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WhiteTypo
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
