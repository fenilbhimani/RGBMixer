package com.example.rgbmixer

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var redSeekBar: SeekBar
    private lateinit var greenSeekBar: SeekBar
    private lateinit var blueSeekBar: SeekBar
    private lateinit var redEditText: EditText
    private lateinit var greenEditText: EditText
    private lateinit var blueEditText: EditText
    private lateinit var redSwitch: Switch
    private lateinit var greenSwitch: Switch
    private lateinit var blueSwitch: Switch
    private lateinit var colorView: TextView
    private lateinit var resetButton: TextView

    private var redValue = 0.5f
    private var greenValue = 0.5f
    private var blueValue = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redSeekBar = findViewById(R.id.red_seek_bar)
        greenSeekBar = findViewById(R.id.green_seek_bar)
        blueSeekBar = findViewById(R.id.blue_seek_bar)
        redEditText = findViewById(R.id.red_edit_text)
        greenEditText = findViewById(R.id.green_edit_text)
        blueEditText = findViewById(R.id.blue_edit_text)
        redSwitch = findViewById(R.id.red_switch)
        greenSwitch = findViewById(R.id.green_switch)
        blueSwitch = findViewById(R.id.blue_switch)
        colorView = findViewById(R.id.color_view)
        resetButton = findViewById(R.id.reset_button)

        redSeekBar.setOnSeekBarChangeListener(colorSeekBarChangeListener)
        greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChangeListener)
        blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChangeListener)

        redEditText.setOnEditorActionListener(colorEditTextListener)
        greenEditText.setOnEditorActionListener(colorEditTextListener)
        blueEditText.setOnEditorActionListener(colorEditTextListener)

        redSwitch.setOnCheckedChangeListener(redSwitchListener)
        greenSwitch.setOnCheckedChangeListener(greenSwitchListener)
        blueSwitch.setOnCheckedChangeListener(blueSwitchListener)

        resetButton.setOnClickListener {
            redSeekBar.progress = 50
            greenSeekBar.progress = 50
            blueSeekBar.progress = 50
            redEditText.setText("0.5")
            greenEditText.setText("0.5")
            blueEditText.setText("0.5")
            redSwitch.isChecked = true
            greenSwitch.isChecked = true
            blueSwitch.isChecked = true
            redValue = 0.5f
            greenValue = 0.5f
            blueValue = 0.5f
            updateColorView()
        }

        colorView.setOnClickListener {
            val text = colorView.text.toString()
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Hex Code copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        updateColorView()
    }

    private fun updateColorView() {
        val color = Color.rgb(
            (redValue * 255).toInt(),
            (greenValue * 255).toInt(),
            (blueValue * 255).toInt()
        )
        val hexColor1 = String.format("#%06X", 0xFFFFFF and color)
        Log.e("Color", hexColor1)
        colorView.setText(hexColor1)
        colorView.setBackgroundColor(color)
    }

    private fun updateColorFromSeekBar(seekBar: SeekBar) {
        val value = seekBar.progress / 100f
        when (seekBar.id) {
            R.id.red_seek_bar -> redValue = value
            R.id.green_seek_bar -> greenValue = value
            R.id.blue_seek_bar -> blueValue = value
        }
        updateColorView()
        updateEditTextValue(seekBar, value)
    }

    private fun updateColorFromEditText(editText: EditText) {
        val value = editText.text.toString().toFloatOrNull() ?: 0f
        when (editText.id) {
            R.id.red_edit_text -> redValue = value
            R.id.green_edit_text -> greenValue = value
            R.id.blue_edit_text -> blueValue = value
        }
        updateColorView()
        updateSeekBarProgress(editText, value)
    }

    private val redSwitchListener =
        CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            if (switch != null && isChecked) {
                //redValue = 1f
                redSeekBar.isEnabled = true
                redEditText.isEnabled = true
                redSwitch.setText("On")
            } else {
                redValue = 0f
                redSeekBar.isEnabled = false
                redEditText.isEnabled = false
                redEditText.setText("0.0")
                redSwitch.setText("Off")
            }
            updateColorView()
            updateEditTextValue(switch, redValue)
            updateSeekBarProgress(switch, redValue)
        }

    private val greenSwitchListener =
        CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            if (switch != null && isChecked) {
                //greenValue = 1f
                greenSeekBar.isEnabled = true
                greenEditText.isEnabled = true
                greenSwitch.setText("On")
            } else {
                greenValue = 0f
                greenSeekBar.isEnabled = false
                greenEditText.isEnabled = false
                greenEditText.setText("0.0")
                greenSwitch.setText("Off")
            }
            updateColorView()
            updateEditTextValue(switch, greenValue)
            updateSeekBarProgress(switch, greenValue)
        }

    private val blueSwitchListener =
        CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            if (switch != null && isChecked) {
                //blueValue = 1f
                blueSeekBar.isEnabled = true
                blueEditText.isEnabled = true
                blueSwitch.setText("On")
            } else {
                blueValue = 0f
                blueSeekBar.isEnabled = false
                blueEditText.isEnabled = false
                blueEditText.setText("0.0")
                blueSwitch.setText("Off")
            }
            updateColorView()
            updateEditTextValue(switch, blueValue)
            updateSeekBarProgress(switch, blueValue)
        }

    private fun updateEditTextValue(view: Any, value: Float) {
        when (view) {
            is SeekBar -> {
                val editText = when (view.id) {
                    R.id.red_seek_bar -> redEditText
                    R.id.green_seek_bar -> greenEditText
                    R.id.blue_seek_bar -> blueEditText
                    else -> throw IllegalArgumentException("Invalid SeekBar ID")
                }
                editText.setText(value.toString())
            }
            is Switch -> {
                val editText = when (view.id) {
                    R.id.red_switch -> redEditText
                    R.id.green_switch -> greenEditText
                    R.id.blue_switch -> blueEditText
                    else -> throw IllegalArgumentException("Invalid Switch ID")
                }
                editText.setText(value.toString())
            }
        }
    }

    private fun updateSeekBarProgress(view: Any, value: Float) {
        val progress = (value * 100).toInt()
        when (view) {
            is EditText -> {
                val seekBar = when (view.id) {
                    R.id.red_edit_text -> redSeekBar
                    R.id.green_edit_text -> greenSeekBar
                    R.id.blue_edit_text -> blueSeekBar
                    else -> throw IllegalArgumentException("Invalid EditText ID")
                }
                seekBar.progress = progress
            }
            is Switch -> {
                val seekBar = when (view.id) {
                    R.id.red_switch -> redSeekBar
                    R.id.green_switch -> greenSeekBar
                    R.id.blue_switch -> blueSeekBar
                    else -> throw IllegalArgumentException("Invalid Switch ID")
                }
                seekBar.progress = progress
            }
        }
    }

    private val colorSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                updateColorFromSeekBar(seekBar!!)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    private val colorEditTextListener = TextView.OnEditorActionListener { view, _, _ ->
        val editText = view as EditText
        updateColorFromEditText(editText)
        false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current values of the sliders and switches
        outState.putInt("red_progress", redSeekBar.progress)
        outState.putString("red_text", redEditText.text.toString())
        outState.putBoolean("red_switch", redSwitch.isChecked)
        outState.putInt("green_progress", greenSeekBar.progress)
        outState.putString("green_text", greenEditText.text.toString())
        outState.putBoolean("green_switch", greenSwitch.isChecked)
        outState.putInt("blue_progress", blueSeekBar.progress)
        outState.putString("blue_text", blueEditText.text.toString())
        outState.putBoolean("blue_switch", blueSwitch.isChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the previous values of the sliders and switches
        redSeekBar.progress = savedInstanceState.getInt("red_progress", 0)
        redEditText.setText(savedInstanceState.getString("red_text", "0.0"))
        redSwitch.isChecked = savedInstanceState.getBoolean("red_switch", true)
        greenSeekBar.progress = savedInstanceState.getInt("green_progress", 0)
        greenEditText.setText(savedInstanceState.getString("green_text", "0.0"))
        greenSwitch.isChecked = savedInstanceState.getBoolean("green_switch", true)
        blueSeekBar.progress = savedInstanceState.getInt("blue_progress", 0)
        blueEditText.setText(savedInstanceState.getString("blue_text", "0.0"))
        blueSwitch.isChecked = savedInstanceState.getBoolean("blue_switch", true)
    }
}


