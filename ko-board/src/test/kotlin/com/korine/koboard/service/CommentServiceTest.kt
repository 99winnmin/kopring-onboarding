package com.korine.koboard.service

import com.korine.koboard.domain.Comment
import com.korine.koboard.domain.Post
import com.korine.koboard.exception.CommentNotDeletableException
import com.korine.koboard.exception.CommentNotUpdatableException
import com.korine.koboard.exception.PostNotFoundException
import com.korine.koboard.repository.CommentRepository
import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.CommentCreateRequestDto
import com.korine.koboard.service.dto.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) : BehaviorSpec({

    given("댓글 생성 시"){
        val post = postRepository.save(Post(title = "title", content = "content", createdBy = "createdBy"))
        When("input 이 정상적으로 들어오면"){
            val commentId = commentService.createComment(
                post.id,
                CommentCreateRequestDto(
                    content = "content",
                    createdBy = "createdBy"
                )
            )
            then("정상 생성됨을 확인한다."){
                commentId shouldBeGreaterThan 0L
                val comment = commentRepository.findByIdOrNull(commentId)
                comment shouldNotBe null
                comment?.content shouldBe "content"
                comment?.createdBy shouldBe "createdBy"
            }
        }

        When("게시글이 존재하지 않으면"){
            then("게시글이 존재하지 않음 예외가 발생한다."){
                shouldThrow<PostNotFoundException> { commentService.createComment(
                    9999L,
                    CommentCreateRequestDto(
                        content = "content",
                        createdBy = "createdBy"
                        )
                    )
                }
            }
        }
    }

    given("댓글 수정 시"){
        val post = postRepository.save(Post(title = "title", content = "content", createdBy = "createdBy"))
        val saved = commentRepository.save(Comment(content = "content", createdBy = "createdBy", post = post))
        When("input 이 정상적으로 들어오면"){
            val updatedId = commentService.updateComment(
                saved.id,
                CommentUpdateRequestDto(
                    content = "updated content",
                    updatedBy = "createdBy"
                )
            )
            then("정상 수정됨을 확인한다."){
                updatedId shouldBe saved.id
                val updated = commentRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.content shouldBe "updated content"
                updated?.updatedBy shouldBe "createdBy"
            }
        }

        When("작성자와 수정자가 다르면"){
            then("수정할 수 없는 댓글 예외가 발생한다."){
                shouldThrow<CommentNotUpdatableException> { commentService.updateComment(
                    saved.id,
                    CommentUpdateRequestDto(
                        content = "updated content",
                        updatedBy = "another createdBy"
                        )
                    )
                }
            }
        }
    }

    given("댓글 삭제 시"){
        val post = postRepository.save(Post(title = "title", content = "content", createdBy = "createdBy"))
        val saved1 = commentRepository.save(Comment(content = "content", createdBy = "createdBy", post = post))
        val saved2 = commentRepository.save(Comment(content = "content", createdBy = "createdBy", post = post))
        When("input 이 정상적으로 들어오면") {
            val commentId = commentService.deleteComment(saved1.id, "createdBy")
            then("정상 삭제됨을 확인한다.") {
                commentId shouldBe saved1.id
                commentRepository.findByIdOrNull(commentId) shouldBe null
            }
        }
        When("작성자와 삭제자가 다르면"){
            then("댓글이 존재하지 않음 예외가 발생한다."){
                shouldThrow<CommentNotDeletableException> {commentService.deleteComment(saved2.id, "another createdBy") }
            }
        }
    }
})
