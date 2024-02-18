package com.adjoda.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostDTO> findAll() {
        log.info("PostServiceImpl:findAll execution started.");

        return postMapper.mapToDTOS(postRepository.findAll());

        /*return postRepository.findAll().stream()
                .map(postMapper::mapToDTO)
                .collect(Collectors.toList());

         */
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        Post post = postMapper.mapToEntity(postDTO);
        Post savedPost = postRepository.save(post);
        return postMapper.mapToDTO(savedPost);
    }

    @Override
    public PostDTO findById(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return postMapper.mapToDTO(post);
    }

    @Override
    public PostDTO update(PostDTO postDTO, String id) {
        Post existingPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        BeanUtils.copyProperties(postDTO, existingPost, id );

        Post savedPost = postRepository.save(existingPost);
        return postMapper.mapToDTO(savedPost);
    }

    @Override
    public void deleteById(String id) {
        postRepository.deleteById(id);
    }
}
