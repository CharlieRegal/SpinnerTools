package xyz.mattjashworth.sample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.mattjashworth.spinnertools.sheet.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rootView = findViewById<View>(R.id.main)

        val ex = ExampleObject("Test", "Should Be Working")
        ex.ID = 5

        val data = arrayListOf(
            ex,
            ExampleObject("Mohammed", "35"),
            ExampleObject("Charlie", "24"),
            ExampleObject("James", "21"),
            ExampleObject("Rachel", "22"),
            ExampleObject("Louise", "34"),
            ExampleObject("Nathan", "38"),
            ExampleObject("Jordan", "45"),
            ExampleObject("Olivia", "25"),
            ExampleObject("Matt", "27"),
            ExampleObject("Abbie", "23"),
            ExampleObject("Sophie", "27"),
            ExampleObject("Eve", "19"),
            ExampleObject("Emma", "33"),
            ExampleObject("Randy", "63"),
            ExampleObject("Stuart", "37"),
            ExampleObject("Andrew", "47"),
            ExampleObject("Robert", "29"),
            ExampleObject("Stephen", "??"),
            ExampleObject("Zed", "50"),
            ExampleObject("Steve", "24"),
            ExampleObject("Alfie", "27"),
            ExampleObject("Jamie", "27"),
            ExampleObject("Lynn", "24"),
            ExampleObject("Lauren", "27"),
            ExampleObject("Sam", "27"),
            ExampleObject("Craig", "24"),
            ExampleObject("Calum", "27"),
            ExampleObject("Katie", "27"),
            ExampleObject("Rebecca", "24"),
            ExampleObject("Vicky", "27"),

            )


        val searchSpinner = findViewById<Spinner<String>>(R.id.app_spinner)
        searchSpinner.setItems(arrayListOf("One", "Two", "Three", "Four", "Five", "Six"))
        searchSpinner.setOnItemSelectedListener(object : Spinner.OnItemSelectedListener<String> {
            override fun onItemSelected(model: String) {
                Snackbar.make(rootView, model, Snackbar.LENGTH_LONG).show()
            }

        })

        CoroutineScope(Dispatchers.IO).launch {
            delay(10000)
            runOnUiThread {
                searchSpinner.setItems(arrayListOf("One"))
            }
        }




    }
}