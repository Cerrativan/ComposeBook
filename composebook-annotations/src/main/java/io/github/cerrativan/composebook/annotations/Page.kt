package io.github.cerrativan.composebook.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Page(
    val name: String,
    val group: String = "Default"
)