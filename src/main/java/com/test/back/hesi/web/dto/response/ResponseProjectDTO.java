package com.test.back.hesi.web.dto.response;

import com.test.back.hesi.web.model.entity.Project;
import lombok.*;

import java.time.format.DateTimeFormatter;

//@Schema(description = "프로젝트")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseProjectDTO {
    //@Schema
    Long id;
    //@Schema
    String name;
    //@Schema
    String contents;
    //@Schema
    String createdAt;
    //@Schema
    String auth;

    @Builder
    public ResponseProjectDTO(Project project){
        this.id = project.getId();
        this.name = project.getName();
        this.contents = project.getContents();
        this.createdAt = project.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.auth = project.getAuth();
    }
}