package com.adjoda.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    PostService postService;

    List<PostDTO> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        posts = List.of(
                new PostDTO("1", "test"),
                new PostDTO("2", "test2")
        );
    }

    @Test
    void shouldPing() throws Exception {
        mockMvc.perform(get("/api/v1/posts/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }

    @Test
    void should_find_all_posts() throws Exception {
        given(postService.findAll()).willReturn(posts);


        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(posts)));
    }

    @Test
    void should_create_new_post_when_given_valid_post() throws Exception {
        var post = posts.get(0);

        given(postService.save(post)).willReturn(post);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_create_new_post_when_given_invalid_post() throws Exception {
        var post =  new PostDTO("1", null);
        given(postService.save(post)).willReturn(post);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_post_when_given_valid_input() throws Exception {
        // Given
        PostDTO postDTO = new PostDTO("1", "test", "imageId1");
        String id ="1";
        when(postService.update(postDTO, id)).thenReturn(postDTO);


        String json = objectMapper.writeValueAsString(postDTO);
        mockMvc.perform(put("/api/v1/posts/"+id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_post_by_id() throws Exception {
        // Given
        String id = "1";
        // When
        mockMvc.perform(get("/api/v1/posts/"+id))
                .andExpect(status().isOk());
    }
    @Test
    void should_find_post_when_given_valid_id() throws Exception {
        // Given
        var post = posts.get(0);
        var id = "1";
        given(postService.findById(id)).willReturn(post);

        mockMvc.perform(get("/api/v1/posts/"+id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(post)));
    }

    @Test
    void should_not_find_post_when_given_invalid_id() throws Exception {
        // Given
        var post = posts.get(0);
        var id = "11111";
        given(postService.findById(id)).willThrow(PostNotFoundException.class);

        mockMvc.perform(get("/api/v1/posts/"+id))
                .andExpect(status().isNotFound());
    }


}
