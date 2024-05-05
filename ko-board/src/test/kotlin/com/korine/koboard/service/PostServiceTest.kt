package com.korine.koboard.service

import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.PostCreateRequestDto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("게시글 생성 시"){
        `when`("게시글 생성"){
            val postId = postService.createPost(PostCreateRequestDto(
                title = "title",
                content = "content",
                createdBy = "createdBy"
            ))
            then("게시글이 정상적으로 생성됨을 확인한다."){
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
            }
        }
    }
})
