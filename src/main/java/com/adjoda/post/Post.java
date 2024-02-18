package com.adjoda.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Entity
@Table(name = "post")

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Post {
    @Id
    private String postId;
    private String userId;
    private String userTel;
    @NotEmpty
    private String caption;
    private String imageId;
    private String imageId2;
    private String imageId3;
    private String imageUrl;
    private String imageUrl2;
    private String imageUrl3;
    private String location;
    private String tags;
    private double latitude;
    private double longitude;
    private double distanceZero;
}
