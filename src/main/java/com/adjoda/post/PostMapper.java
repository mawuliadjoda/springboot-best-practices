package com.adjoda.post;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    Post mapToEntity(PostDTO postDTO);
    PostDTO mapToDTO(Post post);

    List<Post> mapToEntities(List<PostDTO> postDTOS);

    List<PostDTO> mapToDTOS(List<Post> posts);
}
