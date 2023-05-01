package com.dijkstra.pathfinder.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun AutoCompleteSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = SearchBarDefaults.dockedShape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions,
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.zIndex(1f)
    ) {
        Column {
            InputField(
                value = value,
                placeholder = placeholder,
                onValueChanged = onValueChange,
                onActiveChange = onActiveChange,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                interactionSource = interactionSource,
                keyboardActions = keyboardActions
            )

            AnimatedVisibility(
                visible = active && value.isNotEmpty(),
                modifier = Modifier.background(Color.Transparent),
                enter = DockedEnterTransition,
                exit = DockedExitTransition
            ) {
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val maxHeight = remember(screenHeight) {
                    screenHeight * DockedActiveTableMaxHeightScreenRatio
                }

                Card(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .heightIn(max = maxHeight)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(14.dp),
                            clip = false,
                            ambientColor = Color.LightGray,
                            spotColor = Color.DarkGray
                        ),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Column(
                        modifier = Modifier.background(Color.Transparent),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            color = Color.Transparent
                        )
                        content()
                    }
                }
            } // End of AnimatedVisibility
        } // End of Column
    } // End of Surface

    LaunchedEffect(active) {
        if (!active) {
            delay(AnimationDelayMillis.toLong())
            focusManager.clearFocus()
        }
    }

    BackHandler(enabled = active) {
        onActiveChange(false)
    }

} // End of AutoCompleteSearchBar

private const val DockedActiveTableMaxHeightScreenRatio: Float = 2f / 3f
private val DockedActiveTableMinHeight: Dp = 240.dp

// Animation specs
private const val AnimationEnterDurationMillis: Int = 600
private const val AnimationExitDurationMillis: Int = 350
private const val AnimationDelayMillis: Int = 100
private val AnimationEnterEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
private val AnimationExitEasing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f)
private val AnimationEnterFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val AnimationEnterSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val DockedEnterTransition: EnterTransition =
    fadeIn(AnimationEnterFloatSpec) + expandVertically(AnimationEnterSizeSpec)
private val DockedExitTransition: ExitTransition =
    fadeOut(AnimationExitFloatSpec) + shrinkVertically(AnimationExitSizeSpec)