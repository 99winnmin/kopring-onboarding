package com.fastcampus.kotlinspring.todo.api.model

class TodoRequest(
        val title : String,
        val description : String,
        val done: Boolean = false,
) {
}
