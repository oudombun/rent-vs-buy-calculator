package android.template.ui.calculator

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    onOpenAbout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rent vs Loan Calculator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    TextButton(onClick = onOpenAbout) {
                        Text(
                            "About",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2F2F2F),
                    titleContentColor = Color.White
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step 1: Loan Calculator
            Step1Card(
                state = state,
                onHousePriceChange = viewModel::updateHousePrice,
                onInterestRateChange = viewModel::updateInterestRate,
                onLoanPeriodChange = viewModel::updateLoanPeriod,
                onCustomPeriodChange = viewModel::updateCustomPeriod,
                onToggleCustomPeriod = viewModel::toggleCustomPeriod,
                onCalculateLoan = viewModel::calculateLoan
            )

            // Step 2: Rent Comparison (only show if loan is calculated)
            if (state.loanCalculation != null) {
                Step2Card(
                    state = state,
                    onRentChange = viewModel::updateRent,
                    onCalculateScenarios = viewModel::calculateScenarios,
                    onToggleScenario = viewModel::toggleScenario
                )
            }
        }
    }
}

@Composable
private fun Step1Card(
    state: CalculatorState,
    onHousePriceChange: (String) -> Unit,
    onInterestRateChange: (String) -> Unit,
    onLoanPeriodChange: (Int) -> Unit,
    onCustomPeriodChange: (String) -> Unit,
    onToggleCustomPeriod: (Boolean) -> Unit,
    onCalculateLoan: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with number circle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(
                            color =  Color(0xFF2F2F2F),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Loan Calculator",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F2F2F)
                )
            }
            val colors = MaterialTheme.colorScheme
            Log.d("Theme", "primary: ${colors.primary}")
            Log.d("Theme", "onSurface: ${colors.onSurface}")
            Log.d("Theme", "surface: ${colors.surface}")
            // House Price Input
            OutlinedTextField(
                value = state.housePrice,
                onValueChange = onHousePriceChange,
                label = { Text("House Price (\$)") },
                isError = state.housePriceError != null,
                supportingText = state.housePriceError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Interest Rate Input
            OutlinedTextField(
                value = state.interestRate,
                onValueChange = onInterestRateChange,
                label = { Text("Annual Interest Rate (%)") },
                isError = state.interestRateError != null,
                supportingText = state.interestRateError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2F2F2F),
                    unfocusedBorderColor = Color(0xFFBDBDBD)
                )
            )

            // Loan Period
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Loan Period",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2F2F2F)
                )

                // Checkbox for custom period
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = state.isCustomPeriod,
                        onCheckedChange = onToggleCustomPeriod,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF2F2F2F)
                        )
                    )
                    Text("Use custom period")
                }

                if (!state.isCustomPeriod) {
                    // Period chips in a single row with borders - matching Flutter exactly
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        listOf(10, 15, 20).forEachIndexed { index, years ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        if (state.loanPeriodYears == years) Color(0xFFE3F2FD) else Color.Transparent
                                    )
                                    .clickable { onLoanPeriodChange(years) },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (state.loanPeriodYears == years) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF2196F3),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = "$years years",
                                        color = if (state.loanPeriodYears == years) Color(0xFF2196F3) else Color(0xFF757575),
                                        fontSize = 14.sp,
                                        fontWeight = if (state.loanPeriodYears == years) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }

                            // Add divider between items (not after last item)
                            if (index < 2) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(1.dp),
                                    color = Color(0xFFBDBDBD)
                                )
                            }
                        }
                    }
                }

                // Custom Period Input
                AnimatedVisibility(visible = state.isCustomPeriod) {
                    OutlinedTextField(
                        value = state.customPeriod,
                        onValueChange = onCustomPeriodChange,
                        label = { Text("Custom Period (years)") },
                        placeholder = { Text("Enter loan period in years") },
                        isError = state.customPeriodError != null,
                        supportingText = state.customPeriodError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2F2F2F),
                            unfocusedBorderColor = Color(0xFFBDBDBD)
                        )
                    )
                }
            }

            // Calculate Button
            Button(
                onClick = onCalculateLoan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F2F2F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = !state.isCalculatingStep1
            ) {
                if (state.isCalculatingStep1) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        "Calculate Loan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Loan Results
            state.loanCalculation?.let { loan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Loan Results:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F2F2F)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Monthly Payment:",
                                fontSize = 16.sp,
                                color = Color(0xFF2F2F2F)
                            )
                            Text(
                                formatCurrencyWithDecimals(loan.monthlyPayment),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2F2F2F)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total Interest Paid:",
                                fontSize = 14.sp,
                                color = Color(0xFF2F2F2F)
                            )
                            Text(
                                formatCurrency(loan.totalInterest),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE74C3C)
                            )
                        }

                        // Info box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "If you take a full loan, you pay ${formatCurrencyWithDecimals(loan.monthlyPayment)}/month and lose ${formatCurrency(loan.totalInterest)} in interest over ${loan.loanPeriodYears.toInt()} years.",
                                fontSize = 14.sp,
                                color = Color(0xFF2F2F2F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Step2Card(
    state: CalculatorState,
    onRentChange: (String) -> Unit,
    onCalculateScenarios: () -> Unit,
    onToggleScenario: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with number circle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(Color(0xFF2F2F2F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Rent Comparison",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F2F2F)
                )
            }

            // Show instruction or monthly loan payment
            if (state.scenarios.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Please complete Step 1 first to calculate the monthly loan payment.",
                        fontSize = 14.sp,
                        color = Color(0xFFE65100)
                    )
                }
            } else {
                // Monthly loan payment display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monthly loan\n payment:",
                            fontSize = 12.sp,
                            color = Color(0xFF2F2F2F),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = state.loanCalculation?.let { formatCurrencyWithDecimals(it.monthlyPayment) + "/month" } ?: "",
                            fontSize = 15.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Rent Input
            OutlinedTextField(
                value = state.rent,
                onValueChange = onRentChange,
                label = { Text("Monthly Rent (\$) - Optional") },
                placeholder = { Text("Enter rent amount (0 = rent-free)") },
                isError = state.rentError != null,
                supportingText = {
                    Text(state.rentError ?: "Enter 0 for rent-free scenario, or leave empty to use full loan payment as savings")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2F2F2F),
                    unfocusedBorderColor = Color(0xFFBDBDBD)
                )
            )

            // Calculate Button
            Button(
                onClick = onCalculateScenarios,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F2F2F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = !state.isCalculatingStep2
            ) {
                if (state.isCalculatingStep2) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        "Calculate Scenarios",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Net Savings Display
            state.netSavings?.let { savings ->
                if (savings > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Net Savings:",
                                fontSize = 15.sp,
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${formatCurrencyWithDecimals(savings)}/month",
                                fontSize = 17.sp,
                                color = Color(0xFF2F2F2F),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Scenarios List
            if (state.scenarios.isNotEmpty()) {
                Text(
                    "Comparison of All Scenarios:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F2F2F)
                )

                state.scenarios.forEachIndexed { index, scenario ->
                    ScenarioCard(
                        scenario = scenario,
                        index = index,
                        isExpanded = state.expandedScenarios.contains(index),
                        isBestOption = index == 0,
                        onToggle = { onToggleScenario(index) },
                        monthlyPayment = state.loanCalculation?.monthlyPayment ?: 0.0,
                        loanPeriodYears = state.loanCalculation?.loanPeriodYears ?: 15.0
                    )
                }
            }
        }
    }
}

