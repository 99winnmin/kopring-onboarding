package com.fastcampus.kotlinspring.todo.api.model

import com.fastcampus.kotlinspring.todo.domain.Todo
import com.fasterxml.jackson.annotation.JsonIgnore

data class TodoListResponse(
        val items: List<TodoResponse>,
) {
    val size : Int
        @JsonIgnore // JSON 으로 변환할 때 제외
        get() = items.size

    fun get(index: Int) = items[index]

    companion object {
        fun of(todoList: List<Todo>) =
                TodoListResponse(todoList.map(TodoResponse::of))
    }
}
