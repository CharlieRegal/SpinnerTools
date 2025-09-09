package xyz.mattjashworth.sample

data class ExampleObject(
    val name: String,
    val age: String
): ExampleNestedObject() {

    val nameAndAge: String
        get() = "$name, $age"

}
