package com.example.calcapp.domain

interface Calculator {
    fun input(symbol: String): String
    fun clear(): String
    fun delete(): String
    fun evaluate(): String
}


