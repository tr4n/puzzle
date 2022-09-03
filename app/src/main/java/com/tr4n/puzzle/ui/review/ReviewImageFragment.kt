package com.tr4n.puzzle.ui.review

import androidx.navigation.fragment.findNavController
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.databinding.FragmentReviewImageBinding
import com.tr4n.puzzle.extension.navigateWithAnim
import com.tr4n.puzzle.ui.game.GameFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewImageFragment :
    BaseFragment<FragmentReviewImageBinding, ReviewImageViewModel>(),
    ReviewImageFragmentListener {

    override val layoutResId: Int
        get() = R.layout.fragment_review_image

    override val viewModel: ReviewImageViewModel by viewModel()

    override fun setBindingVariables() {
        super.setBindingVariables()
        viewBD.viewModel = viewModel
        viewBD.listener = this
    }

    override fun initData() {
        viewModel.getRecentTakenImageBitmap()
    }

    override fun observe() {
        super.observe()
        viewModel.challenge.observe(viewLifecycleOwner) { challenge ->
            val bundle = GameFragmentArgs.Builder(challenge).build().toBundle()
            findNavController().navigateWithAnim(R.id.gameFragment, bundle)
        }
    }

    override fun cancel() {
        findNavController().popBackStack()
    }
}

interface ReviewImageFragmentListener : DataBindingListener {

    fun cancel()
}
