package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color
import com.disfluency.R


enum class DisfluencyType(
    val fullName: String,
    val color: Color,
    val descriptionPt1: Int,
    val descriptionPt2: Int
) {
    V("vacilacion", Color(1.0f, 0.29f, 0.8f, 1.0f), R.string.v_description_1, R.string.v_description_2),
    I("interjeccion", Color(0.016f, 0.592f, 0.667f, 1.0f), R.string.i_description_1, R.string.i_description_2),
    M("modificacion", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.m_description_1, R.string.m_description_2),
    RF("repeticion de frase", Color(0.298f, 0.686f, 0.314f, 1.0f), R.string.rf_description_1, R.string.rf_description_2),
    RP("repeticion de palabra", Color(0.188f, 0.31f, 1.0f, 1.0f), R.string.rp_description_1, R.string.rp_description_2),
    Rsi("repeticion de silaba", Color(0.933f, 0.459f, 0.31f, 1.0f), R.string.rsi_description_1, R.string.rsi_description_2),
    Rs("repeticion de sonido", Color(1.0f, 0.216f, 0.157f, 1.0f), R.string.rs_description_1, R.string.rs_description_2),
    P("prolongacion", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.p_description_1, R.string.p_description_2),
    B("bloqueo", Color(1.0f, 0.29f, 0.8f, 1.0f), R.string.b_description_1,R.string.b_description_2),
    GG("glope de glotis",Color(0.016f, 0.592f, 0.667f, 1.0f), R.string.gg_description_1, R.string.gg_description_2),
    CA("cambios de altura", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.ca_description_1,R.string.ca_description_2),
    N("vocal neutralizada", Color(0.298f, 0.686f, 0.314f, 1.0f), R.string.n_description_1,R.string.n_description_2),
    TVi("tension visible", Color(0.188f, 0.31f, 1.0f, 1.0f), R.string.tvi_description_1,R.string.tvi_description_2),
    U("inspiracion retomada muy audible", Color(0.933f, 0.459f, 0.31f, 1.0f), R.string.u_description_1,R.string.u_description_2),
    D("deglucion", Color(1.0f, 0.757f, 0.035f, 1.0f), R.string.d_description_1,R.string.d_description_2),
    TAu("tension audible", Color(1.0f, 0.216f, 0.157f, 1.0f), R.string.tau_description_1,R.string.tau_description_2),
    Ac("aceleracion", Color(0.016f, 0.592f, 0.667f, 1.0f), R.string.ac_description_1,R.string.ac_description_2)
}
