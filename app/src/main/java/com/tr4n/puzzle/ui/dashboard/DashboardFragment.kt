package com.tr4n.puzzle.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.data.model.PreviewChallenge
import com.tr4n.puzzle.data.type.Category
import com.tr4n.puzzle.databinding.FragmentDashboardBinding
import com.tr4n.puzzle.extension.navigateWithAnim
import com.tr4n.puzzle.extension.setAnimationResource
import com.tr4n.puzzle.extension.setOnScrollListener
import com.tr4n.puzzle.listener.OnSimpleItemClick
import com.tr4n.puzzle.ui.category.CategoryFragmentArgs
import com.tr4n.puzzle.ui.game.GameFragmentArgs
import com.tr4n.puzzle.util.DialogUtils
import com.tr4n.puzzle.util.RequestPermissionListener
import com.tr4n.puzzle.util.requestPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(),
    DashboardFragmentListener {
    override val layoutResId: Int
        get() = R.layout.fragment_dashboard

    override val viewModel: DashboardViewModel by viewModel()


    override fun setBindingVariables() {
        super.setBindingVariables()
        viewBD.viewModel = viewModel
        viewBD.listener = this
    }

    override fun setUpView() {
        setTextColorStatusBar(true)
        viewBD.nestedScrolLView.setOnScrollListener(
            onScrollTop = {
                viewBD.cardCamera.setAnimationResource(android.R.anim.fade_in, onEnd = {
                    viewBD.cardCamera.isVisible = true
                })
            }, onScrollDown = {
                viewBD.cardCamera.setAnimationResource(android.R.anim.fade_out, onEnd = {
                    viewBD.cardCamera.isVisible = false
                })
            },
            onScrollBottom = {
                viewBD.cardCamera.setAnimationResource(android.R.anim.fade_in, onEnd = {
                    viewBD.cardCamera.isVisible = true
                })
            }
        )
    }

    override fun initData() {
        viewModel.getLocalChallenges()
    }

    override fun observe() {
        super.observe()
        viewModel.isTemporaryFileSaved.observe(viewLifecycleOwner) {
            if (it) {
                navigate(R.id.reviewImageFragment)
                viewModel.isTemporaryFileSaved.value = false
            }
        }
    }

    override fun askPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION_CAMERA,
            object : RequestPermissionListener {
                /**
                 * Callback on permission previously denied
                 * should show permission rationale and continue permission request
                 */
                override fun onPermissionRationaleShouldBeShown(requestPermission: () -> Unit) {
                    DialogUtils.createDialog(requireContext(),
                        title = "Request permission",
                        message = "Permission Disabled, Please allow permissions to use this feature",
                        onPositionClick = {
                            requestPermission()
                        }
                    ).show()
                }

                /**
                 * Callback on permission "Never show again" checked and denied
                 * should show message and open app setting
                 */
                override fun onPermissionPermanentlyDenied(openAppSetting: () -> Unit) {
                    DialogUtils.createDialog(requireContext(),
                        title = "Request permission",
                        message = "Permission Disabled, Please allow permissions to use this feature",
                        onPositionClick = {
                            openAppSetting()
                        }
                    ).show()
                }

                /**
                 * Callback on permission granted
                 */
                override fun onPermissionGranted() {
                    findNavController().navigateWithAnim(R.id.cameraFragment)
                }
            })
    }

    override fun onClick(item: PreviewChallenge) {
        when {
            item.challenge.imageName.isBlank() -> browseImages()
            item.title.isNotBlank() -> {
                val category = Category.fromValue(item.challenge.type) ?: return
                val bundle = CategoryFragmentArgs.Builder(category).build().toBundle()
                findNavController().navigateWithAnim(R.id.categoryFragment, bundle)
            }
            else -> {
                val bundle = GameFragmentArgs.Builder(item.challenge).build().toBundle()
                findNavController().navigateWithAnim(R.id.gameFragment, bundle)
            }
        }
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: return@registerForActivityResult
                viewModel.saveSelectedImage(uri)
            }
        }

    private fun browseImages() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        selectImageLauncher.launch(intent)
    }

    companion object {
        private const val RC_PERMISSION_CAMERA = 101
    }
}

interface DashboardFragmentListener : OnSimpleItemClick<PreviewChallenge> {
    fun askPermission()
}
