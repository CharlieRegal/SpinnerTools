# <img src ="https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Kotlin_logo_2021.svg/1920px-Kotlin_logo_2021.svg.png" width=48> SpinnerTools [![](https://jitpack.io/v/MattJAshworth/SpinnerTools.svg)](https://jitpack.io/#MattJAshworth/SpinnerTools)
Spinner View for Android with bottomsheet and searching built in

# Screenshots
<img src="/screenshots/spinnertools.gif" width="25%" alt="Spinner Tools Demo">

# Download
## build.gradle (Groovy)
Add to your project level `build.gradle`
```Java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
Add a dependency to your module `build.gradle`:
```Java
dependencies {
    implementation 'com.github.MattJAshworth:SpinnerTools:1.2'
}
```

## build.gradle.kts (Kotlin DSL)

Add to `settings.gradle.kts`
```Kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }

    }
}
```

Add a dependency to your `build.gradle.kts`:
```Kotlin
dependencies {
    implementation("com.github.MattJAshworth:SpinnerTools:1.2")
}
```
# Implementation
Add the `xyz.mattjashworth.spinnertools.sheet.Spinner` to your layout XML file.

Below are all the Spinners's xml attributes
```XML
<xyz.mattjashworth.spinnertools.sheet.Spinner
    app:DismissWhenSelected="true"
    app:Title="Select Person"
    app:Searchable="true"
    app:DisplayMember="name"
    app:backgroundColor="@color/purple_500"
    app:textColor="@color/white"
    app:hintTextColor="@color/red"
    app:mode="MULTI"
    app:hint_bottomMargin="5dp"/>
```

When binding to the view element specify the type. For example `ExampleObject`
```Kotlin
data class ExampleObject(
    val name: String,
    val age: String
)
```
If using Multi Select Mode your type should contain an id. For example `ExampleMultiSelect`
```Kotlin
data class ExampleMultiSelect(
    val id: Int,
    val name: String
)
```
Find The view
```Kotlin
val searchSpinner = findViewById<Spinner<ExampleObject>>(R.id.app_spinner)
```

Set the spinner items by calling `setItems()`
```Kotlin
searchSpinner.setItems(data)
```

Attach an item selected listener to return the item selected by the user. The listener returns the type specified. In this case `ExampleObject`.
```Kotlin
searchSpinner.setOnItemSelectedListener(object : Spinner.OnItemSelectedListener<ExampleObject> {
    override fun onItemSelected(model: ExampleObject) {
        Snackbar.make(rootView, model.name, Snackbar.LENGTH_LONG).show()
    }

})
```        
If using Multi Select Mode, set a different listener
```Kotlin
searchSpinner.setOnMultiItemSelectedListener(object : Spinner.OnMultiItemSelectedListener<ExampleMultiSelect> {
    override fun onItemSelected(models: List<ExampleMultiSelect>) {
        Snackbar.make(rootView, models.count().toString() + " Selected", Snackbar.LENGTH_LONG).show()
    }
})
```
### Note
- When using Multi Select Mode the dialog will not dismiss after selecting an object. The XML attribute `DismissWhenSelected` is unused.
- When using either Multi Select Mode or Single Select Mode you should use either String or a custom data type. String will automatically show in the sheet and as a selected item. A custom data type requires the XML attribute `DisplayMember` to be set. Failure will result in the string literal of the last property in your custom data type being used.

# License
```
MIT License

Copyright (c) 2025 Matt J Ashworth

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```