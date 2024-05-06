package com.korine.koboard.service

import com.korine.koboard.domain.Post
import com.korine.koboard.exception.PostNotDeletableException
import com.korine.koboard.exception.PostNotFoundException
import com.korine.koboard.exception.PostNotUpdatableException
import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.PostCreateRequestDto
import com.korine.koboard.service.dto.PostSearchRequestDto
import com.korine.koboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content", createdBy = "ryu"),
                Post(title = "title1", content = "content", createdBy = "ryu"),
                Post(title = "title1", content = "content", createdBy = "ryu"),
                Post(title = "title12", content = "content", createdBy = "ryu"),
                Post(title = "title13", content = "content", createdBy = "ryu"),
                Post(title = "title14", content = "content", createdBy = "ryu"),
                Post(title = "title15", content = "content", createdBy = "ryu"),
                Post(title = "title6", content = "content", createdBy = "ryu"),
                Post(title = "title7", content = "content", createdBy = "ryu"),
                Post(title = "title8", content = "content", createdBy = "ryu"),
                Post(title = "title9", content = "content", createdBy = "ryu"),
                Post(title = "title10", content = "content", createdBy = "ryu"),
            )
        )
    }

    given("게시글 생성 시"){
        `when`("게시글 input 이 정상적으로 들어오면"){
            val postId = postService.createPost(PostCreateRequestDto(
                title = "title",
                content = "content",
                createdBy = "createdBy"
            ))
            then("게시글이 정상적으로 생성됨을 확인한다."){
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "title"
                post?.content shouldBe "content"
                post?.createdBy shouldBe "createdBy"
            }
        }
    }

    given("게시글 수정 시"){
        val saved = postRepository.save(Post(
            title = "title",
            content = "content",
            createdBy = "createdBy"
        ))
        When("정상 수정 시"){
            val updatedId = postService.updatePost(saved.id, PostUpdateRequestDto(
                title = "updatedTitle",
                content = "updatedContent",
                updatedBy = "createdBy"
            )
            )
            then("게시글이 정상적으로 수정됨을 확인한다."){
                saved.id shouldBe updatedId
                val updatedPost = postRepository.findByIdOrNull(updatedId)
                updatedPost shouldNotBe null
                updatedPost?.title shouldBe "updatedTitle"
                updatedPost?.content shouldBe "updatedContent"
                updatedPost?.updatedBy shouldBe "createdBy"
            }
        }

        When("수정할 게시글이 없을 때"){
            then("게시글이 수정되지 않음을 확인한다."){
                shouldThrow<PostNotFoundException> { postService.updatePost(9999L, PostUpdateRequestDto(
                    title = "updatedTitle",
                    content = "updatedContent",
                    updatedBy = "updatedCreatedBy"
                )
                )}
            }
        }

        When("작성자가 동일하지 않으면"){
            then("게시글이 수정되지 않음을 확인한다."){
                shouldThrow<PostNotUpdatableException> {postService.updatePost(1L, PostUpdateRequestDto(
                    title = "updatedTitle",
                    content = "updatedContent",
                    updatedBy = "updatedCreatedBy"
                )
                ) }
            }
        }
    }

    given("게시글 삭제 시"){
        val saved1 = postRepository.save(Post(
            title = "title",
            content = "content",
            createdBy = "createdBy"
        ))

        val saved2 = postRepository.save(Post(
            title = "title",
            content = "content",
            createdBy = "createdBy"
        ))

        When("정상 삭제 시") {
            val postId = postService.deletePost(saved1.id, "createdBy")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved1.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }

        When("작성자가 동일하지 않으면"){
            then("게시글이 삭제되지 않는다는 예외가 발생한다."){
                shouldThrow<PostNotDeletableException> { postService.deletePost(saved2.id, "updatedBy") }
            }
        }
    }

    given("게시글 상세조회 시"){
        val saved = postRepository.save(Post(
            title = "title",
            content = "content",
            createdBy = "createdBy"
        ))
        When("정상 조회 시") {
            val post = postService.getPost(saved.id)
            then("게시글이 정상적으로 조회됨을 확인한다.") {
                post shouldNotBe null
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "createdBy"
            }
        }

        When("조회할 게시글이 없을 때"){
            then("게시글 없다는 예외가 발생한다."){
                shouldThrow<PostNotFoundException> { postService.getPost(9999L) }
            }
        }
    }

    given("게시글 목록 조회 시"){
        When("정상 조회 시"){
            val postPage = postService.getPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
            then("게시글 목록이 정상적으로 조회됨을 확인한다."){
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain  "title1"
                postPage.content[0].createdBy shouldContain "ryu"
            }
        }

        When("타이틀로 검색"){
            val postPage = postService.getPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
            then("타이틀에 해당하는 게시글이 조회된다."){
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain  "title1"
                postPage.content[0].createdBy shouldContain "ryu"
            }
        }

        When("작성자로 검색"){
            val postPage = postService.getPageBy(PageRequest.of(0, 5), PostSearchRequestDto(createdBy = "ryu"))
            then("타이틀에 해당하는 게시글이 조회된다."){
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain  "title1"
                postPage.content[0].createdBy shouldContain "ryu"
            }
        }
    }
})
