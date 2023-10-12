package com.disfluency.api.error

import com.disfluency.api.dto.NewFormDTO

class FormCreationException(newForm: NewFormDTO) : Exception("An error occurred when creating form '${newForm.title}'")