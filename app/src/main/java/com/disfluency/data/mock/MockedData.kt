package com.disfluency.data.mock

import com.disfluency.model.analysis.*
import java.time.LocalDate

/**
 * Object with mocked model examples used for preview testing purposes
 */
object MockedData {

    val analysis = Analysis(
        "2",
        "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3",
        LocalDate.now(),
        listOf(
            AnalysedWord("La",1220,1280),
            AnalysedWord("fiabilidad",1400,2140, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420,2500),
            AnalysedWord("la",2600,2660),
            AnalysedWord("capacidad",2760,3420),
            AnalysedWord("de",3580,3660),
            AnalysedWord("un",3740,3840, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960,4460),
            AnalysedWord("o",4900,4920),
            AnalysedWord("componente",5020,5680, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240,6440),
            AnalysedWord("desempeñar",6600,7340),
            AnalysedWord("las",7460,7580),
            AnalysedWord("funciones",7640,8200),
            AnalysedWord("especificadas,",9040,10020),
            AnalysedWord("cuando",10920,11140, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220,11280, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420,11640),
            AnalysedWord("bajo",11980,12220),
            AnalysedWord("unas",12340,12520, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640,13300, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580,13640),
            AnalysedWord("periodo",13740,14160),
            AnalysedWord("de",14240,14280, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo",14380,14700),
            AnalysedWord("determinados.",14900,16660)
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
            AnalysedWord("La",1220,1280),
            AnalysedWord("fiabilidad",1400,2140, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420,2500),
            AnalysedWord("la",2600,2660),
            AnalysedWord("capacidad",2760,3420),
            AnalysedWord("de",3580,3660),
            AnalysedWord("un",3740,3840, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960,4460),
            AnalysedWord("o",4900,4920),
            AnalysedWord("componente",5020,5680, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240,6440),
            AnalysedWord("desempeñar",6600,7340),
            AnalysedWord("las",7460,7580),
            AnalysedWord("funciones",7640,8200),
            AnalysedWord("especificadas,",9040,10020),
            AnalysedWord("cuando",10920,11140, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220,11280, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420,11640),
            AnalysedWord("bajo",11980,12220),
            AnalysedWord("unas",12340,12520, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640,13300, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580,13640),
            AnalysedWord("periodo",13740,14160),
            AnalysedWord("de",14240,14280, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo",14380,14700),
            AnalysedWord("determinados",14900,16660),
            AnalysedWord("fiabilidad",1400,2140, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420,2500),
            AnalysedWord("la",2600,2660),
            AnalysedWord("capacidad",2760,3420),
            AnalysedWord("de",3580,3660),
            AnalysedWord("un",3740,3840, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960,4460),
            AnalysedWord("o",4900,4920),
            AnalysedWord("componente",5020,5680, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240,6440),
            AnalysedWord("desempeñar",6600,7340),
            AnalysedWord("las",7460,7580),
            AnalysedWord("funciones",7640,8200),
            AnalysedWord("especificadas,",9040,10020),
            AnalysedWord("cuando",10920,11140, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220,11280, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420,11640),
            AnalysedWord("bajo",11980,12220),
            AnalysedWord("unas",12340,12520, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640,13300, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580,13640),
            AnalysedWord("periodo",13740,14160),
            AnalysedWord("de",14240,14280, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo",14380,14700),
            AnalysedWord("determinados",14900,16660),
            AnalysedWord("fiabilidad",1400,2140, listOf(DisfluencyType.V)),
            AnalysedWord("es",2420,2500),
            AnalysedWord("la",2600,2660),
            AnalysedWord("capacidad",2760,3420),
            AnalysedWord("de",3580,3660),
            AnalysedWord("un",3740,3840, listOf(DisfluencyType.RF)),
            AnalysedWord("sistema",3960,4460),
            AnalysedWord("o",4900,4920),
            AnalysedWord("componente",5020,5680, (listOf(DisfluencyType.M, DisfluencyType.I))),
            AnalysedWord("para",6240,6440),
            AnalysedWord("desempeñar",6600,7340),
            AnalysedWord("las",7460,7580),
            AnalysedWord("funciones",7640,8200),
            AnalysedWord("especificadas,",9040,10020),
            AnalysedWord("cuando",10920,11140, listOf(DisfluencyType.I)),
            AnalysedWord("se",11220,11280, listOf(DisfluencyType.V)),
            AnalysedWord("usa",11420,11640),
            AnalysedWord("bajo",11980,12220),
            AnalysedWord("unas",12340,12520, (listOf(DisfluencyType.V, DisfluencyType.RF))),
            AnalysedWord("condiciones",12640,13300, listOf(DisfluencyType.V)),
            AnalysedWord("y",13580,13640),
            AnalysedWord("periodo",13740,14160),
            AnalysedWord("de",14240,14280, listOf(DisfluencyType.RP)),
            AnalysedWord("tiempo",14380,14700),
            AnalysedWord("determinados.",14900,16660)
        ),
        analysisResults
    )
}