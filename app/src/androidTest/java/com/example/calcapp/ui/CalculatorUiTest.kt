package com.example.calcapp.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun basic_addition_flow_displays_correct_result() {
        // 1 + 2 = 3
        composeTestRule.onNodeWithTag("btn_1").performClick()
        composeTestRule.onNodeWithTag("btn_plus").performClick()
        composeTestRule.onNodeWithTag("btn_2").performClick()
        composeTestRule.onNodeWithTag("btn_equals").performClick()

        composeTestRule.onNodeWithTag("display").assertTextEquals("3")
    }

    @Test
    fun clear_button_resets_display() {
        composeTestRule.onNodeWithTag("btn_7").performClick()
        composeTestRule.onNodeWithTag("btn_clear").performClick()

        composeTestRule.onNodeWithTag("display").assertTextEquals("0")
    }

    @Test
    fun power_operation_from_ui() {
        // 2 ^ 3 = 8
        composeTestRule.onNodeWithTag("btn_2").performClick()
        composeTestRule.onNodeWithTag("btn_pow").performClick()
        composeTestRule.onNodeWithTag("btn_3").performClick()
        composeTestRule.onNodeWithTag("btn_equals").performClick()

        composeTestRule.onNodeWithTag("display").assertTextEquals("8")
    }
}


