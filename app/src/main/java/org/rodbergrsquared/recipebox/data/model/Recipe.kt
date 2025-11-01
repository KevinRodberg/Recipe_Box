package org.rodbergrsquared.recipebox.data.model

/** Recipe reference information for selectable list */
data class Recipe(
    val id: Int,
    val category: String,
    val name: String,
    val credit: String
)
