package com.disfluency.data.mock

import com.disfluency.model.analysis.*
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Object with mocked model examples used for preview testing purposes
 */
object MockedData {

    val patient = Patient(
        id = "aaa",
        name = "Pedro",
        lastName = "Dominguez",
        dateOfBirth = LocalDate.now(),
        email = "pedrito@gmail.com",
        joinedSince = LocalDate.now().minusWeeks(6),
        avatarIndex = 1,
        weeklyTurn = listOf(DayOfWeek.MONDAY),
        weeklyHour = LocalTime.of(0,5)
    )
    val patient2 = Patient(
        id = "aaaa",
        name = "Juan",
        lastName = "Guillermo",
        dateOfBirth = LocalDate.now(),
        email = "juani@gmail.com",
        joinedSince = LocalDate.now().minusWeeks(6),
        avatarIndex = 1,
        weeklyTurn = listOf(DayOfWeek.SUNDAY),
        weeklyHour = LocalTime.of(15, 30)
    )

    val therapist = Therapist(
        id = "asd",
        name = "Rodrigo",
        lastName = "Palacio",
        profilePictureUrl = 1,
        todayPatients = mutableListOf(patient, patient2)
    )

    val analysis = Analysis(
        "2",
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3",
        LocalDate.now(),
        listOf(
            AnalysedWord("La",1220f,1280f),
            AnalysedWord("fiabilidad",1400f,2140f, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420f,2500f),
            AnalysedWord("la",2600f,2660f),
            AnalysedWord("capacidad",2760f,3420f),
            AnalysedWord("de",3580f,3660f),
            AnalysedWord("un",3740f,3840f, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960f,4460f),
            AnalysedWord("o",4900f,4920f),
            AnalysedWord("componente",5020f,5680f, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240f,6440f),
            AnalysedWord("desempeñar",6600f,7340f),
            AnalysedWord("las",7460f,7580f),
            AnalysedWord("funciones",7640f,8200f),
            AnalysedWord("especificadas,",9040f,10020f),
            AnalysedWord("cuando",10920f,11140f, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220f,11280f, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420f,11640f),
            AnalysedWord("bajo",11980f,12220f),
            AnalysedWord("unas",12340f,12520f, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640f,13300f, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580f,13640f),
            AnalysedWord("periodo",13740f,14160f),
            AnalysedWord("de",14240f, 14280F, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo", 14380F, 14700F),
            AnalysedWord("determinados.", 14900F, 16660F)
        )
    )

    val analysisResults = AnalysisResults(
        totalWords = 153,
        totalDisfluencies = 23,
        totalPhrases = 29,
        cleanWordsCount = 130,
        fluencyIndex = 0.84f,
        avgDisfluenciesPerPhrase = 0.79f,
        disfluencyStats = listOf(
            DisfluencyTypeStats(
                type = DisfluencyType.Rs,
                count = 7,
                percentageInTotalWords = 0.04f,
                percentageInTotalDisfluencies = 0.31f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.I,
                count = 2,
                percentageInTotalWords = 0.02f,
                percentageInTotalDisfluencies = 0.12f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.V,
                count = 8,
                percentageInTotalWords = 0.06f,
                percentageInTotalDisfluencies = 0.39f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.Rsi,
                count = 4,
                percentageInTotalWords = 0.035f,
                percentageInTotalDisfluencies = 0.18f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.RP,
                count = 1,
                percentageInTotalWords = 0.01f,
                percentageInTotalDisfluencies = 0.05f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.RF,
                count = 1,
                percentageInTotalWords = 0.01f,
                percentageInTotalDisfluencies = 0.05f
            ),
            DisfluencyTypeStats(
                type = DisfluencyType.M,
                count = 0,
                percentageInTotalWords = 0.0f,
                percentageInTotalDisfluencies = 0f
            ),
        )
    )

    val longAnalysis = Analysis(
        "1",
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3",
        LocalDate.now(),
        listOf(
            AnalysedWord("La",1220f,1280f),
            AnalysedWord("fiabilidad",1400f,2140f, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420f,2500f),
            AnalysedWord("la",2600f,2660f),
            AnalysedWord("capacidad",2760f,3420f),
            AnalysedWord("de",3580f,3660f),
            AnalysedWord("un",3740f,3840f, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960f,4460f),
            AnalysedWord("o",4900f,4920f),
            AnalysedWord("componente",5020f,5680f, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240f,6440f),
            AnalysedWord("desempeñar",6600f,7340f),
            AnalysedWord("las",7460f,7580f),
            AnalysedWord("funciones",7640f,8200f),
            AnalysedWord("especificadas,",9040f,10020f),
            AnalysedWord("cuando",10920f,11140f, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220f,11280f, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420f,11640f),
            AnalysedWord("bajo",11980f,12220f),
            AnalysedWord("unas",12340f,12520f, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640f,13300f, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580f,13640f),
            AnalysedWord("periodo",13740f,14160f),
            AnalysedWord("de",14240f, 14280F, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo", 14380F, 14700F),
            AnalysedWord("determinados.", 14900F, 16660F),
            AnalysedWord("La",1220f,1280f),
            AnalysedWord("fiabilidad",1400f,2140f, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420f,2500f),
            AnalysedWord("la",2600f,2660f),
            AnalysedWord("capacidad",2760f,3420f),
            AnalysedWord("de",3580f,3660f),
            AnalysedWord("un",3740f,3840f, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960f,4460f),
            AnalysedWord("o",4900f,4920f),
            AnalysedWord("componente",5020f,5680f, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240f,6440f),
            AnalysedWord("desempeñar",6600f,7340f),
            AnalysedWord("las",7460f,7580f),
            AnalysedWord("funciones",7640f,8200f),
            AnalysedWord("especificadas,",9040f,10020f),
            AnalysedWord("cuando",10920f,11140f, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220f,11280f, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420f,11640f),
            AnalysedWord("bajo",11980f,12220f),
            AnalysedWord("unas",12340f,12520f, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640f,13300f, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580f,13640f),
            AnalysedWord("periodo",13740f,14160f),
            AnalysedWord("de",14240f, 14280F, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo", 14380F, 14700F),
            AnalysedWord("determinados.", 14900F, 16660F)
        ),
        analysisResults
    )
}