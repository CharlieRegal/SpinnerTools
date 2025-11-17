package xyz.mattjashworth.sample
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import xyz.mattjashworth.sample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import xyz.mattjashworth.spinnertools.sheet.Spinner
import xyz.mattjashworth.spinnertools.sheet.enums.Mode

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    var data : ArrayList<ExampleObject> = ArrayList()

    var multiSelectData : List<ExampleMultiSelect> = ArrayList()

    var selectedItem : ExampleObject? = null
    var selectedItems : List<ExampleObject>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.fragment = this
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val ex = ExampleObject("Test", "Should Be Working")
        ex.ID = 5

        data = arrayListOf(
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


        multiSelectData = arrayListOf(
            ExampleMultiSelect(0, "Matt"),
            ExampleMultiSelect(1, "Charlie"),
            ExampleMultiSelect(2, "Andrew"),
            ExampleMultiSelect(3, "Robert"),
            ExampleMultiSelect(4, "Stephen"),
            ExampleMultiSelect(5, "Stuart"),
            ExampleMultiSelect(6, "Alfie"),
            ExampleMultiSelect(7, "Jamie"),
            ExampleMultiSelect(8, "Zoe"),
            ExampleMultiSelect(9, "Jenny"),
            ExampleMultiSelect(10, "Lauren"),
            ExampleMultiSelect(11, "Sam"),
            ExampleMultiSelect(12, "Julie")
        )


        val searchSpinner = findViewById<Spinner<ExampleObject>>(R.id.app_spinner)
        searchSpinner.setItems(data)
        searchSpinner.setOnItemSelectedListener(object : Spinner.OnItemSelectedListener<ExampleObject> {
            override fun onItemSelected(model: ExampleObject) {
                Snackbar.make(binding.root, model.name, Snackbar.LENGTH_LONG).show()
            }
        })
        searchSpinner.setOnMultiItemSelectedListener(object : Spinner.OnMultiItemSelectedListener<ExampleObject> {
            override fun onItemSelected(models: List<ExampleObject>) {
                Snackbar.make(binding.root, models.count().toString() + " Selected", Snackbar.LENGTH_LONG).show()
            }
        })


        searchSpinner.setSelectedItem(data[3])

        val programmaticSpinner = Spinner<String>(this,null)
        programmaticSpinner.setBackgroundColorValue(R.color.red)
        programmaticSpinner.setTextAndIconColor(R.color.purple_500)
        programmaticSpinner.setDismissWhenSelected(true)
        programmaticSpinner.setSelectMode(Mode.SINGLE)
        programmaticSpinner.setIsSearchable(true)
        programmaticSpinner.id = View.generateViewId()

        val params = ViewGroup.LayoutParams(MATCH_PARENT,WRAP_CONTENT)
        programmaticSpinner.layoutParams = params

        programmaticSpinner.setItems(data.map { it.nameAndAge }.toCollection(ArrayList()))
        programmaticSpinner.setTitle("Programmatic Spinner | SINGLE")

        binding.main.addView(programmaticSpinner)

        val constraintLayout = binding.main
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(programmaticSpinner.id,ConstraintSet.TOP,binding.appSpinner.id, ConstraintSet.BOTTOM)
        constraintSet.connect(programmaticSpinner.id,ConstraintSet.START,binding.main.id, ConstraintSet.START)
        constraintSet.connect(programmaticSpinner.id,ConstraintSet.END,binding.main.id, ConstraintSet.END)
        constraintSet.applyTo(constraintLayout)
    }
}