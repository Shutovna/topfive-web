package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
public class NewVideoPayload {
    @NotBlank(message = "{ru.shutovna.msg.video.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.shutovna.msg.video.title.size}")
    String title;
    String description;

    String place;

    String director;

    String actors;

    Integer releasedYear;

    @NotNull(message = "{ru.shutovna.msg.video.genre.not_null}")
    Integer genreId;

    Integer topId;
    MultipartFile file;

    public NewVideoPayload(String title, String description, Integer topId, MultipartFile file,
                           String place, String director, String actors, Integer releasedYear, Integer genreId) {
        this.title = title;
        this.description = description;
        this.topId = topId;
        this.file = file;
        this.place = place;
        this.director = director;
        this.actors = actors;
        this.releasedYear = releasedYear;
        this.genreId = genreId;
    }

    @AssertTrue(message = "{ru.shutovna.msg.video.file.not_null}")
    public boolean isFileSet() {
        try {
            return file != null &&
                    !(StringUtils.isEmpty(file.getOriginalFilename()) || ArrayUtils.isEmpty(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AssertTrue(message = "{ru.shutovna.msg.video.type.is_audio}")
    public boolean isVideoFile() {
        if (file == null) {
            return false;
        }
        String contentType = file.getContentType();
        return !StringUtils.isEmpty(contentType) && contentType.startsWith("video");
    }
}