package com.example.tiptime

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiptime.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var total = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        total = savedInstanceState?.getDouble("total") ?: 0.0
        binding.tipAmount.text = "$$total"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.calcBtn.setOnClickListener {
            val text = binding.serviceEditText.text.toString()

            if (text.isBlank())
                binding.serviceEditText.error = "Please enter a value"
            else {
                val cost = binding.serviceEditText.text.toString().toDouble()

                val tip = when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.amazing_rb -> 0.20
                    R.id.good_rb -> 0.18
                    else -> 0.15
                }
                total = tip * cost

                if (binding.roundUpTip.isChecked) {
                    total = kotlin.math.ceil(total)
                }
                binding.tipAmount.text = tip.toString()

                binding.tipAmount.text = "$$total"
                binding.serviceEditText.clearFocus()
                Snackbar
                    .make(binding.root, getText(R.string.reset), BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(getText(R.string.pro)) {
                        binding.serviceEditText.text?.clear()
                        binding.radioGroup.check(R.id.amazing_rb)
                        binding.roundUpTip.isChecked = true
                        binding.tipAmount.text = "Tip Amount"
                    }
                    .setActionTextColor(getColor(android.R.color.holo_purple))
                    .show()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("total", total)
    }
}