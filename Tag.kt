package signature

import java.io.File

data class Character(var width: Int, var ascii: List<String>)

class Font(fontData: String) {
    private val fontFile = File(fontData.split(" ").first())
    private val wordSpacing = fontData.split(" ").last().toInt()
    val characters = emptyMap<Char, Character>().toMutableMap()
    private var height = 0

    init {
        val chars = fontFile.readText().split(Regex("(\n[A-Za-z] \\d{1,2}\n)"))
        height = chars[0].split(" ").first().toInt()
        val meta = Regex("(\n[A-Za-z] \\d{1,2}\n)").findAll(fontFile.readText()).map { it.value.trim().split(" ") }
        meta.forEachIndexed { i, m -> characters[m[0].first()] = Character(m[1].toInt(), chars[i + 1].split("\n")) }
        characters[' '] = Character(wordSpacing, List(height) { " ".repeat(wordSpacing) })
        }

    fun height() = height
}

private data class Content(val text: String, val font: Font)

class Tag(fullName: String, nameFont: Font, status: String, statusFont: Font, private val border: String) {
    private val name = Content(fullName.trim().split(Regex("[^A-Za-z]+")).joinToString(" "), nameFont)
    private val stat = Content(status, statusFont)

    private fun len(content: Content): Int {
        var length = 0
        for (letter in content.text) {
            length += content.font.characters[letter]?.width ?: 0
        }
        return length
    }

    private fun getText(content: Content, row: Int): String {
        var textRow = ""
        for (letter in content.text) {
            textRow += content.font.characters[letter]?.ascii?.get(row) ?: ""
        }
        return textRow
    }

    override fun toString(): String {
        val size = maxOf(len(name), len(stat)) + 4 + 2 * border.length
        var tagString = ""
        tagString += border.repeat(size / border.length) + border.take(size%border.length) + "\n"
        var spaces = (size - len(name)) / 2 - border.length
        repeat(name.font.height()) { tagString += border + " ".repeat(spaces) + getText(name, it) + " ".repeat(size - len(name) - spaces - 2 * border.length) + border + "\n" }
        spaces = (size - len(stat)) / 2 - border.length
        repeat(stat.font.height()) { tagString += border + " ".repeat(spaces) + getText(stat, it) + " ".repeat(size - len(stat) - spaces - 2 * border.length) + border + "\n" }
        tagString += border.repeat(size / border.length) + border.take(size%border.length)
        return tagString
    }
}