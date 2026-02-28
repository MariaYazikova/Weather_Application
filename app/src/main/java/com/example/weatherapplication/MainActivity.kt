package com.example.weatherapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApplication()
        }
    }
}

//основной интерфейс
@Composable
fun WeatherApplication() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {

        val cities = listOf(
            "Москва",
            "Санкт-Петербург",
            "Самара",
            "Нижний Новгород",
            "Казань",
            "Екатеринбург"
        )

        val periods = listOf(
            "Сегодня",
            "Завтра",
            "3 дня",
            "10 дней"
        )

        var selectedCity by remember { mutableStateOf(cities[0]) }
        var selectedPeriod by remember { mutableStateOf(periods[0]) }

        val context = androidx.compose.ui.platform.LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Выберите город",
                color = Color.LightGray,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )

            DropdownMenuBox(cities, selectedCity) {
                selectedCity = it
            }

            Text(
                text = "Выберите период",
                color = Color.LightGray,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )

            DropdownMenuBox(periods, selectedPeriod) {
                selectedPeriod = it
            }
            //кнопка запуска
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                onClick = {

                    val cityUrl = when (selectedCity) {
                        "Москва" -> "https://www.gismeteo.ru/weather-moscow-4368/"
                        "Санкт-Петербург" -> "https://www.gismeteo.ru/weather-sankt-peterburg-4079/"
                        "Самара" -> "https://www.gismeteo.ru/weather-samara-4618/"
                        "Нижний Новгород" -> "https://www.gismeteo.ru/weather-nizhny-novgorod-4355/"
                        "Казань" -> "https://www.gismeteo.ru/weather-kazan-4364/"
                        "Екатеринбург" -> "https://www.gismeteo.ru/weather-yekaterinburg-4517/"
                        else -> ""
                    }

                    val finalUrl = when (selectedPeriod) {
                        "Сегодня" -> cityUrl
                        "Завтра" -> cityUrl + "tomorrow/"
                        "3 дня" -> cityUrl + "3-days/"
                        "10 дней" -> cityUrl + "10-days/"
                        else -> cityUrl
                    }
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Браузер не найден", Toast.LENGTH_LONG).show()
                    }

                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFF1F1F1F),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Запустить",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

//выпадающий список
@Composable
fun DropdownMenuBox(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    var buttonWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        //кнопка открытия списка
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .onGloballyPositioned { coordinates ->
                    buttonWidth = coordinates.size.width
                },
            onClick = { expanded = true },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFF1F1F1F),
                contentColor = Color.White
            )
        ) {
            Text(
                text = selectedItem,
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp
            )
        }
        //список
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFF1F1F1F),
            modifier = Modifier.width(with(LocalDensity.current) { buttonWidth.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}