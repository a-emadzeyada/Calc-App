package com.example.calcapp.di

import com.example.calcapp.domain.Calculator
import com.example.calcapp.domain.CalculatorImpl
import com.example.calcapp.ui.CalculatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Calculator> { CalculatorImpl() }

    viewModel {
        CalculatorViewModel(
            calculator = get()
        )
    }
}


