package com.shutovna.topfive.service;

import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.entities.payload.DownloadItemDataPayload;
import com.shutovna.topfive.entities.ItemData;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class DefaultFileStorageService implements FileStorageService {

    private final ItemRepository itemRepository;

    private String fileStoreDir;

    public DefaultFileStorageService(ItemRepository itemRepository, @Value("${topfive.file.store.dir}") String fileStoreDir) {
        this.itemRepository = itemRepository;
        this.fileStoreDir = fileStoreDir;
    }


    @Override
    public DownloadItemDataPayload getItemDataPayload(Integer itemId) {
        log.debug("getItemData " + itemId);
        return itemRepository.findById(itemId).map(item -> {
                    ItemData itemData = item.getData();
                    return new DownloadItemDataPayload(
                            getItemDataStream(itemData.getFilename()),
                            itemData.getFilename(),
                            itemData.getContentType());

                })
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public InputStream getItemDataStream(String filename) {
        File dir = new File(fileStoreDir);
        if (!dir.exists()) {
            throw new IllegalStateException("Directory %s does not exist".formatted(dir));
        }
        File file = new File(dir, filename);
        try {
            log.debug("Getting item data from {}", file);
            return new FileInputStream(file);

        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File %s not found".formatted(file));
        }
    }

    @Override
    public byte[] getItemData(String filename) {
        File dir = new File(fileStoreDir);
        if (!dir.exists()) {
            throw new IllegalStateException("Directory %s does not exist".formatted(dir));
        }
        File file = new File(dir, filename);
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File %s not found".formatted(file));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read %s".formatted(file));
        }
    }

    @Override
    public File createItemDataFile(String filename, byte[] data) {
        if (StringUtils.isEmpty(filename)) {
            throw new NullPointerException("filename is null");
        }
        File dir = new File(fileStoreDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Cannot create directory " + dir);
            }
            log.debug("Dir {} created", dir);
        }
        File file = new File(dir, filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            log.debug("File {} created", file);
            return file;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File getItemDataFile(String filename) {
        File dir = new File(fileStoreDir);
        if (!dir.exists()) {
            throw new IllegalStateException("Directory %s does not exist".formatted(dir));
        }
        return new File(dir, filename);
    }

    public void setFileStoreDir(String fileStoreDir) {
        this.fileStoreDir = fileStoreDir;
    }
}
