package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.payload.DownloadItemDataPayload;

import java.io.File;
import java.io.InputStream;

public interface FileStorageService {
    DownloadItemDataPayload getItemDataPayload(Integer itemId);

    InputStream getItemDataStream(String filename);

    byte[] getItemData(String filename);

    File createItemDataFile(String filename, byte[] data);

    File getItemDataFile(String filename);
}
