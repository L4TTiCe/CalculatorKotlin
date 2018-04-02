package io.github.l4ttice.calculatorkotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Double
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class MainActivity : AppCompatActivity() {

    internal lateinit var contents: ArrayList<String>
    internal lateinit var item: String

    fun recognize(s: String): String {              //method divide String on numbers and operators
        contents = ArrayList()         //holds numbers and operators
        item = ""
        for (i in s.length - 1 downTo 0) {           //is scan String from right to left,
            if (Character.isDigit(s[i])) {     //Strings are added to list, if scan finds
                item = s[i] + item              //a operator, or beginning of String
                if (i == 0) {
                    put()
                }
            } else {
                if (s[i] == '.') {
                    item = s[i] + item
                } else if (s[i] == '-' && (i == 0 || !Character.isDigit(s[i - 1]))) {
                    item = s[i] + item          //this part should recognize
                    put()                    //negative numbers
                } else {
                    put()                //it add already formed number and
                    item += s[i]          //operators to list
                    put()                //as separate Strings
                    if (s[i] == '|') {       //add empty String to list, before "|" sign,
                        item += " "          //to avoid removing of any meaningful String
                        put()        //in last part of result method
                    }
                }
            }
        }
        contents = result(contents, "^", "|")    //check Strings
        contents = result(contents, "*", "/")    //for chosen
        contents = result(contents, "+", "-")    //operators
        return contents[0]
    }

    fun put() {
        if (item != "") {
            contents.add(0, item)
            item = ""
        }
    }

    fun result(arrayList: ArrayList<String>, op1: String, op2: String): ArrayList<String> {
        val scale = 10                              //controls BigDecimal decimal point accuracy
        var result = BigDecimal(0)
        var c = 0
        while (c < arrayList.size) {
            if (arrayList[c] == op1 || arrayList[c] == op2) {
                if (arrayList[c] == "^") {
                    result = BigDecimal(arrayList[c - 1]).pow(Integer.parseInt(arrayList[c + 1]))
                } else if (arrayList[c] == "|") {
                    result = BigDecimal(Math.sqrt(Double.parseDouble(arrayList[c + 1])))
                } else if (arrayList[c] == "*") {
                    result = BigDecimal(arrayList[c - 1]).multiply(BigDecimal(arrayList[c + 1]))
                } else if (arrayList[c] == "/") {
                    result = BigDecimal(arrayList[c - 1]).divide(BigDecimal(arrayList[c + 1]), scale, BigDecimal.ROUND_DOWN)
                } else if (arrayList[c] == "+") {
                    result = BigDecimal(arrayList[c - 1]).add(BigDecimal(arrayList[c + 1]))
                } else if (arrayList[c] == "-") {
                    result = BigDecimal(arrayList[c - 1]).subtract(BigDecimal(arrayList[c + 1]))
                }
                try {       //in a case of to "out of range" ex
                    arrayList[c] = result.setScale(scale, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString()
                    arrayList.removeAt(c + 1)            //it replace the operator with result
                    arrayList.removeAt(c - 1)              //and remove used numbers from list
                } catch (ignored: Exception) {
                }

            } else {
                c++
                continue
            }
            c = 0                     //loop reset, as arrayList changed size
            c++
        }
        return arrayList
    }

    fun calcBODMAS(input: String): String {
        return brackets(input)
    }

    fun brackets(s: String): String {             //method which deal with brackets separately
        var s = s
        while (s.contains(Character.toString('(')) || s.contains(Character.toString(')'))) {
            var o = 0
            while (o < s.length) {
                try {                                                        //i there is not sign
                    if ((s[o] == ')' || Character.isDigit(s[o])) //between separate brackets
                            && s[o + 1] == '(') {                         //or number and bracket,
                        s = s.substring(0, o + 1) + "*" + s.substring(o + 1)        //it treat it as
                    }                                                       //a multiplication
                } catch (ignored: Exception) {
                }
                //ignore out of range ex
                if (s[o] == ')') {                                  //search for a closing bracket
                    var i = o
                    while (i >= 0) {
                        if (s[i] == '(') {                          //search for a opening bracket
                            var `in` = s.substring(i + 1, o)
                            `in` = recognize(`in`)
                            s = s.substring(0, i) + `in` + s.substring(o + 1)
                            o = 0
                            i = o
                        }
                        i--
                    }
                }
                o++
            }
            if (s.contains(Character.toString('(')) || s.contains(Character.toString(')')) ||
                    s.contains(Character.toString('(')) || s.contains(Character.toString(')'))) {
                println("Error: incorrect brackets placement")
                Snackbar.make(fab, "Error: incorrect brackets placement", Snackbar.LENGTH_LONG)
                        .setAction("RESET", View.OnClickListener {
                            editText.setText("")
                        }).show()
                return "Error"
            }
        }
        s = recognize(s)
        return s
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val input = Scanner(System.`in`)
            println("Enter an operation: ")
            val a = input.nextLine()

        }
    }

    fun setCursor() {
        val textLength = editText.text.length
        editText.setSelection(textLength,textLength)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset -> {
                editText.setText("")
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val txt = editText.text
            var out = ""
            out = calcBODMAS(txt.toString())
            if (out != "Error") {
                editText.setText(out)
                setCursor()
            } else {
                setCursor()
            }
        }

        BODMASswitch.isEnabled = false

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val txt = editText.text
                var out = ""
                out = calcBODMAS(txt.toString())
                editText.setText(out)
                setCursor()
                true
            } else
                false

        }

        button0.setOnClickListener{
            editText.text=editText.text.append('0')
            setCursor()
        }

        button1.setOnClickListener{
            editText.text=editText.text.append('1')
            setCursor()
        }
        button2.setOnClickListener{
            editText.text=editText.text.append('2')
            setCursor()
        }
        button3.setOnClickListener{
            editText.text=editText.text.append('3')
            setCursor()
        }
        button4.setOnClickListener{
            editText.text=editText.text.append('4')
            setCursor()
        }
        button5.setOnClickListener{
            editText.text=editText.text.append('5')
            setCursor()
        }
        button6.setOnClickListener{
            editText.text=editText.text.append('6')
            setCursor()
        }
        button7.setOnClickListener{
            editText.text=editText.text.append('7')
            setCursor()
        }
        button8.setOnClickListener{
            editText.text=editText.text.append('8')
            setCursor()
        }
        button9.setOnClickListener{
            editText.text=editText.text.append('9')
            setCursor()
        }
        buttonPT.setOnClickListener{
            editText.text=editText.text.append('.')
            setCursor()
        }
        buttonAC.setOnClickListener{
            var temp = editText.text.toString()
            temp = temp.substring(0, temp.length - 1)
            editText.setText(temp)
            setCursor()
        }
        buttonDIV.setOnClickListener{
            editText.text=editText.text.append('/')
            setCursor()
        }
        buttonADD.setOnClickListener{
            editText.text=editText.text.append('+')
            setCursor()
        }
        buttonSUB.setOnClickListener{
            editText.text=editText.text.append('-')
            setCursor()
        }
        buttonMUL.setOnClickListener{
            editText.text=editText.text.append('*')
            setCursor()
        }
        buttonbr1.setOnClickListener {
            editText.text = editText.text.append('(')
            setCursor()
        }
        buttonbr2.setOnClickListener {
            editText.text = editText.text.append(')')
            setCursor()
        }
    }

}
