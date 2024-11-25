package com.ssafy.urs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Completed_Route")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedRoute {
    @EmbeddedId
    private CompletedRouteId id;

    @Column(name = "starting_img_url")
    private String startingImgUrl;

    @Column(name = "middle_img_url")
    private String middleImgUrl;

    @Column(name = "ending_img_url")
    private String endingImgUrl;
}
