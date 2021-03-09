package by.vlfl.lab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import by.vlfl.lab2.databinding.ActivityHistoryBinding
import java.io.File

class HistoryActivity : AppCompatActivity() {
    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!

    private val itemsAdapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val numbersFile = File(filesDir.toString(), FILE_NAME)

        if (numbersFile.exists()) {
            val fileContent = readFromFile()
                    .trimIndent()
                    .dropLast(1)
                    .split(",")
                    .map { it }
            itemsAdapter.items.addAll(fileContent)
        }

        binding.rvHistory.apply {
            adapter = itemsAdapter
            addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun readFromFile(): String {
        return openFileInput(FILE_NAME).bufferedReader().readLine()
    }

    companion object {
        private const val FILE_NAME = "numbers.txt"
    }
}