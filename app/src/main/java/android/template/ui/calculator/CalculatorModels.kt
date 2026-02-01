package android.template.ui.calculator

data class LoanCalculation(
    val monthlyPayment: Double,
    val totalInterest: Double,
    val loanPeriodYears: Double
)

data class ScenarioResult(
    val name: String,
    val percentSaved: Int,
    val monthsRenting: Double,
    val rentPaid: Double,
    val downPayment: Double,
    val loanAmount: Double,
    val interestPaid: Double,
    val totalLoss: Double,
    val monthsToPayOff: Double
)

data class CalculatorState(
    val housePrice: String = "",
    val interestRate: String = "",
    val loanPeriodYears: Int = 15,
    val isCustomPeriod: Boolean = false,
    val customPeriod: String = "",
    val rent: String = "",
    
    val loanCalculation: LoanCalculation? = null,
    val netSavings: Double? = null,
    val scenarios: List<ScenarioResult> = emptyList(),
    
    val isCalculatingStep1: Boolean = false,
    val isCalculatingStep2: Boolean = false,
    val expandedScenarios: Set<Int> = emptySet(),
    
    val housePriceError: String? = null,
    val interestRateError: String? = null,
    val customPeriodError: String? = null,
    val rentError: String? = null
)
