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

Below are all the YesNoButton's xml attributes. You cannot currently set these programmatically.
```XML
<xyz.mattjashworth.spinnertools.sheet.Spinner
    android:id="@+id/app_spinner"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginStart="10dp"
    android:layout_marginTop="10dp"
    android:layout_marginEnd="10dp"
    app:DismissWhenSelected="true"
    app:Title="Select Person"
    app:Searchable="true"
    app:DisplayMember="name"
    app:backgroundColor="@color/purple_500"
    app:textColor="@color/white"
    app:hintTextColor="@color/red"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

When binding to the view element specify the type. For example `ExampleObject`
```Kotlin
data class ExampleObject(
    val name: String,
    val age: String
)
```
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

# License
```
MIT License

Copyright (c) 2024 Matt J Ashworth

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