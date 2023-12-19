package com.aldiperdana.mobilestayawake.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.aldiperdana.mobilestayawake.R
import com.aldiperdana.mobilestayawake.databinding.ActivityLiveBinding
import com.aldiperdana.mobilestayawake.helper.LiveDetectionHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.nnapi.NnApiDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.random.Random

class LiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveBinding
    private lateinit var bitmapBuffer: Bitmap

    private val executor = Executors.newSingleThreadExecutor()
    private val permissions = listOf(Manifest.permission.CAMERA)
    private val permissionsRequestCode = Random.nextInt(0, 10000)
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private val isFrontFacing get() = lensFacing == CameraSelector.LENS_FACING_BACK
    private var pauseAnalysis = true
    private var imageRotationDegrees: Int = 0
    private val tfImageBuffer = TensorImage(DataType.UINT8)

    private var lastDrowsyDetectionTime = 0L
    private val drowsyDetectionDuration = 3000
    private var mediaPlayer: MediaPlayer? = null

    private var isTimerRunning = false
    private var elapsedTimeMillis = 0L
    private lateinit var timerRunnable: Runnable
    private val handler = Handler()

    private val tfImageProcessor by lazy {
        val cropSize = minOf(bitmapBuffer.width, bitmapBuffer.height)
        ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(
                tfInputSize.height, tfInputSize.width, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(Rot90Op(-imageRotationDegrees / 90))
            .add(NormalizeOp(0f, 1f))
            .build()
    }

    private val nnApiDelegate by lazy  {
        NnApiDelegate()
    }

    private val tflite by lazy {
        Interpreter(
            FileUtil.loadMappedFile(this, MODEL_PATH),
            Interpreter.Options().addDelegate(nnApiDelegate))
    }

    private val detector by lazy {
        LiveDetectionHelper(
            tflite,
            FileUtil.loadLabels(this, LABELS_PATH)
        )
    }

    private val tfInputSize by lazy {
        val inputIndex = 0
        val inputShape = tflite.getInputTensor(inputIndex).shape()
        Size(inputShape[2], inputShape[1]) // Order of axis is: {1, height, width, 3}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRotate.setOnClickListener {
            Toast.makeText(this, "Not yet usable", Toast.LENGTH_SHORT).show()
        }

        binding.btnSetting.setOnClickListener {
            Toast.makeText(this, "Not yet usable", Toast.LENGTH_SHORT).show()
        }

        binding.btnStart.setOnClickListener {
            binding.live.visibility = View.VISIBLE
            binding.unlive.visibility = View.GONE
            pauseAnalysis = false
            startTimer()
        }

        binding.btnPause.setOnClickListener {
            pauseAnalysis = !pauseAnalysis

            if (pauseAnalysis) {
                stopTimer()
                binding.btnPause.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_play, 0, 0, 0
                )
            } else {
                startTimer()
                binding.btnPause.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pause, 0, 0, 0
                )
            }
        }

        binding.btnStop.setOnClickListener {}
    }

    private fun startTimer() {
        timerRunnable = object : Runnable {
            override fun run() {
                updateTimer()
                handler.postDelayed(this, 1000)
            }
        }

        binding.time.setBackgroundResource(R.drawable.background_timer)
        handler.postDelayed(timerRunnable, 0)
        isTimerRunning = true
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        isTimerRunning = false
        binding.time.setBackgroundResource(R.drawable.background_timer_off)
    }

    private fun resetTimer() {
        elapsedTimeMillis = -2000
        updateTimer()
    }

    private fun updateTimer() {
        elapsedTimeMillis += 1000
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.time.text = formattedTime
    }

    override fun onDestroy() {
        executor.apply {
            shutdown()
            awaitTermination(1000, TimeUnit.MILLISECONDS)
        }

        tflite.close()
        nnApiDelegate.close()

        super.onDestroy()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindCameraUseCases() = binding.viewFinder.post {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener ({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()

            var frameCounter = 0
            var lastFpsTimestamp = System.currentTimeMillis()

            imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer { image ->
                if (!::bitmapBuffer.isInitialized) {
                    imageRotationDegrees = image.imageInfo.rotationDegrees
                    bitmapBuffer = Bitmap.createBitmap(
                        image.width, image.height, Bitmap.Config.ARGB_8888)
                }

                if (pauseAnalysis) {
                    image.close()
                    return@Analyzer
                }

                image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)  }
                val tfImage =  tfImageProcessor.process(tfImageBuffer.apply { load(bitmapBuffer) })
                val predictions = detector.predict(tfImage)

                reportPrediction(predictions.maxByOrNull { it.score })
                val frameCount = 10

                if (++frameCounter % frameCount == 0) {
                    frameCounter = 0
                    val now = System.currentTimeMillis()
                    val delta = now - lastFpsTimestamp
                    val fps = 1000 * frameCount.toFloat() / delta
                    Log.d(TAG, "FPS: ${"%.02f".format(fps)} with tensorSize: ${tfImage.width} x ${tfImage.height}")
                    lastFpsTimestamp = now
                }
            })

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this as LifecycleOwner, cameraSelector, preview, imageAnalysis)

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun reportPrediction(
        prediction: LiveDetectionHelper.ObjectPrediction?
    ) = binding.viewFinder.post {
        if (prediction == null || prediction.score < ACCURACY_THRESHOLD || pauseAnalysis) {
            binding.boxPrediction.visibility = View.GONE
            binding.textPrediction.visibility = View.GONE
            resetDrowsyAlert()
            return@post
        }

        val location = mapOutputCoordinates(prediction.location)

        binding.textPrediction.text = "${prediction.label}: ${"%.2f".format(prediction.score)}"
        (binding.boxPrediction.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = location.top.toInt()
            leftMargin = location.left.toInt()
            width = min(binding.viewFinder.width, location.right.toInt() - location.left.toInt())
            height = min(binding.viewFinder.height, location.bottom.toInt() - location.top.toInt())
        }

        binding.boxPrediction.visibility = View.VISIBLE
        binding.textPrediction.visibility = View.VISIBLE

        if (prediction.label.equals("drowsy", ignoreCase = true)) {
            val currentTime = System.currentTimeMillis()

            if (lastDrowsyDetectionTime == 0L) {
                lastDrowsyDetectionTime = currentTime
            }

            if (currentTime - lastDrowsyDetectionTime >= drowsyDetectionDuration) {
                showDrowsyBottomSheet()
                resetDrowsyAlert()
            }
        } else {
            resetDrowsyAlert()
        }

    }

    private fun showDrowsyBottomSheet() {
        vibratePhone()
        playAudio()
        pauseAnalysis = true // Berhenti menganalisis saat bottom sheet ditampilkan

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.item_bottom_sheet_drowsiness_warning, null)

        val btnPause = view.findViewById<Button>(R.id.btn_pause)
        val btnContinue = view.findViewById<Button>(R.id.btn_continue)

        btnPause.setOnClickListener {
            pauseAnalysis = true // Lanjutkan analisis setelah bottom sheet ditutup
            stopTimer()
            bottomSheetDialog.dismiss()

            binding.btnPause.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_play, 0, 0, 0
            )
        }

        btnContinue.setOnClickListener {
            pauseAnalysis = false // Lanjutkan analisis setelah bottom sheet ditutup
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setOnDismissListener {
            stopAudio()
            stopVibration()
            resetDrowsyAlert()
        }
        bottomSheetDialog.show()
    }

    private fun playAudio() {
        mediaPlayer = MediaPlayer.create(this, R.raw.drowsiness_detected)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(500, 1000, 500, 1000),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                1
            )
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(3000)
        }
    }

    private fun stopVibration() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
    }

    private fun resetDrowsyAlert() {
        lastDrowsyDetectionTime = 0L
    }

    private fun mapOutputCoordinates(location: RectF): RectF {
        val previewLocation = RectF(
            location.left * binding.viewFinder.width,
            location.top * binding.viewFinder.height,
            location.right * binding.viewFinder.width,
            location.bottom * binding.viewFinder.height
        )

        val isFrontFacing = lensFacing == CameraSelector.LENS_FACING_FRONT
        val correctedLocation = if (isFrontFacing) {
            RectF(
                binding.viewFinder.width - previewLocation.right,
                previewLocation.top,
                binding.viewFinder.width - previewLocation.left,
                previewLocation.bottom)
        } else {
            previewLocation
        }

        val margin = 0.1f
        val requestedRatio = 4f / 3f
        val midX = (correctedLocation.left + correctedLocation.right) / 2f
        val midY = (correctedLocation.top + correctedLocation.bottom) / 2f
        return if (binding.viewFinder.width < binding.viewFinder.height) {
            RectF(
                midX - (1f + margin) * requestedRatio * correctedLocation.width() / 2f,
                midY - (1f - margin) * correctedLocation.height() / 2f,
                midX + (1f + margin) * requestedRatio * correctedLocation.width() / 2f,
                midY + (1f - margin) * correctedLocation.height() / 2f
            )
        } else {
            RectF(
                midX - (1f - margin) * correctedLocation.width() / 2f,
                midY - (1f + margin) * requestedRatio * correctedLocation.height() / 2f,
                midX + (1f - margin) * correctedLocation.width() / 2f,
                midY + (1f + margin) * requestedRatio * correctedLocation.height() / 2f
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (!hasCameraPermission() || !hasMicrophonePermission()) {
            requestPermissions()
        } else {
            resetTimer()
            bindCameraUseCases()
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasMicrophonePermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        val missingPermissions = mutableListOf<String>()

        if (!hasCameraPermission()) {
            missingPermissions.add(Manifest.permission.CAMERA)
        }

        if (!hasMicrophonePermission()) {
            missingPermissions.add(Manifest.permission.RECORD_AUDIO)
        }

        ActivityCompat.requestPermissions(
            this,
            missingPermissions.toTypedArray(),
            permissionsRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionsRequestCode) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                bindCameraUseCases()
            } else {
                // Handle the case where the user did not grant all required permissions
                Toast.makeText(
                    this,
                    "Permissions not granted. Exiting...",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private val TAG = LiveActivity::class.java.simpleName
        private const val ACCURACY_THRESHOLD = 0.7f
        private const val MODEL_PATH = "drowsiness.tflite"
        private const val LABELS_PATH = "drowsiness.txt"
    }
}