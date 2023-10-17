package com.practicum.playlistmaker.utils

//Константы для Koin
const val QUALIFIER_IMAGE_DIRECTORY = "imageDirectory"

fun getTrackCountNoun(count: Int): String {
    val lastDigit = count % 100
    if (lastDigit in 11..14)
        return "треков"
    return when (lastDigit % 10) {
        1 -> "трек"
        2, 3, 4 -> "трека"
        else -> "треков"
    }
}

fun getMinuteCountNoun(count: Long): String {
    val lastDigit = (count % 100).toInt()
    if (lastDigit in 11..14)
        return "минут"
    return when (lastDigit % 10) {
        1 -> "минута"
        2, 3, 4 -> "минуты"
        else -> "минут"
    }
}