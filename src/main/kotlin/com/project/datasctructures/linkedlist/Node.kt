package com.project.datastructures.linkedlist

import kotlinx.serialization.Serializable

@Serializable
data class Node<T>(
    val value: T,
    var prev: Int? = null,
    var next: Int? = null
)
