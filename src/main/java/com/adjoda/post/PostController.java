package com.adjoda.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @GetMapping("/ping")
    public String ping() {
        return "Hello world";
    }

    @GetMapping
    public List<PostDTO> findAll() {
        return postService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    PostDTO create(@RequestBody @Validated PostDTO postDTO) {
        return postService.save(postDTO);
    }

    @GetMapping("/{id}")
    PostDTO findByID(@PathVariable String id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    PostDTO update(@PathVariable String id, @RequestBody @Validated PostDTO postDTO) {
        return postService.update(postDTO, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {
       postService.deleteById(id);
    }
}
