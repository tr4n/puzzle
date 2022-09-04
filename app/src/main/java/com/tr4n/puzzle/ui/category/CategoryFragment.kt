package com.tr4n.puzzle.ui.category

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.data.model.Preview
import com.tr4n.puzzle.databinding.DialogDeletePreviewBinding
import com.tr4n.puzzle.databinding.FragmentCategoryBinding
import com.tr4n.puzzle.extension.navigateWithAnim
import com.tr4n.puzzle.extension.setOnSafeClickListener
import com.tr4n.puzzle.listener.OnSimpleItemClick
import com.tr4n.puzzle.ui.game.GameFragmentArgs
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.DialogUtils
import com.tr4n.puzzle.util.setImageFromFile

class CategoryFragment :
    BaseFragment<FragmentCategoryBinding, CategoryViewModel>(),
    CategoryFragmentListener {

    override val layoutResId: Int
        get() = R.layout.fragment_category

    override val viewModel: CategoryViewModel by viewModels()

    private val args: CategoryFragmentArgs by navArgs()

    override fun initData() {
        viewModel.getCategoryData(args.category)
    }

    override fun setBindingVariables() {
        viewBD.lifecycleOwner = viewLifecycleOwner
        viewBD.listener = this
        viewBD.viewModel = viewModel
    }

    override fun popBackStack() {
        findNavController().popBackStack()
    }

    override fun onClick(item: Preview) {
        val bundle = GameFragmentArgs.Builder(item.challenge).build().toBundle()
        findNavController().navigateWithAnim(R.id.gameFragment, bundle)
    }

    override fun onClick(item: Preview, position: Int) {
        val binding = DialogDeletePreviewBinding.inflate(LayoutInflater.from(context))
        val dialog = DialogUtils.createCustomDialog(binding)
        binding.imageChallenge.setImageFromFile(
            item.challenge.imageName,
            BitmapUtils.DEFAULT_DECODED_IMAGE_SIZE
        )
        binding.textCancel.setOnSafeClickListener {
            dialog.dismiss()
        }
        binding.textDelete.setOnSafeClickListener {
            dialog.dismiss()
            viewModel.deleteChallenge(item.challenge)
        }
        dialog.setCancelable(false)
        dialog.show()
    }
}

interface CategoryFragmentListener : DataBindingListener, OnSimpleItemClick<Preview> {

    fun popBackStack()
}
