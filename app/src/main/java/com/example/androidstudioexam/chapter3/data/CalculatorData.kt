package com.example.androidstudioexam.chapter3.data

import com.example.androidstudioexam.chapter3.model.ButtonConfig
import com.example.androidstudioexam.chapter3.model.ButtonType

object CalculatorData {
  val buttonConfigs = listOf(
    // 第一行：清除按钮和功能按钮
    ButtonConfig("AC", ButtonType.CLEAR, 0, 0),
    ButtonConfig("C", ButtonType.CLEAR, 1, 0),
    ButtonConfig("√", ButtonType.FUNCTION, 2, 0),
    ButtonConfig("x²", ButtonType.FUNCTION, 3, 0),

    // 第二行：数字 7-9 和除号
    ButtonConfig("7", ButtonType.NUMBER, 0, 1),
    ButtonConfig("8", ButtonType.NUMBER, 1, 1),
    ButtonConfig("9", ButtonType.NUMBER, 2, 1),
    ButtonConfig("/", ButtonType.OPERATOR, 3, 1),

    // 第三行：数字 4-6 和乘号
    ButtonConfig("4", ButtonType.NUMBER, 0, 2),
    ButtonConfig("5", ButtonType.NUMBER, 1, 2),
    ButtonConfig("6", ButtonType.NUMBER, 2, 2),
    ButtonConfig("*", ButtonType.OPERATOR, 3, 2),

    // 第四行：数字 1-3 和减号
    ButtonConfig("1", ButtonType.NUMBER, 0, 3),
    ButtonConfig("2", ButtonType.NUMBER, 1, 3),
    ButtonConfig("3", ButtonType.NUMBER, 2, 3),
    ButtonConfig("-", ButtonType.OPERATOR, 3, 3),

    // 第五行：数字 0、小数点、等号和加号
    ButtonConfig("0", ButtonType.NUMBER, 0, 4),
    ButtonConfig(".", ButtonType.NUMBER, 1, 4),
    ButtonConfig("=", ButtonType.EQUALS, 2, 4),
    ButtonConfig("+", ButtonType.OPERATOR, 3, 4),

    // 第六行：可以添加更多功能按钮（可选）
  )



}