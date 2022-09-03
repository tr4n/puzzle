package com.tr4n.puzzle.ui.camera

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseFragment
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.databinding.FragmentCameraBinding
import com.tr4n.puzzle.util.FileUtils
import com.tr4n.puzzle.util.allPermissionsGranted
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : BaseFragment<FragmentCameraBinding, CameraViewModel>(),
    CameraFragmentListener {

    private var imageCapture: ImageCapture? = null

    override val layoutResId: Int
        get() = R.layout.fragment_camera

    override val viewModel: CameraViewModel by viewModel()

    override fun setBindingVariables() {
        super.setBindingVariables()
        viewBD.viewModel = viewModel
        viewBD.listener = this
    }

    override fun setUpView() {
        setTextColorStatusBar(false)
        startCamera()
    }

    override fun onBack() {
        findNavController().popBackStack()
    }

    private fun startCamera() {
        val context = context
        if (context?.allPermissionsGranted(REQUIRED_PERMISSIONS) != true) {
            return
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBD.previewImage.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    override fun takePhoto() {
        val context = context ?: return
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return


        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(FileUtils.temporaryImageFile)
                .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    viewModel.messageToast.value = exc.message
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    navigate(R.id.action_cameraFragment_to_reviewImageFragment)
                }
            }
        )
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

interface CameraFragmentListener : DataBindingListener {

    fun onBack()

    fun takePhoto()
}
