package io.github.l4ttice.calculatorkotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.E

class MainActivity : AppCompatActivity() {

    fun setCursor() {
        val textLength = editText.text.length
        editText.setSelection(textLength,textLength)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //supportActionBar?.hide()

        fab.setOnClickListener {
            view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
            editText.setText("")
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
    }

}
