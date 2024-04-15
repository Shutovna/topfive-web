package com.shutovna.topfive.data;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VideoTest {
    /*@PersistenceContext
    EntityManager entityManager;

    @Autowired
    VideoItemRepository videoItemRepository;

    @Autowired
    GenreRepository genreRepository;

    @Value("classpath:example_file.xml")
    private Resource exampleFile;

    private final String fileName = "example_file.xml";

    @Test
    public void testFindNone() {
        assertTrue(videoItemRepository.findAll().isEmpty());
    }

    @Test
    public void testCreate() throws IOException, SQLException {
        Genre genre = genreRepository.findById(1).orElseThrow();
        byte[] contentAsByteArray = exampleFile.getContentAsByteArray();

        Video video = getVideo(genre, contentAsByteArray);
        videoItemRepository.saveAndFlush(video);

        List<Video> videos = videoItemRepository.findAll();
        assertEquals(1, videos.size());

        Video videoDB = videos.get(0);
        assertEquals("Gladiator", videoDB.getTitle());
        assertEquals("best film", videoDB.getDescription());
        assertEquals("Crow", videoDB.getActors());
        assertEquals("Scott", videoDB.getDirector());
        assertEquals(2000, videoDB.getReleasedYear());
        assertEquals(genre, videoDB.getGenre());
        assertArrayEquals(contentAsByteArray, videoDB.getItemData().getBlob().getBinaryStream().readAllBytes());
    }

    @Test
    public void testUpdate() throws IOException, SQLException {
        Genre genre = genreRepository.findById(1).orElseThrow();
        byte[] contentAsByteArray = exampleFile.getContentAsByteArray();
        Video video = getVideo(genre, contentAsByteArray);
        videoItemRepository.saveAndFlush(video);

        Video videoItem = videoItemRepository.findAll().get(0);
        videoItem.setActors("newActors");
        videoItem.setDirector("newDirector");
        Genre genre2 = genreRepository.findById(2).orElseThrow();
        videoItem.setGenre(genre2);
        videoItem.setReleasedYear(2012);
        contentAsByteArray[0] = 55;
        videoItem.setItemData(new ItemData(BlobHelper.createBlob(entityManager, contentAsByteArray), "file.mp3", "newFileType"));
        videoItem.setDescription("newDescription");
        videoItem = videoItemRepository.saveAndFlush(videoItem);

        assertEquals("newActors", videoItem.getActors());
        assertEquals("newDirector", videoItem.getDirector());
        assertEquals(genre2, videoItem.getGenre());
        assertEquals(2012, videoItem.getReleasedYear());
        assertEquals("file.mp3", videoItem.getItemData().getFilename());
        assertArrayEquals(contentAsByteArray, videoItem.getItemData().getBlob().getBinaryStream().readAllBytes());
        assertEquals("newDescription", videoItem.getDescription());
    }

    private Video getVideo(Genre genre, byte[] contentAsByteArray) {
        return Video.builder()
                .title("Gladiator").description("best film")
                .actors("Crow").director("Scott")
                .releasedYear(2000)
                .genre(genre)
                .itemData(new ItemData(BlobHelper.createBlob(entityManager, contentAsByteArray), fileName, "VIDEO"))
                .build();
    }

    @Test
    public void testFailsWhenEmptySong() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Video song = new Video();
            videoItemRepository.saveAndFlush(song);
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFailsWhenEmptyBaseClassSong() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Video song = Video.builder()
                    .actors("test").director("director")
                    .releasedYear(2000)
                    .genre(genreRepository.findById(1).orElseThrow())
                    .build();
            videoItemRepository.saveAndFlush(song);
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }*/
}
