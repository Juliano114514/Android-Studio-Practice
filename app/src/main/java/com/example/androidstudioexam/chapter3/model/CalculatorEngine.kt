package com.example.androidstudioexam.chapter3.model

import com.example.androidstudioexam.chapter3.model.ErrorType.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

class CalculatorEngine {

  /**
   * 计算精度 - 小数点后保留的位数
   */
  companion object {
    private const val SCALE = 10
    private val ROUNDING_MODE = RoundingMode.HALF_UP  // 四舍五入
  }


  fun add(a: BigDecimal, b: BigDecimal): CalculatorResult {
    return try {
      CalculatorResult.Success(a.add(b))
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(AddOverFlow)
    }
  }

  fun subtract(a: BigDecimal, b: BigDecimal): CalculatorResult {
    return try {
      CalculatorResult.Success(a.subtract(b))
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(SubtractOverFlow)
    }
  }

  fun multiply(a: BigDecimal, b: BigDecimal): CalculatorResult {
    return try {
      CalculatorResult.Success(a.multiply(b))
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(MultiplyOverFlow)
    }
  }

  fun divide(a: BigDecimal, b: BigDecimal): CalculatorResult {
    return try {
      // 检查除零错误
      if (b.compareTo(BigDecimal.ZERO) == 0) {
        return CalculatorResult.Error(DividedByZero)
      }
      // 使用指定精度和舍入模式进行除法运算
      CalculatorResult.Success(
        a.divide(b, SCALE, ROUNDING_MODE)
      )
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(SyntaxError)
    }
  }

  fun squareRoot(value: BigDecimal): CalculatorResult {
    return try {
      // 检查负数开方
      if (value.compareTo(BigDecimal.ZERO) < 0) {
        return CalculatorResult.Error(NegativeSqrt)
      }
      // 使用 Kotlin 标准库的 sqrt 函数，然后转换为 BigDecimal
      val result = sqrt(value.toDouble())
      CalculatorResult.Success(
        BigDecimal(result).setScale(SCALE, ROUNDING_MODE)
      )
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(SyntaxError)
    }
  }

  fun power(value: BigDecimal): CalculatorResult {
    return try {
      // 使用 multiply 实现平方运算
      CalculatorResult.Success(value.multiply(value))
    } catch (e: ArithmeticException) {
      CalculatorResult.Error(PowerOverFlow)
    }
  }

  fun formatNumber(value: BigDecimal): String {
    // 移除末尾的零和小数点（如果小数点后全是零）
    var formatted = value.stripTrailingZeros().toPlainString()
    // 如果结果是整数，不显示小数点
    if (formatted.contains(".")) {
      formatted = formatted.trimEnd('0').trimEnd('.')
    }
    return formatted
  }
}