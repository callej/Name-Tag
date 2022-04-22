package signature

const val NAME_FONT = "C:/roman.txt 10"
const val STATUS_FONT = "C:/medium.txt 5"
const val BORDER = "88"

fun main() {
    print("Enter name and surname: ")
    val name = readln()
    print("Enter person's status: ")
    val status = readln()
    println(Tag(name, Font(NAME_FONT), status, Font(STATUS_FONT), BORDER))
}