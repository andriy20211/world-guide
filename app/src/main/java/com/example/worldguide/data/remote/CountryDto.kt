package com.example.worldguide.data.remote

import com.example.worldguide.model.Country
import com.example.worldguide.model.Flags
import com.example.worldguide.model.Name
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("cca3") val cca3: String? = null,
    @SerialName("name") val name: NameDto? = null,
    @SerialName("capital") val capital: List<String>? = null,
    @SerialName("population") val population: Long? = null,
    @SerialName("region") val region: String? = null,
    @SerialName("subregion") val subregion: String? = null,
    @SerialName("area") val area: Double? = null,
    @SerialName("languages") val languages: Map<String, String>? = null,
    @SerialName("currencies") val currencies: Map<String, CurrencyDto>? = null,
    @SerialName("flags") val flags: FlagsDto? = null
)

@Serializable
data class NameDto(
    @SerialName("official") val official: String? = null,
    @SerialName("common") val common: String? = null
)

@Serializable
data class CurrencyDto(
    @SerialName("name") val name: String? = null,
    @SerialName("symbol") val symbol: String? = null
)

@Serializable
data class FlagsDto(
    @SerialName("png") val png: String? = null,
    @SerialName("svg") val svg: String? = null
)

// Мапер: перетворення DTO мережі у внутрішню модель domain-шару Country
fun CountryDto.toDomain(): Country {
    val countryId = cca3?.takeIf { it.isNotBlank() } ?: "${name?.common ?: "country"}_${population ?: 0L}"
    val commonName = name?.common ?: "Невідома країна"
    val officialName = name?.official ?: commonName

    val capList = capital?.ifEmpty { listOf("Немає столиці") } ?: listOf("Немає столиці")
    val langs = languages?.values?.toList() ?: listOf("Не вказано")
    val currs = currencies?.map { (code, curr) ->
        val cName = curr.name ?: code
        val cSymbol = curr.symbol?.let { " ($it)" } ?: ""
        "$cName$cSymbol"
    } ?: listOf("Не вказано")

    return Country(
        id = countryId,
        name = Name(official = officialName, common = commonName),
        capital = capList,
        population = population ?: 0L,
        region = region ?: "Невідомо",
        subregion = subregion ?: "Невідомо",
        area = area ?: 0.0,
        languages = langs,
        currencies = currs,
        flags = Flags(png = flags?.png ?: "", localDrawableId = null)
    )
}