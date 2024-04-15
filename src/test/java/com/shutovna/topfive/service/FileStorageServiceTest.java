package com.shutovna.topfive.service;

import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.entities.payload.DownloadItemDataPayload;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.util.YamlUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {
    private final String fileStoreDir = YamlUtil.getPropertyValue("topfive.file.store.dir");;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    DefaultFileStorageService itemService;

    String testUsername = YamlUtil.getPropertyValue("topfive.test.username");

    @BeforeEach
    public void before() {
        itemService.setFileStoreDir(fileStoreDir);
    }

    @Test
    void getItemDataPayload() throws IOException {
        Integer itemId = 1;
        String filename = "file.txt";
        byte[] data = "This is simple data".getBytes();
        String type = "mediaType";
        ItemData itemData = new ItemData(filename, type);
        Song song = new Song();
        song.setArtist("Metallica");
        song.setTitle("Unforgiven");
        song.setDescription("testDescription");
        song.setData(itemData);
        song.setReleasedAt(null);
        song.setUsername(testUsername);
        File outFile = getOutFile(filename);
        outFile.createNewFile();
        FileCopyUtils.copy(data, new FileOutputStream(outFile));


        when(itemRepository.findById(itemId)).thenReturn(Optional.of(song));

        DownloadItemDataPayload itemDataPayload = itemService.getItemDataPayload(itemId);
        assertArrayEquals(data, FileCopyUtils.copyToByteArray(itemDataPayload.getInputStream()));
        assertEquals(filename, itemDataPayload.getFilename());
        assertEquals(type, itemDataPayload.getType());
    }

    @Test
    void getItemDataStream() throws IOException {
        String filename = "testFile.txt";
        byte[] data = "This is simple data".getBytes();
        File outFile = getOutFile(filename);
        outFile.createNewFile();
        FileCopyUtils.copy(data, getOutFile(filename));

        InputStream inputStream = itemService.getItemDataStream(filename);
        byte[] bytes = inputStream.readAllBytes();

        assertArrayEquals(data, bytes);
    }

    @Test
    void getItemData() throws IOException {
        byte[] data = "This is simple data".getBytes();
        String filename = "testFile.txt";
        FileCopyUtils.copy(data, getOutFile(filename));

        byte[] itemData = itemService.getItemData(filename);

        assertArrayEquals(data, itemData);
    }

    @Test
    void createItemDataFile() throws IOException {
        byte[] data = "This is simple data".getBytes();
        String filename = "testFile.txt";

        itemService.createItemDataFile(filename, data);

        byte[] fileData = FileCopyUtils.copyToByteArray(getOutFile(filename));
        assertArrayEquals(data, fileData);
    }

    private File getOutFile(String filename) {
        return new File(new File(fileStoreDir), filename);
    }

    @Test
    void getItemDataFile() throws IOException {
        String filename = "testFile.txt";
        File outFile = getOutFile(filename);
        outFile.createNewFile();

        File itemDataFile = itemService.getItemDataFile(filename);

        assertEquals(outFile, itemDataFile);
    }
}