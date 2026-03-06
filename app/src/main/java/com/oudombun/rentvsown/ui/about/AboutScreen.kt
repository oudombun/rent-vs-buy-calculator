package com.oudombun.rentvsown.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val versionName = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
    } catch (_: Exception) {
        "1.0"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "How we calculate",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2F2F2F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionTitle("Monthly payment")
            Text(
                text = "We use the standard amortization formula for a fixed-rate loan:",
                fontSize = 14.sp,
                color = Color(0xFF2F2F2F),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "M = P × [r(1+r)^n] / [(1+r)^n − 1]",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF2F2F2F),
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(
                text = "• P = loan amount (principal)\n• r = monthly interest rate (annual rate ÷ 12)\n• n = number of monthly payments",
                fontSize = 13.sp,
                color = Color(0xFF424242),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("What we include")
            BulletItem("Total interest paid over the full loan term.")
            BulletItem("In rent comparison: time renting, rent paid while saving, down payment, loan amount, interest on the loan.")
            BulletItem("Total cost comparison across scenarios (0%, 20%, 40%, … 100% down).")

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Assumptions")
            BulletItem("Fixed interest rate for the entire loan term.")
            BulletItem("No prepayment or extra payments.")
            BulletItem("No PMI (private mortgage insurance) in the calculation.")
            BulletItem("Scenarios assume you save the difference (monthly loan payment minus rent) toward the down payment.")
            BulletItem("This is for estimation only, not financial advice.")

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Rent or Own",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2F2F2F)
            )
            Text(
                text = "Version $versionName",
                fontSize = 13.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2F2F2F),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun BulletItem(text: String) {
    Text(
        text = "• $text",
        fontSize = 14.sp,
        color = Color(0xFF424242),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
