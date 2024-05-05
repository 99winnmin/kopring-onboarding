package com.korine.koboard.service

import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.PostCreateRequestDto
import com.korine.koboard.service.dto.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional(readOnly = false) // 클래스 단위, 함수 단위 둘 다 있을때는 함수 단위로 트랜잭션 적용이 따라감
    fun createPost(postCreateRequestDto: PostCreateRequestDto): Long {
        return postRepository.save(postCreateRequestDto.toEntity()).id
    }
}
