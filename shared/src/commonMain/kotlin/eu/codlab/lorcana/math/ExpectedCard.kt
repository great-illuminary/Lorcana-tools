package eu.codlab.lorcana.math

class ExpectedCard internal constructor(
    val id: String,
    var name: String,
    originalAmount: Long,
    originalMin: Long,
    originalMax: Long,
    private val onValueUpdated: (amount: Long, min: Long, max: Long) -> Unit
) {
    var amount: Long = originalAmount
        private set(value) {
            println("setting $name -> amount/$value")
            val changed = value != field
            field = value
            if (changed) onValueUpdated(value, min, max)
        }
    var min: Long = originalMin
        private set(value) {
            println("setting $name -> min/$value")
            val changed = value != field
            field = value
            if (changed) onValueUpdated(amount, value, max)
        }
    var max: Long = originalMax
        private set(value) {
            println("setting $name -> max/$value")
            val changed = value != field
            field = value
            if (changed) onValueUpdated(amount, min, value)
        }

    internal fun update(amount: Long, min: Long, max: Long) {
        println("updating $name -> $amount/$min/$max")
        this.amount = amount
        this.min = min
        this.max = max
    }

    override fun toString(): String {
        return "{amt: $amount, min: $min, max: $max}"
    }
}
