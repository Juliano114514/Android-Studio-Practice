package com.example.androidstudioexam.chapter3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidstudioexam.chapter3.model.ButtonType
import com.example.androidstudioexam.chapter3.model.CalculatorEngine
import com.example.androidstudioexam.chapter3.model.CalculatorResult
import com.example.androidstudioexam.chapter3.model.CalculatorState
import com.example.androidstudioexam.R
import com.example.androidstudioexam.chapter3.model.ErrorType
import com.example.androidstudioexam.chapter3.model.ErrorType.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CalculatorViewModel : ViewModel(){

  private val calculatorEngine = CalculatorEngine()         // 计算引擎实例
  private var currentValue: BigDecimal = BigDecimal.ZERO    // 当前显示值
  private var previousValue: BigDecimal? = null             // 上一个值（用于二元运算）
  private var currentOperator: String? = null               // 当前运算符
  private var isNewInput = true                             // 是否正在输入新数字

  // 使用 MutableStateFlow 存储可变的计算器状态
  // TODO: 为什么？
  private val _calculatorState = MutableStateFlow<CalculatorState>(
    CalculatorState.Display("0")
  )

  // 对外暴露只读的 StateFlow，确保状态只能通过 ViewModel 修改
  // TODO: 为什么？
  val calculatorState: StateFlow<CalculatorState> = _calculatorState.asStateFlow()

  /**
   * 处理按钮点击事件
   * 根据按钮类型分发到相应的处理方法
   *
   * @param buttonText 按钮文本
   * @param buttonType 按钮类型
   */
  fun onButtonClick(buttonText: String, buttonType: ButtonType) {
    viewModelScope.launch {
      when (buttonType) {
        ButtonType.NUMBER -> handleNumberInput(buttonText)
        ButtonType.OPERATOR -> handleOperatorInput(buttonText)
        ButtonType.FUNCTION -> handleFunctionInput(buttonText)
        ButtonType.CLEAR -> handleClearInput(buttonText)
        ButtonType.EQUALS -> handleEqualsInput()
      }
    }
  }

  /**
   * 处理数字输入
   * @param digit 输入的数字字符
   */
  private fun handleNumberInput(digit: String) {
    if (isNewInput) {
      currentValue = BigDecimal(digit)
      isNewInput = false
    } else {
      // 追加数字，避免精度问题
      val newValue = currentValue.toPlainString() + digit
      currentValue = try {
        BigDecimal(newValue)
      } catch (e: NumberFormatException) {
        e.printStackTrace()
        _calculatorState.value = CalculatorState.Error(SyntaxError)
        return
      }
    }
    updateDisplay()
  }


  /**
   * 处理运算符输入
   * @param operator 运算符（+、-、*、/）
   */
  private fun handleOperatorInput(operator: String) {
    // 如果已有待执行的运算，先执行它
    if (previousValue != null && currentOperator != null && !isNewInput) {
      executeOperation()
    }

    previousValue = currentValue
    currentOperator = operator
    isNewInput = true
  }


  /**
   * 处理清除输入
   * @param clearType 清除类型（"C" 或 "AC"）
   */
  private fun handleClearInput(clearType: String) {
    when (clearType) {
      "C" -> {
        // 清除当前输入
        currentValue = BigDecimal.ZERO
        isNewInput = true
      }
      "AC" -> {
        // 全部清除
        currentValue = BigDecimal.ZERO
        previousValue = null
        currentOperator = null
        isNewInput = true
      }
    }
    updateDisplay()
  }


  /**
   * 处理等号输入
   * 执行待执行的运算
   */
  private fun handleEqualsInput() {
    if (previousValue != null && currentOperator != null) {
      executeOperation()
      previousValue = null
      currentOperator = null
      isNewInput = true
    }
  }

  /**
   * 执行二元运算
   * 根据当前运算符调用相应的计算方法
   */
  private fun executeOperation() {
    val prev = previousValue ?: return
    val op = currentOperator ?: return

    val result = when (op) {
      "+" -> calculatorEngine.add(prev, currentValue)
      "-" -> calculatorEngine.subtract(prev, currentValue)
      "*" -> calculatorEngine.multiply(prev, currentValue)
      "/" -> calculatorEngine.divide(prev, currentValue)
      else -> CalculatorResult.Error(SyntaxError)
    }

    handleCalculationResult(result)
  }

  /**
   * 处理功能按钮输入（开方、乘方）
   * @param function 功能标识（"√" 或 "x²"）
   */
  private fun handleFunctionInput(function: String) {
    val result = when (function) {
      "√" -> calculatorEngine.squareRoot(currentValue)
      "x²" -> calculatorEngine.power(currentValue)
      else -> CalculatorResult.Error(SyntaxError)
    }

    handleCalculationResult(result)
    isNewInput = true
  }


  /**
   * 处理计算结果
   * 根据结果类型更新状态
   * @param result 计算结果
   */
  private fun handleCalculationResult(result: CalculatorResult) {
    when (result) {
      is CalculatorResult.Success -> {
        currentValue = result.value
        updateDisplay()
      }
      is CalculatorResult.Error -> {
        _calculatorState.value = CalculatorState.Error(result.error)
      }
    }
  }

  /**
   * 更新显示
   * 格式化当前值并更新状态
   */
  private fun updateDisplay() {
    val formattedValue = calculatorEngine.formatNumber(currentValue)
    _calculatorState.value = CalculatorState.Display(formattedValue)
  }

  /**
   * 根据错误类型获取对应的字符串资源 ID
   * @param error 错误类型
   * @return 字符串资源 ID
   */
  fun getErrorOutput(error: ErrorType): Int {
    return when(error) {
      is DividedByZero -> R.string.error_divide_by_zero
      is NegativeSqrt -> R.string.error_negative_square_root
      is AddOverFlow -> R.string.error_add_overflow
      is SubtractOverFlow -> R.string.error_subtract_overflow
      is MultiplyOverFlow -> R.string.error_multiply_overflow
      is PowerOverFlow -> R.string.error_overflow
      is SyntaxError -> R.string.error_invalid_operation
    }
  }
}