package org.rodbergrsquared.recipebox.data.datasource

import android.content.Context
import android.net.Uri
import com.opencsv.CSVReaderBuilder
import org.rodbergrsquared.recipebox.data.model.Recipe
import org.rodbergrsquared.recipebox.data.model.RecipeEntry
import java.io.InputStreamReader

class CsvFileDataSource(private val context: Context) {
    fun loadRecipes(uri: Uri): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val csvReader = CSVReaderBuilder(reader)
                        .withSkipLines(1)
                        .build()
                    csvReader.forEach { row ->
                        try {
                            if (row.size >= 4) {
                                val recipe = Recipe(
                                    id = row[0].trim().toInt(),
                                    category = row[1].trim(),
                                    name = row[2].trim(),
                                    credit = row[3].trim()
                                )
                                recipeList.add(recipe)
                            }
                        } catch (e: Exception) {
                            // Skip malformed row
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Log error for the whole file reading process
            e.printStackTrace()
        }
        return recipeList
    }

    fun loadEntries(uri: Uri): List<RecipeEntry> {
        val entryList = mutableListOf<RecipeEntry>()
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val csvReader = CSVReaderBuilder(reader)
                        .withSkipLines(1)
                        .build()
                    csvReader.forEach { row ->
                        try {
                            if (row.size >= 5) {
                                val entry = RecipeEntry(
                                    recipeId = row[0].trim().toInt(),
                                    cardIndex = row[1].trim().toInt(),
                                    quantity = row[2].trim(),
                                    units = row[3].trim(),
                                    ingredient = row[4].trim(),
                                    note = if (row.size > 5) row[5].trim() else ""
                                )
                                entryList.add(entry)
                            }
                        } catch (e: Exception) {
                            // Skip malformed row
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Log error for the whole file reading process
            e.printStackTrace()
        }
        return entryList
    }
}
