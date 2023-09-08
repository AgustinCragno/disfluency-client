package com.disfluency.model.exercise

data class Exercise(
    val id: String,
    val title: String,
    val instruction: String,
    val phrase: String? = null,
    val sampleRecordingUrl: String
) {
    fun fullName(): String{
        return "$title $instruction $phrase"
    }

    fun getFullInstructions(): String{
        return if (phrase.isNullOrBlank()){
            instruction
        }else{
            "$instruction: $phrase"
        }
    }
}