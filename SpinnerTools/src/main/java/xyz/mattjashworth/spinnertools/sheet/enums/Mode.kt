package xyz.mattjashworth.spinnertools.sheet.enums

enum class Mode(val value: Int) {
    SINGLE(0),
    MULTI(1);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}