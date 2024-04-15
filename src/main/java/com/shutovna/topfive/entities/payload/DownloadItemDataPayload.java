package com.shutovna.topfive.entities.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class DownloadItemDataPayload {
    private InputStream inputStream;
    private String filename;
    private String type;
}
