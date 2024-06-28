package com.example.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DisplayParkings(parkings: List<Parking>) {
    val context = LocalContext.current

    LazyColumn {
        items(parkings) { parking ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .padding(4.dp)
                    .background(Color(0xFFE0E0E0))
                    .clickable {
                        Toast.makeText(context, parking.parkingName, Toast.LENGTH_SHORT).show()
                    }
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = parking.parkingName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    parking.DescriptionParking?.let {
                        Text(
                            text = it,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
