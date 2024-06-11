package id.project.stuntguard.data.model

data class UserModel(
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)