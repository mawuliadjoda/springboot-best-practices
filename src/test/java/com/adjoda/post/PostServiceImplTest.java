package com.adjoda.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PostServiceImplTest {
    private PostService postService;
    private PostRepository postRepository;
    private PostMapper postMapper;

    List<PostDTO> posts = new ArrayList<>();
    Post post = new Post();
    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        postMapper = mock(PostMapper.class);
        postService = new PostServiceImpl(postRepository, postMapper);

        posts = List.of(
                new PostDTO("1", "test"),
                new PostDTO("2", "test2")
        );

        post = new Post();
        post.setPostId("1");
        post.setCaption("test");
    }

    @Test
    void should_find_all_posts() {
        // Given
        var existingPosts = posts;

        List<Post> existingPostsEntities = postMapper.mapToEntities(existingPosts);

        given(postMapper.mapToDTOS(existingPostsEntities)).willReturn(existingPosts);

        given(postRepository.findAll()).willReturn(existingPostsEntities);

        // When
        List<PostDTO> posts = postService.findAll();

        // Then
        verify(postRepository, times(1)).findAll();
        assertThat(existingPosts).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(posts);
    }

    @Test
    public void should_delete_post_by_id() {
        // Given
        var id = "1";
        // When
        postService.deleteById(id);
        // Then
        verify(postRepository, times(1)).deleteById(id);
    }

    @Test
    void should_create_post() {
        // Given
        var postDTO = posts.get(0);

        given(postMapper.mapToEntity(postDTO)).willReturn(post);
        given(postRepository.save(post)).willReturn(post);
        given(postMapper.mapToDTO(post)).willReturn(postDTO);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        // When
        PostDTO savedPost = postService.save(postDTO);
        verify(postRepository, times(1)).save(postArgumentCaptor.capture());

        // Then
        assertPost(post, postArgumentCaptor.getValue());
        assertPostDTO(postDTO, savedPost);
    }

    @Test
    void should_update_post_when_given_valid_id() {
        // Given
        //var postDTO = posts.get(0);
        PostDTO postDTO = new PostDTO("1", "test", "imageId1");
        String id ="1";

        given(postRepository.findById(id)).willReturn(Optional.of(post));
        given(postRepository.save(any())).willReturn(post);
        given(postMapper.mapToDTO(any())).willReturn(postDTO);

        // When
        PostDTO updatedPostDTO = postService.update(postDTO,id);

        // Then
        assertPostDTO(postDTO, updatedPostDTO);
    }

    @Test
    void should_not_update_post_when_given_invalid_id() {
        // Given
        //var postDTO = posts.get(0);
        PostDTO postDTO = new PostDTO("1", "test", "imageId1");
        String id ="1";

        given(postRepository.findById(id)).willThrow(PostNotFoundException.class);


        // When
        assertThatThrownBy(()-> postService.update(postDTO,id)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void should_found_post_when_given_valid_id() {
        // Given
        String id ="1";
        var postDTO = posts.get(0);

        given(postRepository.findById(id)).willReturn(Optional.of(post));
        given(postMapper.mapToDTO(post)).willReturn(postDTO);

        // When
        PostDTO postById = postService.findById(id);
        assertPostDTO(postDTO, postById);
    }

    @Test
    void should_not_found_post_when_given_invalid_id() {
        // Given
        String id ="111";
        given(postRepository.findById(id)).willThrow(PostNotFoundException.class);

        // When
        assertThatThrownBy(()-> postService.findById(id)).isInstanceOf(PostNotFoundException.class);
    }


    private void assertPostDTO(PostDTO expected, PostDTO actual) {
        assertThat(expected.postId()).isEqualTo(actual.postId());
        assertThat(expected.caption()).isEqualTo(actual.caption());
    }

    private void assertPost(Post expected, Post actual) {
        assertThat(expected.getPostId()).isEqualTo(actual.getPostId());
        assertThat(expected.getCaption()).isEqualTo(actual.getCaption());
    }
}
