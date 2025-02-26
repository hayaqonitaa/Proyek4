package com.example.hanyarunrun.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.ui.components.JetsnackCard
import com.example.hanyarunrun.ui.components.JetsnackButton
import com.example.hanyarunrun.ui.components.JetsnackDivider
import com.example.hanyarunrun.ui.theme_navbar.JetsnackTheme

@Composable
fun DataListScreen(navController: NavHostController, viewModel: DataViewModel) {
    val dataList by viewModel.dataList.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<DataEntity?>(null) as MutableState<DataEntity?> }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black, Color.Black)
                )
            )

    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (dataList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Data Available", style = MaterialTheme.typography.headlineMedium)
                }
            } else {
                JetsnackTheme {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dataList) { item ->
                            Column {
                                JetsnackCard(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Provinsi: ${item.namaProvinsi} (${item.kodeProvinsi})",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Kabupaten/Kota: ${item.namaKabupatenKota} (${item.kodeKabupatenKota})",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Total: ${item.total} ${item.satuan}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Tahun: ${item.tahun}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            JetsnackButton(
                                                onClick = {
                                                    navController.navigate("edit/${item.id}")
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(text = "Edit")
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            JetsnackButton(
                                                onClick = {
                                                    selectedItem = item
                                                    showDialog = true
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                            ) {
                                                Text(text = "Delete")
                                            }
                                        }
                                    }
                                }
                                JetsnackTheme{
                                    JetsnackDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("form") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "+", style = MaterialTheme.typography.headlineMedium)
        }
    }

    // Popup konfirmasi delete
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus data ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedItem?.let { item ->
                            viewModel.deleteData(item)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
