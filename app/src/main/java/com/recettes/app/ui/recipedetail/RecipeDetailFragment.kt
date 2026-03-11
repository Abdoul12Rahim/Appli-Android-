package com.recettes.app.ui.recipedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.recettes.app.R
import com.recettes.app.data.model.MealDetail
import com.recettes.app.databinding.FragmentRecipeDetailBinding
import com.recettes.app.databinding.LayoutEmptyStateBinding
import com.recettes.app.util.UiState

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailFragmentArgs by navArgs()
    private val viewModel: RecipeDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title immediately from nav arg while loading
        binding.collapsingToolbar.title = args.mealName

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        observeViewModel()
        viewModel.loadDetail(args.mealId)
    }

    private fun observeViewModel() {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showDetail(state.data)
                is UiState.Error   -> showError(state.message)
                is UiState.Empty   -> showError(getString(R.string.empty_title))
            }
        }
    }

    private fun showLoading() {
        binding.shimmerDetail.isVisible = true
        binding.shimmerDetail.startShimmer()
        binding.contentLayout.isVisible = false
        binding.errorState.isVisible = false
    }

    private fun showDetail(detail: MealDetail) {
        binding.shimmerDetail.stopShimmer()
        binding.shimmerDetail.isVisible = false
        binding.errorState.isVisible = false
        binding.contentLayout.isVisible = true

        // Hero image
        Glide.with(this)
            .load(detail.thumbnail)
            .placeholder(R.color.shimmer_base)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .centerCrop()
            .into(binding.imageMeal)

        // Toolbar title
        binding.collapsingToolbar.title = detail.name
        binding.textMealTitle.text = detail.name

        // Tags chips
        binding.chipCategory.text = detail.category ?: ""
        binding.chipCategory.isVisible = !detail.category.isNullOrBlank()
        binding.chipArea.text = detail.area ?: ""
        binding.chipArea.isVisible = !detail.area.isNullOrBlank()

        // Instructions
        val instructions = detail.instructions?.trim()
        binding.textInstructions.text =
            if (instructions.isNullOrBlank()) getString(R.string.no_instructions)
            else instructions

        // Ingredients
        binding.layoutIngredients.removeAllViews()
        detail.getIngredientList().forEach { (ingredient, measure) ->
            addIngredientRow(binding.layoutIngredients, ingredient, measure)
        }
    }

    private fun addIngredientRow(container: LinearLayout, ingredient: String, measure: String) {
        val context = container.context
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, dpToPx(10)) }
        }

        // Bullet dot
        val dot = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(dpToPx(8), dpToPx(8)).apply {
                setMargins(0, dpToPx(6), dpToPx(12), 0)
            }
            setBackgroundResource(R.drawable.shape_chip_background)
            background?.setTint(resources.getColor(R.color.brand_primary, context.theme))
        }

        // Ingredient name
        val ingView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = ingredient
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
            val tv = android.util.TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, tv, true)
            setTextColor(tv.data)
        }

        // Measure
        val measView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = measure
            setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            val tv = android.util.TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, tv, true)
            setTextColor(tv.data)
        }

        row.addView(dot)
        row.addView(ingView)
        row.addView(measView)
        container.addView(row)
    }

    private fun showError(message: String) {
        binding.shimmerDetail.stopShimmer()
        binding.shimmerDetail.isVisible = false
        binding.contentLayout.isVisible = false
        binding.errorState.isVisible = true

        val errorBinding = LayoutEmptyStateBinding.bind(binding.errorState.root)
        errorBinding.textEmptyTitle.text = getString(R.string.error_title)
        errorBinding.textEmptySubtitle.text = message
        errorBinding.imageEmpty.setImageResource(R.drawable.ic_no_wifi)
        errorBinding.btnRetry.isVisible = true
        errorBinding.btnRetry.setOnClickListener { viewModel.loadDetail(args.mealId) }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
