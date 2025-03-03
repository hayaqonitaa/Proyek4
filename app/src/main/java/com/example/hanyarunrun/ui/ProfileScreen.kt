package com.example.hanyarunrun.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.hanyarunrun.data.ProfileEntity
import com.example.hanyarunrun.utils.ImageUtils
import com.example.hanyarunrun.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var profileImagePath by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Ambil data profil dari database saat pertama kali diakses
    LaunchedEffect(Unit) {
        profileViewModel.getProfile().observeForever { profile ->
            profile?.let {
                studentName = it.studentName
                studentId = it.studentId
                studentEmail = it.studentEmail
                profileImagePath = it.profileUri // Ambil path gambar yang tersimpan
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = ImageUtils.saveImageToInternalStorage(context, it)
            if (savedPath != null) {
                profileImagePath = savedPath
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (profileImagePath != null) {
                    AsyncImage(
                        model = profileImagePath,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Picture")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = studentId,
                    onValueChange = { studentId = it },
                    label = { Text("Student ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = studentEmail,
                    onValueChange = { studentEmail = it },
                    label = { Text("Student Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Button untuk memilih gambar
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Photo")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val profile = ProfileEntity(
                                id = 1,  // Gunakan ID tetap agar data di-update, bukan duplikasi
                                studentName = studentName,
                                studentId = studentId,
                                studentEmail = studentEmail,
                                profileUri = profileImagePath
                            )
                            profileViewModel.saveProfile(profile)
                        }
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            } else {
                Text(
                    text = studentName.ifEmpty { "Nama belum diisi" },
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ID: ${studentId.ifEmpty { "Belum diisi" }}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = studentEmail.ifEmpty { "Email belum diisi" },
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}
