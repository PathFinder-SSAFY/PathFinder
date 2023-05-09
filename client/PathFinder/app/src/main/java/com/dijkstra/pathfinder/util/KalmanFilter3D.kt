package com.dijkstra.pathfinder.util

class KalmanFilter3D(
    initialState: List<Double>,
    initialCovariance: List<List<Double>>
) {
    private var state = initialState
    private var covariance = initialCovariance
    private val processNoise = listOf(
        listOf(0.1, 0.0, 0.0),
        listOf(0.0, 0.1, 0.0),
        listOf(0.0, 0.0, 0.1)
    )

    fun update(measurement: List<Double>, measurementNoise: List<Double>): List<Double> {
        val kalmanGain = calculateKalmanGain(measurementNoise)
        val innovation = measurement.zip(state) { m, s -> m - s }
        state = state.zip(
            kalmanGain.map { row ->
                row.zip(innovation) { x, y ->
                    x * y
                }.sum()
            }
        ).map { it.first + it.second }
        covariance = updateCovariance(kalmanGain)
        return state
    }

    private fun calculateKalmanGain(measurementNoise: List<Double>): List<List<Double>> {
        val sum = covariance.zip(processNoise) { a, b -> a.zip(b) { x, y -> x + y } }
        val sumDiagonal = sum.indices.map { sum[it][it] }
        val noisePlusSumDiagonal = measurementNoise.zip(sumDiagonal) { x, y -> x + y }
        return covariance.map { row -> row.zip(noisePlusSumDiagonal) { x, y -> x / y } }
    }

    private fun updateCovariance(kalmanGain: List<List<Double>>): List<List<Double>> {
        val gainTimesCovariance =
            kalmanGain.map { row -> row.zip(covariance) { x, col -> x * col.sum() } }
        val identityMinusGain = gainTimesCovariance.indices.map { i ->
            gainTimesCovariance[i].mapIndexed { j, value -> if (i == j) 1.0 - value else -value }
        }
        return identityMinusGain.map { row -> row.zip(covariance) { x, col -> x * col.sum() } }
    }

}
