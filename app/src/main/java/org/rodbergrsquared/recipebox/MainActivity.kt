package org.rodbergrsquared.recipebox

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.rodbergrsquared.recipebox.ui.RecipeAdapter
import org.rodbergrsquared.recipebox.data.RecipeRepository

class MainActivity : AppCompatActivity() {

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    private var recipeFileUri: Uri? = null
    private var entryFileUri: Uri? = null

    private val openRecipeFileLauncher =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            recipeFileUri = it
            Toast.makeText(this,
                "Recipes file selected. Now select entries file.",
                Toast.LENGTH_LONG).show()
            // After picking the first file,
            // immediately launch the picker for the second file.
            openEntryFileLauncher.launch(arrayOf("*/*"))
        }
    }

    private val openEntryFileLauncher =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            entryFileUri = it
            // Now that we have both files, we can process them.
            loadAndDisplayData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeRepository = RecipeRepository(this)
        setupRecyclerView()
        val loadButton = findViewById<Button>(R.id.loadCsvButton)

        loadButton.setOnClickListener {
            // Start the file picking process for the first file (recipes.csv)
            recipeFileUri = null
            entryFileUri = null
            openRecipeFileLauncher.launch(arrayOf("*/*")) // We want any file type
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Start with an empty adapter
        adapter = RecipeAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun loadAndDisplayData() {
        if (recipeFileUri != null && entryFileUri != null) {
            // Tell the repository to load data from the selected files
            recipeRepository.loadDataFromCsv(
                recipeFileUri!!, entryFileUri!!)

            // Get the newly loaded and combined recipes
            val recipeList = recipeRepository.getFullRecipes()

            // Update the adapter's data and notify it to refresh the UI
            adapter.updateData(recipeList)

            Toast.makeText(this, "Successfully loaded ${recipeList.size} recipes!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Could not load files.", Toast.LENGTH_SHORT).show()
        }
    }
}