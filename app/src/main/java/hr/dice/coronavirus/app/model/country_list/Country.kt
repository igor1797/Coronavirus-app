package hr.dice.coronavirus.app.model.country_list

data class Country(
    val name: String,
    val slug: String,
    val iso2: String
)

fun Country.getFlagEmoji(): String {
    val firstLetter = Character.codePointAt(iso2, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(iso2, 1) - 0x41 + 0x1F1E6

    if (!iso2[0].isLetter() || !iso2[1].isLetter()) {
        return iso2
    }

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
