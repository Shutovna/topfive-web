package com.shutovna.topfive.controller;

import com.shutovna.topfive.service.DefaultFileStorageService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class FilesRestControllerIT {
    private static final String filename = "example_song.mp3";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DefaultFileStorageService fileStorageService;

    @Value("${topfive.test.username}")
    private String testUsername;


    @Value("classpath:" + filename)
    private Resource exampleFile;

    private File file;

    @BeforeEach
    public void createFile() throws IOException {
        file = fileStorageService.createItemDataFile(filename, exampleFile.getContentAsByteArray());
    }

    @AfterEach
    public void deleteFile() {
        file.delete();
    }


    @Test
    @Sql("/db/songs.sql")
    public void downloadFile_ReturnsStream() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/files/" + filename)
                .with(user(testUsername).roles("USER"));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith("audio/mpeg"),
                        content().bytes(exampleFile.getContentAsByteArray())
                );
    }
}
