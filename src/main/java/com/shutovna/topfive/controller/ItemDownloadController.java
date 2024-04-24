package com.shutovna.topfive.controller;

import com.shutovna.topfive.entities.payload.DownloadItemDataPayload;
import com.shutovna.topfive.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemDownloadController {
    private final FileStorageService fileStorageService;

    @GetMapping("/items/{itemId}/file")
    @ResponseBody
    public ResponseEntity<Resource> downloadItem(@PathVariable Integer itemId) {
        log.info("Downloading file for item " + itemId);
        DownloadItemDataPayload itemDataPayload = fileStorageService.getItemDataPayload(itemId);
        Resource rs = new InputStreamResource(itemDataPayload.getInputStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + itemDataPayload.getFilename() + "\"").body(rs);
    }
}
