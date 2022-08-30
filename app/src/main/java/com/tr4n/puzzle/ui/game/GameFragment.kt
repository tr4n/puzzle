package com.tr4n.puzzle.ui.game

import androidx.recyclerview.widget.GridLayoutManager
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.base.recyclerview.SimpleBindingAdapter
import com.tr4n.puzzle.data.Puzzle
import com.tr4n.puzzle.databinding.FragmentGameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : BaseFragment<FragmentGameBinding, GameViewModel>(), GameFragmentListener {
    override val layoutResId: Int
        get() = R.layout.fragment_game

    override val viewModel: GameViewModel by viewModel()

    private val puzzleAdapter = SimpleBindingAdapter<Puzzle>(R.layout.item_puzzle)

    override fun setBindingVariables() {
        super.setBindingVariables()
        viewBD.viewModel = viewModel
        viewBD.listener = this
    }

    override fun observe() {
        viewModel.currentSize.observe(viewLifecycleOwner) {
            val layoutManager = GridLayoutManager(requireContext(), it)
            viewBD.recyclerPuzzles.layoutManager = layoutManager
        }
    }

    override fun onIncreaseSize() {
        viewModel.increaseSize()
    }
}

interface GameFragmentListener : DataBindingListener {

    fun onIncreaseSize()
}
