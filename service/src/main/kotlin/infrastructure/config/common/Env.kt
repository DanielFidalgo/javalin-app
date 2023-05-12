package infrastructure.config.common

enum class Env(val label: String) {
    STUB("stub"),
    DEV("dev"),
    STAGING("staging"),
    BETA("beta"),
    PROD("prod");

    companion object {
        private val cachedValues: Map<String, Env> = values().map { it }.associateBy({it.label}, {it})

        fun getByLabel(label: String?): Env {
            return label?.let { cachedValues[label] ?: STUB } ?: STUB
        }
    }
}