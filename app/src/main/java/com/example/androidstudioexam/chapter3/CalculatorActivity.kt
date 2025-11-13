package com.example.androidstudioexam.chapter3

import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidstudioexam.R
import com.example.androidstudioexam.chapter3.data.CalculatorData.buttonConfigs
import com.example.androidstudioexam.chapter3.model.ButtonConfig
import com.example.androidstudioexam.chapter3.model.ButtonType
import com.example.androidstudioexam.chapter3.model.CalculatorState
import com.example.androidstudioexam.chapter3.viewmodel.CalculatorViewModel
import com.example.androidstudioexam.databinding.CalculatorLayoutBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class CalculatorActivity : AppCompatActivity() {

  private var viewModel =  CalculatorViewModel()
  private lateinit var binding: CalculatorLayoutBinding
  /**
   * 按钮配置列表
   * 集中管理所有按钮的配置，便于维护和修改
   * 布局：4列 x 6行
   */


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = CalculatorLayoutBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupButtons()
    observeViewModel()
  }

  private fun setupButtons() {
    buttonConfigs.forEach { config ->
      // 验证配置有效性
      if (!config.isValid()) {
        return@forEach
      }

      // 创建 MaterialButton
      val button = createButton(config)

      // 设置按钮在 GridLayout 中的位置
      val params = GridLayout.LayoutParams().apply {
        // 设置行和列
        rowSpec = GridLayout.spec(config.gridRow, 1f)
        columnSpec = GridLayout.spec(config.gridColumn, 1f)

        // 设置边距，使按钮之间有间距
        setMargins(8, 8, 8, 8)

        // 设置宽度和高度
        width = 0
        height = GridLayout.LayoutParams.WRAP_CONTENT
      }

      // 添加到 GridLayout
      binding.gridLayoutButtons.addView(button, params)
    }
  }

  /**
   * 创建按钮
   * 根据配置创建并配置 MaterialButton
   * @param config 按钮配置
   * @return 配置好的 MaterialButton
   */
  private fun createButton(config: ButtonConfig): MaterialButton {
    return MaterialButton(this).apply {
      // 设置按钮文本
      text = config.text

      // 设置按钮样式（根据类型）
      setButtonStyle(config.type)

      // 设置点击事件 - 统一委托给 ViewModel 处理
      setOnClickListener {
        viewModel.onButtonClick(config.text, config.type)
      }

      // 设置按钮最小高度，保证触摸区域足够大
      minimumHeight = resources.getDimensionPixelSize(R.dimen.button_min_height)

      // 设置文本大小
      textSize = 24f

      // 设置文本对齐方式
      gravity = Gravity.CENTER
    }
  }

  /**
   * 设置按钮样式
   * 根据按钮类型设置不同的背景色和文本色
   * @param type 按钮类型
   */
  private fun MaterialButton.setButtonStyle(type: ButtonType) {
    when (type) {
      ButtonType.NUMBER -> {
        // 数字按钮：白色背景，深色文字
        setBackgroundColor(getColor(R.color.button_number_background))
        setTextColor(getColor(R.color.button_text_primary))
      }
      ButtonType.OPERATOR -> {
        // 运算符按钮：紫色背景，白色文字
        setBackgroundColor(getColor(R.color.button_operator_background))
        setTextColor(getColor(R.color.button_text_on_primary))
      }
      ButtonType.FUNCTION -> {
        // 功能按钮：青色背景，深色文字
        setBackgroundColor(getColor(R.color.button_function_background))
        setTextColor(getColor(R.color.button_text_primary))
      }
      ButtonType.CLEAR -> {
        // 清除按钮：红色背景，白色文字
        setBackgroundColor(getColor(R.color.button_clear_background))
        setTextColor(getColor(R.color.button_text_on_primary))
      }
      ButtonType.EQUALS -> {
        // 等号按钮：紫色背景，白色文字（与运算符相同）
        setBackgroundColor(getColor(R.color.button_operator_background))
        setTextColor(getColor(R.color.button_text_on_primary))
      }
    }
  }

  /**
   * 观察 ViewModel 状态
   * 使用 Kotlin 协程和 StateFlow 实现响应式 UI 更新
   */
  private fun observeViewModel() {
    lifecycleScope.launch {
      viewModel.calculatorState.collect { state ->
        when (state) {
          is CalculatorState.Display -> {
            // 显示正常值
            binding.textDisplay.text = state.value
            binding.textDisplay.setTextColor(getColor(R.color.calculator_display_text))
          }
          is CalculatorState.Error -> {
            // 显示错误信息
            binding.textDisplay.text = getString(viewModel.getErrorOutput(state.error))
            binding.textDisplay.setTextColor(getColor(R.color.button_clear_background))
          }
          is CalculatorState.Clear -> {
            // 清除显示
            binding.textDisplay.text = getString(R.string.display_default)
            binding.textDisplay.setTextColor(getColor(R.color.calculator_display_text))
          }
        }
      }
    }
  }
}