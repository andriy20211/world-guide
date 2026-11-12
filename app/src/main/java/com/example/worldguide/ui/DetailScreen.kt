package com.example.worldguide.ui

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worldguide.data.remote.NetworkClient
import com.example.worldguide.data.remote.toDomain
import com.example.worldguide.data.remote.toNetworkError
import com.example.worldguide.model.Country
import com.example.worldguide.ui.components.ErrorStateView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    countryId: String?,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var country by remember { mutableStateOf<Country?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Лаунчер для запиту Runtime Permission (POST_NOTIFICATIONS)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Сповіщення увімкнено для ${country?.name?.common}!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "У доступі відмовлено. Функцію сповіщень вимкнено.", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadData() {
        if (!countryId.isNullOrEmpty()) {
            isLoading = true
            errorMessage = null
            try {
                // Корпусна функція через LaunchedEffect
            } catch (e: Exception) { }
        }
    }

    LaunchedEffect(countryId) {
        if (!countryId.isNullOrEmpty()) {
            isLoading = true
            errorMessage = null
            try {
                val dtos = NetworkClient.apiService.getCountryByCode(countryId)
                country = dtos.firstOrNull()?.toDomain()
            } catch (e: Throwable) {
                val netErr = e.toNetworkError()
                errorMessage = netErr.userMessage
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(country?.name?.common ?: "Деталі країни") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (country != null) {
                        // Кнопка з дією, яка запитує Runtime Permission
                        IconButton(onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                Toast.makeText(context, "Сповіщення активовано!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Сповістити")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                ErrorStateView(
                    errorMessage = errorMessage!!,
                    onRetryClick = {
                        // Спроба повторити запит
                        isLoading = true
                    }
                )
            }
            country == null -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Країну з ID «$countryId» не знайдено", style = MaterialTheme.typography.titleMedium)
                }
            }
            else -> {
                val currentCountry = country!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Text(text = currentCountry.name.common, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(text = "Офіційна назва: ${currentCountry.name.official}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        DetailRow(label = "Регіон", value = currentCountry.region)
                        DetailRow(label = "Столиця", value = currentCountry.capital.joinToString(", "))
                        DetailRow(label = "Населення", value = String.format("%,d чол.", currentCountry.population))
                    }
                }
            }
        }
    }
}