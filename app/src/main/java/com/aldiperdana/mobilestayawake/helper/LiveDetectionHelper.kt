package com.aldiperdana.mobilestayawake.helper

import android.graphics.RectF
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage

class LiveDetectionHelper(private val tflite: Interpreter, private val labels: List<String>) {

    data class ObjectPrediction( val score: Float, val location: RectF, val label: String)

    private val locations = arrayOf(Array(OBJECT_COUNT) { FloatArray(4) })
    private val labelIndices =  arrayOf(FloatArray(OBJECT_COUNT))
    private val scores =  arrayOf(FloatArray(OBJECT_COUNT))
    private val outputBuffer = mapOf(
        0 to scores,
        1 to locations,
        2 to FloatArray(1),
        3 to labelIndices
    )

    val predictions get() = (0 until OBJECT_COUNT).map {
        ObjectPrediction(
            score = scores[0][it],
            location = locations[0][it].let {
                RectF(it[1], it[0], it[3], it[2])
            },
            label = labels[labelIndices[0][it].toInt()]
        )
    }

    fun predict(image: TensorImage): List<ObjectPrediction> {
        tflite.runForMultipleInputsOutputs(arrayOf(image.buffer), outputBuffer)
        return predictions
    }

    companion object {
        const val OBJECT_COUNT = 10
    }
}