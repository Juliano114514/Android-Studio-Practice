package com.example.androidstudioexam.chapter3.model

import java.math.BigDecimal

const val KEYBOARD_ROW = 4
const val KEYBOARD_COLUMN = 6

/**
 * 计算结果密封类
 */
sealed class CalculatorResult {
  data class Success(val value: BigDecimal) : CalculatorResult()
  data class Error(val errorMessage: String) : CalculatorResult()
}

/**
 * 计算器显示状态枚举
 */
sealed class CalculatorState {
  data class Display(val value: String) : CalculatorState()
  data class Error(val message: String) : CalculatorState()
  object Clear : CalculatorState()  // 单例清除，可以全局只用一个（rbq这一块）
}


/**
 * 按钮类型枚举
 * 用于区分不同类型的按钮，便于统一处理和样式设置
 */
enum class ButtonType {
  NUMBER,
  OPERATOR,
  FUNCTION,
  CLEAR,
  EQUALS
}

/**
 * 按钮配置数据类
 * @param text 按钮显示的文本
 * @param type 按钮类型，用于样式设置和事件处理
 * @param gridColumn 按钮在网格中的列位置（0-3）
 * @param gridRow 按钮在网格中的行位置（0-5）
 */
data class ButtonConfig(
  val text: String,
  val type: ButtonType,
  val gridColumn: Int,
  val gridRow: Int
) {
  /**
   * 验证配置的有效性
   * 确保列和行位置在有效范围内
   */
  fun isValid(): Boolean {
    return gridColumn in 0 until KEYBOARD_COLUMN && gridRow in 0 until KEYBOARD_ROW
  }
}