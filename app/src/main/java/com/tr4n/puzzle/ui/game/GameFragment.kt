package com.tr4n.puzzle.ui.game

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.data.type.Move
import com.tr4n.puzzle.databinding.FragmentGameBinding
import com.tr4n.puzzle.listener.OnSwipeTouchListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : BaseFragment<FragmentGameBinding, GameViewModel>(), GameFragmentListener {
    override val layoutResId: Int
        get() = R.layout.fragment_game

    override val viewModel: GameViewModel by viewModel()

    private val args by navArgs<GameFragmentArgs>()

    override fun setBindingVariables() {
        super.setBindingVariables()
        viewBD.viewModel = viewModel
        viewBD.listener = this
    }

    override fun setUpView() {
        viewBD.root.setOnSafeSwipeListener()
        viewBD.recyclerPuzzles.setOnSafeSwipeListener()
    }

    override fun initData() {
        viewModel.initChallenge(args.challenge)
    }

    private fun View.setOnSafeSwipeListener() {
        setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeTop() {
                viewModel.swipePuzzle(Move.UP)
            }

            override fun onSwipeBottom() {
                viewModel.swipePuzzle(Move.DOWN)
            }

            override fun onSwipeLeft() {
                viewModel.swipePuzzle(Move.LEFT)
            }

            override fun onSwipeRight() {
                viewModel.swipePuzzle(Move.RIGHT)
            }
        })
    }

    override fun observe() {
        super.observe()
        viewModel.isChallengeSaved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                findNavController().popBackStack(R.id.dashboardFragment, false)
            }
        }
    }

    override fun onBack() {
        viewModel.saveChallenge()
    }
}

interface GameFragmentListener : DataBindingListener {

    fun onBack()
}
