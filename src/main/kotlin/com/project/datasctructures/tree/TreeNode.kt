package com.project.datastructures.tree

import kotlinx.serialization.Serializable

@Serializable
data class TreeNode<T>(
    val value: T,
    var leftIndex: Int? = null,
    var rightIndex: Int? = null
)
