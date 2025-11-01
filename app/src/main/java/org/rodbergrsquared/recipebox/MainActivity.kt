package org.rodbergrsquared.recipebox

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.rodbergrsquared.recipebox.data.repository.RecipeRepository
import org.rodbergrsquared.recipebox.ui.OnRecipeClickListener
import org.rodbergrsquared.recipebox.ui.RecipeAdapter

class MainActivity : AppCompatActivity(), OnRecipeClickListener {

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var adapter: RecipeAdapter
    private lateinit var topActionButton: Button
    private lateinit var printFab: FloatingActionButton

    private var recipeFileUri: Uri? = null
    private var entryFileUri: Uri? = null

    // Dictates whether the UI is in detail view or not.
    private var isDetailView = false

    private val openRecipeFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            recipeFileUri = it
            Toast.makeText(this, "@striing/open_recipe_toast", Toast.LENGTH_SHORT).show()
            openEntryFileLauncher.launch(arrayOf("*/*"))
        }
    }

    private val openEntryFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            entryFileUri = it
            loadAndDisplayData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeRepository = RecipeRepository(this)

        topActionButton = findViewById(R.id.topActionButton)
        printFab = findViewById(R.id.printFab)

        setupRecyclerView()

        topActionButton.setOnClickListener {
            if (isDetailView) {
                showRecipeHeaders()
            } else {
                recipeFileUri = null
                entryFileUri = null
                openRecipeFileLauncher.launch(arrayOf("*/*"))
            }
        }

        // Load the initial static data on startup.
        showRecipeHeaders()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeAdapter(emptyList(), this)
        recyclerView.adapter = adapter
    }

    // Display data loaded from CSV files.
    private fun loadAndDisplayData() {
        if (recipeFileUri != null && entryFileUri != null) {
            // The repository now returns true for success and false for failure.
            val success = recipeRepository.loadDataFromCsv(recipeFileUri!!, entryFileUri!!)
            if (success) {
                Toast.makeText(this, "Successfully loaded recipes from CSV!", Toast.LENGTH_SHORT).show()
                showRecipeHeaders() // Refresh the UI with the new data.
            } else {
                // If loading failed, inform the user and remain on the static data.
                Toast.makeText(this, "Could not load CSV data. Please check the files.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Could not load files.", Toast.LENGTH_SHORT).show()
        }
    }

    // Show the list of recipe headers which provide a list
    // selectable items to click on.
    private fun showRecipeHeaders() {
        val recipeHeaders = recipeRepository.getRecipeHeaders()
        adapter.updateData(recipeHeaders)

        isDetailView = false
        topActionButton.text = getString(R.string.btn_csv_load)
        printFab.visibility = View.GONE
    }

    // Handle clicks on recipe headers in the list
    // providing the id to get the recipe detail
    override fun onRecipeSelected(recipeId: Int) {
        val selectedRecipe = recipeRepository.getRecipeById(recipeId)
        if (selectedRecipe != null) {
            adapter.updateData(listOf(selectedRecipe))

            isDetailView = true
            topActionButton.text = getString(R.string.back_to_list)
            printFab.visibility = View.GONE // Keep print disabled.
        }
    }
}
