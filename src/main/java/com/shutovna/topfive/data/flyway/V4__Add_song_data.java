package com.shutovna.topfive.data.flyway;

import com.shutovna.topfive.entities.Genre;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.service.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
@Profile("dev")
public class V4__Add_song_data extends BaseJavaMigration {
    private static final String filename = "example_song.mp3";

    private static final int TOP_ID = 2;

    @Value("classpath:" + filename)
    private Resource exampleFile;

    @Value("${topfive.file.store.dir}")
    private String fileStoreDir;

    private static final List<Song> data;

    static {
        LocalDate now = LocalDate.now();
        Random random = new Random();
        ItemData itemData = new ItemData(filename, "audio/mpeg");
        data = List.of(
                new Song(20, "Chop Suey!", null, itemData, new User(1),
                        "System Of A Down", now.minusYears(random.nextInt(20)), 320, new Genre(1)),
                new Song(21, "Duality", null, itemData, new User(1),
                        "Slipknot", now.minusYears(random.nextInt(20)), 192, new Genre(1)),
                new Song(22, "Change(In the House of Flies)", null, itemData, new User(1),
                        "Deftones", now.minusYears(random.nextInt(20)), 320, new Genre(1)),
                new Song(23, "Square Hammer", null, itemData, new User(1),
                        "Ghost", now.minusYears(random.nextInt(20)), 192, new Genre(1)),
                new Song(24, "Pull Harder on the Strings of Your Martyr", null, itemData, new User(1),
                        "Trivium", now.minusYears(random.nextInt(20)), 128, new Genre(1)));
    }


    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        PreparedStatement itemSt = null;
        PreparedStatement songSt = null;
        PreparedStatement topItemsSt = null;
        byte[] fileContent = exampleFile.getContentAsByteArray();

        try {
            itemSt = connection.prepareStatement(
                    "INSERT INTO topfive.item(id, user_id, content_type, description, filename, title)\n" +
                            "        VALUES (?, ?, ?, ?, ?, ?)");

            songSt = songSt = connection.prepareStatement(
                    "INSERT INTO topfive.song(id, artist, released_at, bit_rate, genre_id)" +
                            "VALUES(?, ?, ?, ?, ?)");

            topItemsSt = connection.prepareStatement(
                    "insert into topfive.top_items(top_id, item_id) values(?, ?)");

            for (Song song : data) {
                itemSt.setInt(1, song.getId());
                itemSt.setInt(2, song.getUser().getId());
                itemSt.setString(3, song.getData().getContentType());
                itemSt.setString(4, song.getDescription());
                itemSt.setString(5, song.getData().getFilename());
                itemSt.setString(6, song.getTitle());
                itemSt.execute();

                songSt.setInt(1, song.getId());
                songSt.setString(2, song.getArtist());
                songSt.setDate(3, Date.valueOf(song.getReleasedAt()));
                songSt.setInt(4, song.getBitRate());
                songSt.setInt(5, song.getGenre().getId());
                songSt.execute();

                topItemsSt.setInt(1, TOP_ID);
                topItemsSt.setInt(2, song.getId());
                topItemsSt.execute();
            }
        } finally {
            itemSt.close();
            songSt.close();
            topItemsSt.close();
        }

        File dir = new File(fileStoreDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File example = new File(fileStoreDir, filename);
        IOUtils.copy(new ByteArrayInputStream(fileContent), new FileOutputStream(example));
    }
}
