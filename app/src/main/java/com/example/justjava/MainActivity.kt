/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 */
package com.example.justjava


import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


/**
 * This app displays an order form to order coffee.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private var numberOfCoffees = 1
    private var priceOfCoffees = 5
    private var cream = ""
    private var chocolate = ""
    private var toppings = arrayListOf<String>()
    private var massageMail = ""

    /**
     * This method is called when the order button is clicked.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun submitOrder(view: View?) {
        val getTextContent: String = findViewById<EditText>(R.id.text_input).text.toString()
        val priceMessage = getString(R.string.name) + ": $getTextContent\n" +
                getString(R.string.toppings) +": ${toppings.toString().replace("[", "").replace("]", "")}\n"+
                getString(R.string.quantity) +": ${numberOfCoffees}\n" +
                getString(R.string.total) + ": $${calculatePrice()}\n" + getString(R.string.thank_you)
        massageMail = priceMessage
        composeEmail("",getString(R.string.coffee_order))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            displayMessage(priceMessage)

        }
    }

    private fun composeEmail(addresses: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:artem.nkfv@gmail.com") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, massageMail)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }




    /**
     * This method add toppings to order
     */

     fun onCheckboxClicked(view: View?) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkbox_cream -> {
                    if (checked) {
                        cream = "Cream"
                        toppings.add(cream)
                        priceOfCoffees += 1
                    } else {
                        cream = ""
                        toppings.remove("Cream")
                        priceOfCoffees = 5
                    }
                }
                R.id.checkbox_chocolate -> {
                    if (checked) {
                        chocolate = "Chocolate"
                        toppings.add(chocolate)
                        priceOfCoffees += 1
                    } else {
                        chocolate = ""
                        toppings.remove("Chocolate")
                        priceOfCoffees = 5
                    }
                }
            }
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private fun displayQuantity(number: Int) {
        val quantityTextView: TextView = findViewById(R.id.quantity_text_view)
        quantityTextView.text = "" + number
    }
    private fun calculatePrice(): Int {
        return numberOfCoffees * priceOfCoffees
    }

    /**
     * This method display the given string(message) on the screen
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayMessage(message: String) {
        val priceTextView: TextView = findViewById(R.id.price_text_view)
        priceTextView.text = message
    }


    /**
     * This method increment number of coffees order
     */
     fun increment(view: View?) {
        when(numberOfCoffees){
            in 1..99 -> numberOfCoffees ++
            100 -> Toast.makeText(this, "Maximum coffees order is 100",Toast.LENGTH_SHORT).show()
        }
        displayQuantity(numberOfCoffees)
    }

    /**
     * This method decrement number of coffees order
     */
     fun decrement(view: View?) {
        when(numberOfCoffees){
            in 2..100 -> numberOfCoffees--
            1 -> Toast.makeText(this, "Minimum coffees order is 1",Toast.LENGTH_SHORT).show()

        }
        displayQuantity(numberOfCoffees)
    }

}




