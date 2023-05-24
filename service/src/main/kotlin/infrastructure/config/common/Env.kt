package infrastructure.config.common

enum class Env(val label: String) {
    STUB("stub"),
    DEV("dev"),
    STAGING("staging"),
    BETA("beta"),
    PROD("prod");

    companion object {
        private val cachedValues: Map<String, Env> = values().associateBy{ it.label }

        fun getByLabel(label: String?): Env {
            return label?.let { cachedValues[it] ?: STUB } ?: STUB
        }
    }
}