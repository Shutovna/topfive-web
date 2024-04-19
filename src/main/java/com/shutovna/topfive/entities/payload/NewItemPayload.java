package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
public abstract class NewItemPayload extends ItemPayload {
    Integer topId;
    MultipartFile file;

    public NewItemPayload(String title, String description, Integer topId, MultipartFile file) {
        super(title, description);
        this.topId = topId;
        this.file = file;
    }

    @AssertTrue(message = "{ru.nikitos.msg.song.file.not_null}")
    public boolean isFileSet() {
        try {
            return file != null &&
                    !(StringUtils.isEmpty(file.getOriginalFilename()) || ArrayUtils.isEmpty(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AssertTrue(message = "{ru.nikitos.msg.song.type.is_audio}")
    public boolean isAudioFile() {
        if (file == null) {
            return false;
        }
        String contentType = file.getContentType();
        return !StringUtils.isEmpty(contentType) && contentType.startsWith("audio");
    }
}
