package com.recettes.app.ui.recipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.search.SearchView
import com.recettes.app.R
import com.recettes.app.databinding.FragmentRecipeListBinding
import com.recettes.app.databinding.LayoutEmptyStateBinding
import com.recettes.app.util.UiState

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var searchAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { meal ->
            val action = RecipeListFragmentDirections
                .actionListToDetail(mealId = meal.id, mealName = meal.name)
            findNavController().navigate(action)
        }
        searchAdapter = RecipeAdapter { meal ->
            binding.searchView.hide()
            val action = RecipeListFragmentDirections
                .actionListToDetail(mealId = meal.id, mealName = meal.name)
            findNavController().navigate(action)
        }

        val spanCount = 2
        binding.recyclerRecipes.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }
        binding.recyclerSearchResults.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = searchAdapter
        }
    }

    private fun setupSearch() {
        // SearchView state changes
        binding.searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.HIDDEN) {
                viewModel.search("") // clear search
            }
        }
        binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
            viewModel.search(textView.text.toString())
            false
        }
        binding.searchView.editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {
                viewModel.search(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) = Unit
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.brand_primary, R.color.brand_secondary)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            binding.chipGroupCategories.removeAllViews()
            categories.forEachIndexed { index, category ->
                val chip = layoutInflater.inflate(
                    R.layout.item_category_chip,
                    binding.chipGroupCategories,
                    false
                ) as Chip
                chip.text = category.name
                chip.isChecked = index == 0
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) viewModel.loadMeals(category.name)
                }
                binding.chipGroupCategories.addView(chip)
            }
        }

        viewModel.mealsState.observe(viewLifecycleOwner) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showMeals(state.data)
                is UiState.Error   -> showError(state.message)
                is UiState.Empty   -> showEmpty(isError = false)
            }
        }

        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> { /* could show progress in search view */ }
                is UiState.Success -> searchAdapter.submitList(state.data)
                is UiState.Error   -> searchAdapter.submitList(emptyList())
                is UiState.Empty   -> searchAdapter.submitList(emptyList())
            }
        }

        viewModel.isOffline.observe(viewLifecycleOwner) { offline ->
            binding.offlineBanner.isVisible = offline
        }
    }

    private fun showLoading() {
        binding.shimmerLayout.isVisible = true
        binding.shimmerLayout.startShimmer()
        binding.swipeRefresh.isVisible = false
        binding.emptyState.isVisible = false
    }

    private fun showMeals(meals: List<com.recettes.app.data.model.Meal>) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        binding.swipeRefresh.isVisible = true
        binding.emptyState.isVisible = false
        recipeAdapter.submitList(meals)
    }

    private fun showError(message: String) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        binding.swipeRefresh.isVisible = false
        binding.emptyState.isVisible = true

        // Customise empty state for error
        val emptyBinding = LayoutEmptyStateBinding.bind(binding.emptyState.root)
        emptyBinding.textEmptyTitle.text = getString(R.string.error_title)
        emptyBinding.textEmptySubtitle.text = message
        emptyBinding.imageEmpty.setImageResource(R.drawable.ic_no_wifi)
        emptyBinding.btnRetry.isVisible = true
        emptyBinding.btnRetry.setOnClickListener { viewModel.refresh() }
    }

    private fun showEmpty(isError: Boolean) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        binding.swipeRefresh.isVisible = false
        binding.emptyState.isVisible = true

        val emptyBinding = LayoutEmptyStateBinding.bind(binding.emptyState.root)
        emptyBinding.textEmptyTitle.text = getString(R.string.empty_title)
        emptyBinding.textEmptySubtitle.text = getString(R.string.empty_subtitle)
        emptyBinding.imageEmpty.setImageResource(R.drawable.ic_empty_plate)
        emptyBinding.btnRetry.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
