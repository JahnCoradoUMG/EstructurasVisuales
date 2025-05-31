package com.project.datastructures.linkedlist

import kotlinx.serialization.Serializable

@Serializable
data class Node<T>(
    val value: T,
    val prev: Int?,
    val next: Int?
)
