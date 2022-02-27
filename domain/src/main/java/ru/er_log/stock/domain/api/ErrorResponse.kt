package ru.er_log.stock.domain.api

data class ErrorResponse(
    val message: String? = null,
    val messages: List<String>? = null,
    val error: String? = null,
    val errors: List<String>? = null,
    val description: String? = null
) {
    /** Преобразует сырые сообщения об ошибках в единую строку. */
    val parsed: String? by lazy {
        val list = mutableListOf<String>()

        message?.let { list.add(it) }
        messages?.let { it.forEach { mes -> list.add(mes) } }
        error?.let { list.add(it) }
        errors?.let { it.forEach { mes -> list.add(mes) } }
        description?.let { list.add(it) }

        return@lazy list.joinToString("\n").let { it.ifBlank { null } }
    }
}