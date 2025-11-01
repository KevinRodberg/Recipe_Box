package org.rodbergrsquared.recipebox.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.rodbergrsquared.recipebox.R
import org.rodbergrsquared.recipebox.data.model.RecipeEntry

/** Adapter provides separate view holders for ingredients and
 * instruction steps with separate layouts.  */
class IngredientAdapter(private val entries: List<RecipeEntry>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // Changed to generic ViewHolder

    private val VIEWTYPEINGREDIENT = 0
    private val VIEWTYPESTEP = 1

    /** ViewHolder for ingredients (two-column layout) */
    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quantityAndUnits: TextView = itemView.findViewById(R.id.itemQtyByUnits)
        val name: TextView = itemView.findViewById(R.id.ingredientName)
    }
    /** ViewHolder for steps (single TextView layout) */
    class StepViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
            val instruction: TextView = itemView.findViewById(R.id.stepInstruction)
    }

    // Tells the adapter which layout to use for each item position.
    override fun getItemViewType(position: Int): Int {
        return if (entries[position].units.equals("step",
                ignoreCase = true)) {
            VIEWTYPESTEP
        } else {
            VIEWTYPEINGREDIENT
        }
    }

    // Inflates the correct layout based on the viewType.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEWTYPEINGREDIENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ingredient, parent, false)
            IngredientViewHolder(view)
        } else { // viewType is VIEWTYPESTEP
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_steps, parent, false)
            StepViewHolder(view)
        }
    }

    // Binds data to the correct ViewHolder type.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val entry = entries[position]
        when (holder.itemViewType) {
            VIEWTYPEINGREDIENT -> {
                val ingredientHolder = holder as IngredientViewHolder
                ingredientHolder.quantityAndUnits.text = "• ${entry.quantity} ${entry.units}"
                ingredientHolder.name.text = "${entry.ingredient} ${entry.note}"
            }
            VIEWTYPESTEP -> {
                val stepHolder = holder as StepViewHolder
                // If you want to include the note for steps, you can do this:
                if (entry.note.isNotBlank()) {
                    stepHolder.instruction.text = "${entry.ingredient} (${entry.note})"
                } else {
                    stepHolder.instruction.text = entry.ingredient
                }
            }
        }
    }

    override fun getItemCount() = entries.size
}
