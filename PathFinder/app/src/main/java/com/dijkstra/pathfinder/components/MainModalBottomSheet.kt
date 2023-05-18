package com.dijkstra.pathfinder.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    nowLocation: String,
    destination: String,
    countdownText: String,
    onClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            BottomSheetTextItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 8.dp),
                label = stringResource(id = R.string.now_location),
                content = nowLocation
            ) // 현재 위치

            Divider()

            BottomSheetTextItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, bottom = 36.dp),
                label = stringResource(id = R.string.destination),
                content = destination
            )

            Text(
                text = countdownText + stringResource(id = R.string.auto_close),
                color = Color.Black,
                fontFamily = nanumSquareNeo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                onClick = { onClick.invoke() }
            ) {
                Text(
                    text = stringResource(id = R.string.start_nav),
                    color = Color.White,
                    fontFamily = nanumSquareNeo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            } // End of Button
        } // End of Column
    } // End of ModalBottomSheet
} // End of MainModalBottomSheet

@Composable
fun BottomSheetTextItem(
    modifier: Modifier = Modifier,
    label: String = "현재 위치",
    content: String = "1층 입구"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start

    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontFamily = nanumSquareNeo,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            color = Color.Black,
            fontFamily = nanumSquareNeo,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    }
} // End of BottomSheetTextItem