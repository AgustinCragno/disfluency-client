package com.disfluency.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disfluency.api.dto.NewFormDTO
import com.disfluency.api.error.FormCreationException
import com.disfluency.data.FormRepository
import com.disfluency.model.user.Therapist
import com.disfluency.viewmodel.states.ConfirmationState
import kotlinx.coroutines.launch

class FormCreationViewModel() : ViewModel(){

    private val formRepository = FormRepository()

    val creationConfirmationState: MutableState<ConfirmationState> = mutableStateOf(ConfirmationState.DONE)

    fun createFormForTherapist(therapist: Therapist, newFormDTO: NewFormDTO) = viewModelScope.launch {
        creationConfirmationState.value = ConfirmationState.LOADING
        try {
            val newForm = formRepository.createFormOfTherapist(therapist.id, newFormDTO)
            therapist.addForm(newForm)
            creationConfirmationState.value = ConfirmationState.SUCCESS
        }
        catch (exception: FormCreationException){
            creationConfirmationState.value = ConfirmationState.ERROR
        }
    }
}