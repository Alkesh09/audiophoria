package com.alkeshapp.audiophoria.ui.compoents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkeshapp.audiophoria.R
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.ui.theme.SubTextColor
import com.alkeshapp.audiophoria.ui.theme.TabUnselectedColor


@Composable
fun SongCard(modifier: Modifier = Modifier, song: Song) {
    Row(
        modifier = modifier
    ) {

        SongSmallCover(coverId = song.cover.orEmpty() )

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            Text(
                text = song.name.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = song.artist.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = SubTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {

}