@Composable
private fun ScenarioCard(
    scenario: ScenarioResult,
    index: Int,
    isExpanded: Boolean,
    isBestOption: Boolean,
    onToggle: () -> Unit,
    monthlyPayment: Double,
    loanPeriodYears: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable(onClick = onToggle),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = if (isBestOption) 2.dp else 1.dp,
            color = if (isBestOption) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = scenario.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isBestOption) Color(0xFF2E7D32) else Color(0xFF2F2F2F)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isBestOption) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "BEST",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFF757575)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total cost row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total cost",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    formatCurrency(scenario.totalLoss),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFFE74C3C)
                )
            }

            // Expanded Details
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider(color = Color(0xFFE0E0E0))

                    Spacer(modifier = Modifier.height(8.dp))

                    if (scenario.monthsRenting > 0) {
                        DetailItem(
                            title = "Time renting: ${formatMonthsWithYears(scenario.monthsRenting)}",
                            backgroundColor = Color(0xFFE3F2FD),
                            textColor = Color(0xFF1976D2)
                        )

                        if (scenario.rentPaid > 0) {
                            DetailItem(
                                title = "Rent paid: ${formatCurrency(scenario.rentPaid)}",
                                backgroundColor = Color(0xFFFFF3E0),
                                textColor = Color(0xFFE65100)
                            )
                        }

                        DetailItem(
                            title = "Down payment: ${formatCurrency(scenario.downPayment)}",
                            backgroundColor = Color(0xFFE8F5E9),
                            textColor = Color(0xFF2E7D32)
                        )

                        DetailItem(
                            title = "Loan amount: ${formatCurrency(scenario.loanAmount)}",
                            backgroundColor = Color(0xFFF3E5F5),
                            textColor = Color(0xFF7B1FA2)
                        )
                    }

                    // Monthly payment box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "💳 Monthly payment to bank: ${formatCurrencyWithDecimals(monthlyPayment)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2F2F2F)
                            )
                            Text(
                                "After buying, you pay the full monthly payment (no rent)",
                                fontSize = 12.sp,
                                color = Color(0xFF757575)
                            )

                            if (scenario.monthsToPayOff > 0 && scenario.monthsToPayOff < loanPeriodYears * 12) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        "✓",
                                        fontSize = 14.sp,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Loan pays off in ${formatMonthsWithYears(scenario.monthsToPayOff)}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    if (scenario.interestPaid > 0) {
                        DetailItem(
                            title = "Interest paid: ${formatCurrency(scenario.interestPaid)}",
                            backgroundColor = Color(0xFFFFEBEE),
                            textColor = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

// Formatting helpers
private fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(amount)
}

private fun formatCurrencyWithDecimals(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(amount)
}

private fun formatMonthsWithYears(months: Double): String {
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