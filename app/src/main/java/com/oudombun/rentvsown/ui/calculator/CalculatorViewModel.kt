package com.oudombun.rentvsown.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun updateHousePrice(value: String) {
        _state.update { it.copy(housePrice = value, housePriceError = null) }
    }

    fun updateInterestRate(value: String) {
        _state.update { it.copy(interestRate = value, interestRateError = null) }
    }

    fun updateLoanPeriod(years: Int) {
        _state.update { it.copy(loanPeriodYears = years) }
    }

    fun updateCustomPeriod(value: String) {
        _state.update { it.copy(customPeriod = value, customPeriodError = null) }
    }

    fun toggleCustomPeriod(enabled: Boolean) {
        _state.update { it.copy(isCustomPeriod = enabled, customPeriod = "") }
    }

    fun updateRent(value: String) {
        _state.update { it.copy(rent = value, rentError = null) }
    }

    fun toggleScenario(index: Int) {
        _state.update {
            val newSet = it.expandedScenarios.toMutableSet()
            if (newSet.contains(index)) {
                newSet.remove(index)
            } else {
                newSet.add(index)
            }
            it.copy(expandedScenarios = newSet)
        }
    }

    fun calculateLoan() {
        val currentState = _state.value

        // Validate inputs
        val housePrice = validateHousePrice(currentState.housePrice)
        val interestRate = validateInterestRate(currentState.interestRate)
        val customPeriod = if (currentState.isCustomPeriod) {
            validateCustomPeriod(currentState.customPeriod)
        } else null

        if (housePrice == null || interestRate == null ||
            (currentState.isCustomPeriod && customPeriod == null)) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isCalculatingStep1 = true) }
            delay(300) // Simulate calculation

            val effectivePeriod = if (currentState.isCustomPeriod) customPeriod!! else currentState.loanPeriodYears.toDouble()
            val monthlyInterestRate = (interestRate / 100.0) / 12.0
            val numberOfMonths = (effectivePeriod * 12).toInt()

            val monthlyPayment = calculateMonthlyPayment(housePrice, monthlyInterestRate, numberOfMonths)
            val totalInterest = (monthlyPayment * numberOfMonths) - housePrice

            _state.update {
                it.copy(
                    loanCalculation = LoanCalculation(monthlyPayment, totalInterest, effectivePeriod),
                    isCalculatingStep1 = false
                )
            }
        }
    }

    fun calculateScenarios() {
        val currentState = _state.value
        val loanCalc = currentState.loanCalculation ?: return

        val rent = validateRent(currentState.rent)
        if (rent == null && currentState.rent.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isCalculatingStep2 = true, expandedScenarios = emptySet()) }
            delay(400)

            val actualRent = rent ?: 0.0
            val netSavings = loanCalc.monthlyPayment - actualRent

            if (netSavings <= 0) {
                _state.update {
                    it.copy(
                        netSavings = netSavings,
                        scenarios = emptyList(),
                        isCalculatingStep2 = false
                    )
                }
                return@launch
            }

            val housePrice = currentState.housePrice.toDouble()
            val annualInterest = currentState.interestRate.toDouble()
            val monthlyInterestRate = (annualInterest / 100.0) / 12.0

            val scenarios = calculateAllScenarios(
                housePrice = housePrice,
                monthlyPayment = loanCalc.monthlyPayment,
                monthlyInterestRate = monthlyInterestRate,
                netSavings = netSavings,
                actualRent = actualRent,
                loanPeriodYears = loanCalc.loanPeriodYears
            )

            _state.update {
                it.copy(
                    netSavings = netSavings,
                    scenarios = scenarios,
                    isCalculatingStep2 = false,
                    expandedScenarios = if (scenarios.isNotEmpty()) setOf(0) else emptySet()
                )
            }
        }
    }

    private fun validateHousePrice(value: String): Double? {
        if (value.isEmpty()) {
            _state.update { it.copy(housePriceError = "Please enter house price") }
            return null
        }
        val price = value.toDoubleOrNull()
        if (price == null) {
            _state.update { it.copy(housePriceError = "Please enter a valid number") }
            return null
        }
        if (price <= 0) {
            _state.update { it.copy(housePriceError = "House price must be greater than 0") }
            return null
        }
        if (price > 1_000_000_000) {
            _state.update { it.copy(housePriceError = "House price is too large") }
            return null
        }
        return price
    }

    private fun validateInterestRate(value: String): Double? {
        if (value.isEmpty()) {
            _state.update { it.copy(interestRateError = "Please enter interest rate") }
            return null
        }
        val rate = value.toDoubleOrNull()
        if (rate == null) {
            _state.update { it.copy(interestRateError = "Please enter a valid number") }
            return null
        }
        if (rate <= 0) {
            _state.update { it.copy(interestRateError = "Interest rate must be greater than 0") }
            return null
        }
        if (rate > 100) {
            _state.update { it.copy(interestRateError = "Interest rate seems too high") }
            return null
        }
        return rate
    }

    private fun validateCustomPeriod(value: String): Double? {
        if (value.isEmpty()) {
            _state.update { it.copy(customPeriodError = "Please enter custom period") }
            return null
        }
        val period = value.toDoubleOrNull()
        if (period == null || period <= 0) {
            _state.update { it.copy(customPeriodError = "Please enter a valid number") }
            return null
        }
        if (period > 50) {
            _state.update { it.copy(customPeriodError = "Period seems too long") }
            return null
        }
        return period
    }

    private fun validateRent(value: String): Double? {
        if (value.isEmpty()) return null
        val rent = value.toDoubleOrNull()
        if (rent == null) {
            _state.update { it.copy(rentError = "Please enter a valid number") }
            return null
        }
        if (rent < 0) {
            _state.update { it.copy(rentError = "Rent cannot be negative") }
            return null
        }
        if (rent > 100_000) {
            _state.update { it.copy(rentError = "Rent amount seems too high") }
            return null
        }
        return rent
    }

    private fun calculateMonthlyPayment(principal: Double, monthlyRate: Double, numberOfMonths: Int): Double {
        if (principal <= 0 || monthlyRate <= 0 || numberOfMonths <= 0) return 0.0
        if (monthlyRate == 0.0) return principal / numberOfMonths

        val numerator = monthlyRate * (1 + monthlyRate).pow(numberOfMonths)
        val denominator = (1 + monthlyRate).pow(numberOfMonths) - 1
        return principal * (numerator / denominator)
    }

    private fun calculateAllScenarios(
        housePrice: Double,
        monthlyPayment: Double,
        monthlyInterestRate: Double,
        netSavings: Double,
        actualRent: Double,
        loanPeriodYears: Double
    ): List<ScenarioResult> {
        val results = mutableListOf<ScenarioResult>()

        // Scenario 1: Full loan (0% saved)
        val fullLoanInterest = calculateLoanInterest(housePrice, monthlyInterestRate, monthlyPayment, loanPeriodYears)
        results.add(
            ScenarioResult(
                name = "Buy Now (No Savings)",
                percentSaved = 0,
                monthsRenting = 0.0,
                rentPaid = 0.0,
                downPayment = 0.0,
                loanAmount = housePrice,
                interestPaid = fullLoanInterest,
                totalLoss = fullLoanInterest,
                monthsToPayOff = loanPeriodYears * 12.0
            )
        )

        // Scenarios: 20%, 40%, 50%, 60%, 70%, 80%, 90% saved
        for (percent in listOf(20, 40, 50, 60, 70, 80, 90)) {
            val saveAmount = housePrice * (percent / 100.0)
            val monthsToSave = saveAmount / netSavings
            val rentPaid = actualRent * monthsToSave
            val loanAmount = housePrice - saveAmount

            val payoffResult = calculateLoanPayoffWithFixedPayment(loanAmount, monthlyInterestRate, monthlyPayment)

            results.add(
                ScenarioResult(
                    name = "Save $percent% First",
                    percentSaved = percent,
                    monthsRenting = monthsToSave,
                    rentPaid = rentPaid,
                    downPayment = saveAmount,
                    loanAmount = loanAmount,
                    interestPaid = payoffResult.first,
                    totalLoss = rentPaid + payoffResult.first,
                    monthsToPayOff = payoffResult.second
                )
            )
        }

        // Scenario: Full cash (100% saved)
        val monthsToSaveFull = housePrice / netSavings
        val rentPaidFull = actualRent * monthsToSaveFull
        results.add(
            ScenarioResult(
                name = "Save 100% First",
                percentSaved = 100,
                monthsRenting = monthsToSaveFull,
                rentPaid = rentPaidFull,
                downPayment = housePrice,
                loanAmount = 0.0,
                interestPaid = 0.0,
                totalLoss = rentPaidFull,
                monthsToPayOff = 0.0
            )
        )

        return results.sortedBy { it.totalLoss }
    }

    private fun calculateLoanInterest(
        loanAmount: Double,
        monthlyRate: Double,
        monthlyPayment: Double,
        loanPeriodYears: Double
    ): Double {
        if (loanAmount <= 0) return 0.0
        var balance = loanAmount
        var totalInterest = 0.0
        var months = 0
        val maxMonths = (loanPeriodYears * 12).toInt()

        while (balance > 0 && months < maxMonths) {
            val interest = balance * monthlyRate
            val principal = monthlyPayment - interest
            if (principal <= 0) break

            totalInterest += interest
            balance -= principal
            months++
            if (balance < 0) balance = 0.0
        }

        return totalInterest
    }

    private fun calculateLoanPayoffWithFixedPayment(
        loanAmount: Double,
        monthlyRate: Double,
        fixedMonthlyPayment: Double
    ): Pair<Double, Double> {
        if (loanAmount <= 0 || fixedMonthlyPayment <= 0) return Pair(0.0, 0.0)

        var balance = loanAmount
        var totalInterest = 0.0
        var months = 0
        val maxMonths = 600 // 50 years safety limit

        while (balance > 0 && months < maxMonths) {
            val interest = balance * monthlyRate
            val principal = fixedMonthlyPayment - interest
            if (principal <= 0) break

            totalInterest += interest
            balance -= principal
            months++
            if (balance < 0) balance = 0.0
        }

        return Pair(totalInterest, months.toDouble())
    }
}
