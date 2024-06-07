package id.project.stuntguard.data.model

import id.project.stuntguard.data.preferences.UserModel

data class MissionModel(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val assignedToUser: UserModel,
    val assignedToChild: ChildModel,
)