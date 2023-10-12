package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color
import com.disfluency.R
import com.disfluency.model.analysis.DisfluencyCategory.QUALITATIVE
import com.disfluency.model.analysis.DisfluencyCategory.QUANTITATIVE


enum class DisfluencyType(
    val fullName: String,
    val color: Color,
    val descriptionPt1: Int,
    val descriptionPt2: Int,
    val category: DisfluencyCategory
) {
    V("Vacilacion", Color(1.0f, 0.29f, 0.8f, 1.0f), R.string.v_description_1, R.string.v_description_2, QUANTITATIVE),
    I("Interjeccion", Color(0.016f, 0.592f, 0.667f, 1.0f), R.string.i_description_1, R.string.i_description_2, QUANTITATIVE),
    M("Modificacion", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.m_description_1, R.string.m_description_2, QUANTITATIVE),
    RF("Repeticion de frase", Color(0.298f, 0.686f, 0.314f, 1.0f), R.string.rf_description_1, R.string.rf_description_2, QUANTITATIVE),
    RP("Repeticion de palabra", Color(0.188f, 0.31f, 1.0f, 1.0f), R.string.rp_description_1, R.string.rp_description_2, QUANTITATIVE),
    Rs("Repeticion de sonido", Color(1.0f, 0.216f, 0.157f, 1.0f), R.string.rs_description_1, R.string.rs_description_2, QUANTITATIVE),
    Rsi("Repeticion de silaba", Color(0.933f, 0.459f, 0.31f, 1.0f), R.string.rsi_description_1, R.string.rsi_description_2, QUANTITATIVE),
    P("Prolongacion", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.p_description_1, R.string.p_description_2, QUANTITATIVE),
    N("Vocal neutralizada", Color(1.0f, 0.29f, 0.8f, 1.0f), R.string.rsi_description_1, R.string.rsi_description_2, QUALITATIVE),
    U("Inspiración audible", Color(0.016f, 0.592f, 0.667f, 1.0f), R.string.i_description_1, R.string.i_description_2, QUALITATIVE),
    D("Deglución", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.m_description_1, R.string.m_description_2, QUALITATIVE)
}

enum class DisfluencyCategory {
    QUALITATIVE, QUANTITATIVE
}