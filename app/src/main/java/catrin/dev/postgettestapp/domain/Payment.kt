package catrin.dev.postgettestapp.domain

data class Payment(
    val id: Long,
    val title: String,
    val amount: String?,
    val created: Long?
)
