package by.vlfl.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.lab2.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.File


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var isParenthesisOpen: Boolean = false

    private val additionalEngineerOperations = AdditionalEngineerOperations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textViewInputNumbers = binding.textViewInputNumbers
        val textViewResult = binding.textViewResult

        if (savedInstanceState != null) {
            textViewInputNumbers.append(savedInstanceState.getCharSequence(KEY_EXPRESSION))
            textViewResult.append(savedInstanceState.getCharSequence(KEY_RESULT))
        }

        binding.apply {

            buttonOne.setOnClickListener {
                textViewInputNumbers.append(VALUE_ONE)
            }

            buttonTwo.setOnClickListener {
                textViewInputNumbers.append(VALUE_TWO)
            }

            buttonThree.setOnClickListener {
                textViewInputNumbers.append(VALUE_THREE)
            }

           buttonFour.setOnClickListener {
                textViewInputNumbers.append(VALUE_FOUR)
            }

            buttonFive.setOnClickListener {
                textViewInputNumbers.append(VALUE_FIVE)
            }

            buttonSix.setOnClickListener {
                textViewInputNumbers.append(VALUE_SIX)
            }

            buttonSeven.setOnClickListener {
                textViewInputNumbers.append(VALUE_SEVEN)
            }

            buttonEight.setOnClickListener {
                textViewInputNumbers.append(VALUE_EIGHT)
            }

            buttonNine.setOnClickListener {
                textViewInputNumbers.append(VALUE_NINE)
            }

            buttonZero.setOnClickListener {
                textViewInputNumbers.append(VALUE_ZERO)
            }

            buttonParenthesis.setOnClickListener {
                isParenthesisOpen = if (isParenthesisOpen) {
                    textViewInputNumbers.append(PARENTHESIS_CLOSE)
                    false
                } else {
                    textViewInputNumbers.append(PARENTHESIS_OPEN)
                    true
                }
            }

            buttonPlus.setOnClickListener {
                textViewInputNumbers.append(VALUE_PLUS)
            }

            buttonMinus.setOnClickListener {
                textViewInputNumbers.append(VALUE_MINUS)
            }

            buttonDivision.setOnClickListener {
                textViewInputNumbers.append(VALUE_DIVISION)
            }

            buttonMultiplication.setOnClickListener {
                textViewInputNumbers.append(VALUE_MULTIPLICATION)
            }

            buttonPercent.setOnClickListener {
                textViewInputNumbers.append(VALUE_PERCENT)
            }

            buttonRedo.setOnClickListener {
                textViewInputNumbers.text = textViewInputNumbers.text?.dropLast(1)
            }

            buttonDot.setOnClickListener {
                if (!textViewInputNumbers.text.takeLastWhile { !operations.contains(it) }
                                .contains(VALUE_DOT)) {
                    textViewInputNumbers.append(VALUE_DOT)
                }
            }

            buttonFactorial?.setOnClickListener {
                if (!textViewInputNumbers.text.takeLastWhile { !operations.contains(it) }
                                .contains(VALUE_FACTORIAL)) {
                    textViewInputNumbers.append(VALUE_FACTORIAL)
                }
            }

            buttonPi?.setOnClickListener {
                textViewInputNumbers.append(VALUE_PI)
            }

            buttonExponent?.setOnClickListener {
                textViewInputNumbers.append(VALUE_EXP)
            }

            buttonSin?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_SIN)
            }

            buttonCos?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_COS)
            }

            buttonTan?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_TAN)
            }

            buttonAbsolute?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_ABSOLUTE)
            }

            buttonSquareRoot?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_SQUARE_ROOT)
            }

            buttonExponentPower?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_EXP_POWER)
            }

            buttonLn?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_LN)
            }

            buttonLog?.setOnClickListener {
                appendValueWithOpenedParenthesis(VALUE_LOG)
            }

            buttonPower?.setOnClickListener {
                textViewInputNumbers.append(VALUE_POWER)
            }

            buttonSecondPower?.setOnClickListener {
                textViewInputNumbers.append(VALUE_POWER + VALUE_TWO)
            }

            buttonPowerOfTwo?.setOnClickListener {
                textViewInputNumbers.append(VALUE_TWO + VALUE_POWER)
            }

            buttonMinusPower?.setOnClickListener {
                textViewInputNumbers.append(VALUE_ONE + VALUE_DIVISION)
            }

            buttonEqual.setOnClickListener {
                if (isParenthesisOpen) {
                    textViewInputNumbers.append(PARENTHESIS_CLOSE)
                }

                try {
                    val expression = ExpressionBuilder(
                            textViewInputNumbers.text.toString()
                                    .replace(VALUE_MULTIPLICATION, EXPRESSION_VALID_MULTIPLICATION)
                                    .replace(VALUE_DIVISION, EXPRESSION_VALID_DIVISION)
                                    .replace(VALUE_LOG, EXPRESSION_VALID_LOG)
                                    .replace(VALUE_LN, EXPRESSION_VALID_LN)
                    )
                            .operator(additionalEngineerOperations.factorial)

                            .build()
                    val result = expression.evaluate()
                    val resultWithoutFractionalPart = result.toLong()
                    if (result.compareTo(resultWithoutFractionalPart) == 0) {
                        textViewResult.text = resultWithoutFractionalPart.toString()
                        writeToFile(resultWithoutFractionalPart.toString())
                    } else {
                        textViewResult.text = result.toString()
                        writeToFile(result.toString())
                    }

                } catch (argumentException: IllegalArgumentException) {
                    textViewResult.text = INVALID_FORMAT
                } catch (divisionByZeroException: ArithmeticException) {
                    textViewResult.text = DIVISION_BY_ZERO
                }

            }

            buttonClear.setOnClickListener {
                textViewInputNumbers.text = null
                textViewResult.text = null
            }

            buttonHistory.setOnClickListener {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run {
            putCharSequence(KEY_EXPRESSION, binding.textViewInputNumbers.text)
            putCharSequence(KEY_RESULT, binding.textViewResult.text)
        }
    }

    private fun appendValueWithOpenedParenthesis(value: String) {
        binding.textViewInputNumbers.apply {
            append(value)
            append(PARENTHESIS_OPEN)
            isParenthesisOpen = true
        }
    }

    private fun writeToFile(newData: String) {

        val path = filesDir
        if (!path.exists()) {
            path.mkdirs()
        }

        openFileOutput(FILE_NAME, Context.MODE_APPEND).use {
            it?.write(("$newData,").toByteArray())
        }
    }

    companion object {
        private const val VALUE_ONE = "1"
        private const val VALUE_TWO = "2"
        private const val VALUE_THREE = "3"
        private const val VALUE_FOUR = "4"
        private const val VALUE_FIVE = "5"
        private const val VALUE_SIX = "6"
        private const val VALUE_SEVEN = "7"
        private const val VALUE_EIGHT = "8"
        private const val VALUE_NINE = "9"
        private const val VALUE_ZERO = "0"
        private const val VALUE_PLUS = "+"
        private const val VALUE_MINUS = "-"
        private const val VALUE_DIVISION = "÷"
        private const val VALUE_MULTIPLICATION = "×"
        private const val VALUE_FACTORIAL = "!"
        private const val EXPRESSION_VALID_MULTIPLICATION = "*"
        private const val EXPRESSION_VALID_DIVISION = "/"
        private const val VALUE_PERCENT = "%"
        private const val PARENTHESIS_OPEN = "("
        private const val PARENTHESIS_CLOSE = ")"
        private const val VALUE_DOT = "."
        private const val VALUE_SIN = "sin"
        private const val VALUE_COS = "cos"
        private const val VALUE_TAN = "tan"
        private const val VALUE_LN = "ln"
        private const val VALUE_LOG = "log"
        private const val VALUE_POWER = "^"
        private const val EXPRESSION_VALID_LN = "log"
        private const val EXPRESSION_VALID_LOG = "log10"
        private const val VALUE_SQUARE_ROOT = "sqrt"
        private const val VALUE_ABSOLUTE = "abs"
        private const val VALUE_PI = "3.14"
        private const val VALUE_EXP = "2.72"
        private const val VALUE_EXP_POWER = "exp"
        private const val INVALID_FORMAT = "Invalid format"
        private const val DIVISION_BY_ZERO = "Division by zero"
        private const val KEY_EXPRESSION = "Expression key"
        private const val KEY_RESULT = "Result key"
        private const val FILE_NAME = "numbers.txt"
        private const val operations = "$VALUE_PLUS$VALUE_MINUS$VALUE_DIVISION$VALUE_MULTIPLICATION" +
                "$PARENTHESIS_OPEN$PARENTHESIS_CLOSE$VALUE_PERCENT"
    }
}
