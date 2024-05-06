package com.korine.koboard.service

import com.korine.koboard.exception.PostNotDeletableException
import com.korine.koboard.exception.PostNotFoundException
import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.PostCreateRequestDto
import com.korine.koboard.service.dto.PostUpdateRequestDto
import com.korine.koboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional // 클래스 단위, 함수 단위 둘 다 있을때는 함수 단위로 트랜잭션 적용이 따라감
    fun createPost(postCreateRequestDto: PostCreateRequestDto): Long {
        return postRepository.save(postCreateRequestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, postUpdateRequestDto: PostUpdateRequestDto): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(postUpdateRequestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String) : Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if(post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.deleteById(id)
        return id
    }
}
