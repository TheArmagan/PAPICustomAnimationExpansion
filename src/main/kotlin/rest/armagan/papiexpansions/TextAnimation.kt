package rest.armagan.papiexpansions

data class TextAnimationFrame(
    val text: String,
    val duration: Int? = null
)

data class TextAnimation(
    val defaultDuration: Int,
    val frames: List<TextAnimationFrame>,
    var lastChangedAt: Long = System.currentTimeMillis(),
    var lastFrameIndex: Int = 0
) {
    fun getFrame(): TextAnimationFrame {
        val frame = frames[lastFrameIndex]
        val duration = frame.duration ?: defaultDuration;
        val now = System.currentTimeMillis()
        if (now - lastChangedAt >= duration) {
            lastChangedAt = now
            lastFrameIndex = (lastFrameIndex + 1) % frames.size
        }
        return frames[lastFrameIndex]
    }
}
