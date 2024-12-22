package org.lanzadera.proyectos

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CameraView() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Camera provider setup
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

//    //val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
//
//    // Request camera permission before setting up the camera
//    LaunchedEffect(permissionState.hasPermission) {
//        if (!permissionState.hasPermission) {
//            permissionState.launchPermissionRequest()
//        }
//    }


        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                // Bind camera to lifecycle
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }

        )


}