package com.example.hanyarunrun.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.hanyarunrun.R

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000) // Tampilkan splash selama 2 detik
        navController.navigate("list") {
            popUpTo("splash") { inclusive = true } // Hapus splash dari back stack
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.gunung),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Haya Qonita Amani", fontSize = 24.sp)
            Text(text = "231511013", fontSize = 16.sp)
        }
    }
}
