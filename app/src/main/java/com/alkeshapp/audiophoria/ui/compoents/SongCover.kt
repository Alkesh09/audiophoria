package com.alkeshapp.audiophoria.ui.compoents

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alkeshapp.audiophoria.common.Constants
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongSmallCover(
    modifier: Modifier = Modifier,
    coverId: String,
    size: Dp = 40.dp,
    shape: Shape = CircleShape,
) {
    GlideImage(
        model = "${Constants.IMAGE_BASE_URL}$coverId",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(shape = shape),
        contentDescription = null
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongBigCover(
    modifier: Modifier = Modifier,
    coverId: String,
    size: Dp = 300.dp,
) {
    GlideImage(
        model = "${Constants.IMAGE_BASE_URL}$coverId",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(MaterialTheme.shapes.small),
        contentDescription = null
    )
}