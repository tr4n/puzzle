package com.tr4n.puzzle.ui.category

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.data.model.PreviewChallenge
import com.tr4n.puzzle.databinding.FragmentCategoryBinding
import com.tr4n.puzzle.extension.navigateWithAnim
import com.tr4n.puzzle.listener.OnSimpleItemClick
import com.tr4n.puzzle.ui.game.GameFragmentArgs

class CategoryFragment :
    BaseFragment<FragmentCategoryBinding, CategoryViewModel>(),
    CategoryFragmentListener {

    override val layoutResId: Int
        get() = R.layout.fragment_category

    override val viewModel: CategoryViewModel by viewModels()

    private val args: CategoryFragmentArgs by navArgs()

    override fun initData() {
        viewModel.getChallengesData(args.category)
    }

    override fun setBindingVariables() {
        viewBD.lifecycleOwner = viewLifecycleOwner
        viewBD.listener = this
        viewBD.viewModel = viewModel
    }

    override fun popBackStack() {
        findNavController().popBackStack()
    }

    override fun onClick(item: PreviewChallenge) {
        val bundle = GameFragmentArgs.Builder(item.challenge).build().toBundle()
        findNavController().navigateWithAnim(R.id.gameFragment, bundle)
    }
}

interface CategoryFragmentListener : DataBindingListener, OnSimpleItemClick<PreviewChallenge> {

    fun popBackStack()
}
