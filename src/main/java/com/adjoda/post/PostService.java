package com.adjoda.post;

import java.util.List;

public interface PostService {
    List<PostDTO> findAll();

    PostDTO save(PostDTO postDTO);

    PostDTO findById(String id);

    PostDTO update(PostDTO postDTO, String id);

    void deleteById(String id);
}
