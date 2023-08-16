package com.disfluency.components.inputs.text

import android.util.Patterns
import androidx.core.text.isDigitsOnly

interface InputValidation {
    fun validate(input: String): Boolean
}

class MandatoryValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return input.isNotEmpty() && input.isNotBlank()
    }
}

class EmailValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.asPredicate().test(input)
    }
}

class DigitsOnlyValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return input.isDigitsOnly()
    }
}

class PasswordValidation: InputValidation {
    override fun validate(input: String): Boolean {
        //TODO: implementar una vez que se defina el formato de la password
        return true
    }
}

class EqualToValidation(private val referenceValue: String): InputValidation {
    override fun validate(input: String): Boolean {
        return referenceValue == input
    }
}

class NoValidation: InputValidation {
    override fun validate(input: String): Boolean {
        return true
    }
}