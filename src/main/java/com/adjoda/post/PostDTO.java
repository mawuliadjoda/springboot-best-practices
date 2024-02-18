package com.adjoda.post;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public record PostDTO(
        String postId,
        String userId,
        String userTel,
        @NotEmpty
        String caption,
        String imageId,
        String imageId2,
        String imageId3,
        String imageUrl,
        String imageUrl2,
        String imageUrl3,
        String location,
        String tags,
        double latitude,
        double longitude,
        double distanceZero) {
    public PostDTO(String postId, String caption) {
        this(postId, null, null, caption, null, null, null, null, null, null, null, null, 0, 0, 0);
    }

    public PostDTO(String postId, String caption, String imageId) {
        this(postId, null, null, caption, imageId, null, null, null, null, null, null, null, 0, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(postId, postDTO.postId) && Objects.equals(userId, postDTO.userId) && Objects.equals(userTel, postDTO.userTel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId, userTel);
    }
}
