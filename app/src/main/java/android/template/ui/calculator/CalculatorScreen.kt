package android.template.ui.calculator

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rent vs Loan Calculator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF424242),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { LoanCalculatorSection(state, viewModel) }
            item { RentComparisonSection(state, viewModel) }
        }
    }
}

@Composable
fun LoanCalculatorSection(state: CalculatorState, viewModel: CalculatorViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFF424242),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Text(
                    text = "Loan Calculator",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            // House Price Input
            OutlinedTextField(
                value = state.housePrice,
                onValueChange = { viewModel.updateHousePrice(it) },
                label = { Text("House Price (\$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.housePriceError != null,
                supportingText = state.housePriceError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF424242),
                    focusedLabelColor = Color(0xFF424242),
                    cursorColor = Color(0xFF424242)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Interest Rate Input
            OutlinedTextField(
                value = state.interestRate,
                onValueChange = { viewModel.updateInterestRate(it) },
                label = { Text("Annual Interest Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.interestRateError != null,
                supportingText = state.interestRateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF424242),
                    focusedLabelColor = Color(0xFF424242),
                    cursorColor = Color(0xFF424242)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Loan Period Section
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Loan Period",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF424242)
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleCustomPeriod(!state.isCustomPeriod) }
                ) {
                    Checkbox(
                        checked = state.isCustomPeriod,
                        onCheckedChange = { viewModel.toggleCustomPeriod(it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF424242)
                        )
                    )
                    Text(
                        "Use custom period",
                        fontSize = 15.sp,
                        color = Color(0xFF424242)
                    )
                }

                if (!state.isCustomPeriod) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp)),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        listOf(10, 15, 20).forEach { years ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (state.loanPeriodYears == years) Color(0xFFE8F5E9)
                                        else Color.Transparent
                                    )
                                    .clickable { viewModel.updateLoanPeriod(years) }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (state.loanPeriodYears == years) {
                                        Text(
                                            text = "✓",
                                            fontSize = 14.sp,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }
                                    Text(
                                        text = "$years years",
                                        fontSize = 15.sp,
                                        fontWeight = if (state.loanPeriodYears == years) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (state.loanPeriodYears == years) Color(0xFF424242) else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = state.customPeriod,
                        onValueChange = { viewModel.updateCustomPeriod(it) },
                        label = { Text("Custom Period (years)") },
                        placeholder = { Text("Enter loan period in years") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = state.customPeriodError != null,
                        supportingText = state.customPeriodError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
                            ?: { Text("Enter loan period in years", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF424242),
                            focusedLabelColor = Color(0xFF424242),
                            cursorColor = Color(0xFF424242)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Calculate Button
            Button(
                onClick = { viewModel.calculateLoan() },
                enabled = !state.isCalculatingStep1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF424242),
                    disabledContainerColor = Color(0xFF424242).copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(26.dp)
            ) {
                if (state.isCalculatingStep1) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Calculate Loan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Results
            state.loanCalculation?.let { calc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Loan Results:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monthly Payment:",
                                fontSize = 16.sp,
                                color = Color(0xFF424242)
                            )
                            Text(
                                text = formatCurrencyWithDecimals(calc.monthlyPayment),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Interest Paid:",
                                fontSize = 16.sp,
                                color = Color(0xFF424242)
                            )
                            Text(
                                text = formatCurrency(calc.totalInterest),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE53935)
                            )
                        }

                        // Info Box
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF8E1)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "If you take a full loan, you pay ${formatCurrencyWithDecimals(calc.monthlyPayment)}/month and lose ${formatCurrency(calc.totalInterest)} in interest over ${formatYears(calc.loanPeriodYears)}.",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                color = Color(0xFF424242),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RentComparisonSection(state: CalculatorState, viewModel: CalculatorViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFF424242),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Text(
                    text = "Rent Comparison",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            // Info banner when loan not calculated
            if (state.loanCalculation == null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF8E1)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Please complete Step 1 first to calculate the monthly loan payment.",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 15.sp,
                        color = Color(0xFFEF6C00),
                        lineHeight = 22.sp
                    )
                }
            } else {
                // Monthly loan payment display
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monthly loan\npayment:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF424242),
                            lineHeight = 22.sp
                        )
                        Text(
                            text = "${formatCurrencyWithDecimals(state.loanCalculation.monthlyPayment)}/month",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                // Rent Input
                OutlinedTextField(
                    value = state.rent,
                    onValueChange = { viewModel.updateRent(it) },
                    label = { Text("Monthly Rent (\$) - Optional") },
                    placeholder = { Text("100") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = state.rentError != null,
                    supportingText = if (state.rentError != null) {
                        { Text(state.rentError, color = MaterialTheme.colorScheme.error) }
                    } else {
                        {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    "Enter 0 for rent-free scenario, or leave empty to use full loan payment as savings",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        focusedLabelColor = Color(0xFF424242),
                        cursorColor = Color(0xFF424242)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Calculate Scenarios Button
                Button(
                    onClick = { viewModel.calculateScenarios() },
                    enabled = !state.isCalculatingStep2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF424242),
                        disabledContainerColor = Color(0xFF424242).copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    if (state.isCalculatingStep2) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Calculate Scenarios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Scenarios Results
                if (state.scenarios.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Comparison Results:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        state.scenarios.forEachIndexed { index, scenario ->
                            ScenarioCard(
                                scenario = scenario,
                                isExpanded = state.expandedScenarios.contains(index),
                                isBest = index == 0,
                                onToggle = { viewModel.toggleScenario(index) },
                                monthlyPayment = state.loanCalculation.monthlyPayment,
                                loanPeriodYears = state.loanCalculation.loanPeriodYears
                            )
                        }
                    }
                } else if (state.netSavings != null && state.netSavings <= 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⚠️ Your rent is equal to or higher than the loan payment. There are no savings to build a down payment. Consider buying immediately or finding cheaper rent.",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            color = Color(0xFFC62828),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioCard(
    scenario: ScenarioResult,
    isExpanded: Boolean,
    isBest: Boolean,
    onToggle: () -> Unit,
    monthlyPayment: Double,
    loanPeriodYears: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (isBest) Color(0xFFE8F5E9) else Color(0xFFFAFAFA)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isBest) 3.dp else 1.dp
        )
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = scenario.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "Total cost",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = formatCurrency(scenario.totalLoss),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBest) Color(0xFF2E7D32) else Color(0xFF212121)
                    )

                    if (isBest) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "BEST",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // Expanded Details
            if (isExpanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (scenario.monthsRenting > 0) {
                        InfoCard(
                            icon = "📅",
                            backgroundColor = Color(0xFFE3F2FD),
                            text = "Time renting: ${formatMonthsWithYears(scenario.monthsRenting)}"
                        )
                    }

                    if (scenario.rentPaid > 0) {
                        InfoCard(
                            icon = "🏠",
                            backgroundColor = Color(0xFFFFF3E0),
                            text = "Rent paid: ${formatCurrency(scenario.rentPaid)}"
                        )
                    }

                    if (scenario.downPayment > 0) {
                        InfoCard(
                            icon = "💰",
                            backgroundColor = Color(0xFFE8F5E9),
                            text = "Down payment: ${formatCurrency(scenario.downPayment)}"
                        )
                    }

                    if (scenario.loanAmount > 0) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            InfoCard(
                                icon = "💳",
                                backgroundColor = Color(0xFFF3E5F5),
                                text = "Loan amount: ${formatCurrency(scenario.loanAmount)}"
                            )

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "💵 Monthly payment to bank: ${formatCurrencyWithDecimals(monthlyPayment)}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF424242)
                                    )
                                    Text(
                                        text = "After buying, you pay the full monthly payment (no rent)",
                                        fontSize = 13.sp,
                                        color = Color.Gray,
                                        lineHeight = 18.sp
                                    )
                                    if (scenario.monthsToPayOff > 0 && scenario.monthsToPayOff < loanPeriodYears * 12) {
                                        Text(
                                            text = "✓ Loan pays off in ${formatMonthsWithYears(scenario.monthsToPayOff)}",
                                            fontSize = 13.sp,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (scenario.interestPaid > 0) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📈", fontSize = 18.sp)
                                Text(
                                    text = "Interest paid: ${formatCurrency(scenario.interestPaid)}",
                                    fontSize = 14.sp,
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(icon: String, backgroundColor: Color, text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 18.sp)
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )
        }
    }
}

// Formatting helpers
fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }.format(amount)
}

fun formatCurrencyWithDecimals(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }.format(amount)
}

fun formatYears(years: Double): String {
    return if (years % 1 == 0.0) {
        "${years.toInt()} years"
    } else {
        "$years years"
    }
}

fun formatMonthsWithYears(months: Double): String {
    if (months <= 0) return "0 months"

    val totalMonths = months.toInt()
    val years = totalMonths / 12
    val remainingMonths = totalMonths % 12

    return when {
        years == 0 -> "$totalMonths months"
        remainingMonths == 0 -> "$totalMonths months (${years} ${if (years == 1) "year" else "years"})"
        else -> "$totalMonths months ($years ${if (years == 1) "year" else "years"} $remainingMonths ${if (remainingMonths == 1) "month" else "months"})"
    }
}