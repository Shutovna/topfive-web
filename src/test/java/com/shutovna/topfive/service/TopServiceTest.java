package com.shutovna.topfive.service;

import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.util.YamlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopServiceTest {
    @Mock
    TopRepository topRepository;

    @InjectMocks
    DefaultTopService topService;

    String testUsername = YamlUtil.getPropertyValue("topfive.test.username");

    @Test
    void findAllTops_FilterIsEmpty_ReturnsEmptyList() {
        List<Top> tops = IntStream.rangeClosed(1, 4).mapToObj(
                value -> new Top(value, TopType.SONG, "Title %d".formatted(value),
                        "details %d".formatted(value), testUsername)
        ).toList();

        doReturn(tops).when(topRepository).findAll();

        List<Top> result = topService.findAllTops(null);

        assertEquals(tops, result);

        verify(this.topRepository).findAll();
        verifyNoMoreInteractions(this.topRepository);
    }

    @Test
    void findAllTops_FilterIsSet_ReturnsEmptyList() {
        List<Top> tops = IntStream.rangeClosed(1, 4).mapToObj(
                value -> new Top(value, TopType.SONG, "Title %d".formatted(value),
                        "details %d".formatted(value), testUsername)
        ).toList();

        doReturn(tops).when(topRepository).findAllByTitleLikeIgnoreCase("%title%");

        List<Top> result = topService.findAllTops("title");

        assertEquals(tops, result);

        verify(this.topRepository).findAllByTitleLikeIgnoreCase("%title%");
        verifyNoMoreInteractions(this.topRepository);
    }

    @Test
    void findTop_TopExists_ReturnTop() {
        Top top = new Top(1, TopType.SONG, "Title %d".formatted(1),
                "details %d".formatted(1), testUsername);

        doReturn(Optional.of(top)).when(topRepository).findById(1);

        Optional<Top> result = topService.findTop(1);
        assertEquals(top, result.get());

        verify(topRepository).findById(1);
        verifyNoMoreInteractions(topRepository);
    }

    @Test
    void findTop_TopDoesNotExist_ReturnEmptyOptional() {
        doReturn(Optional.empty()).when(topRepository).findById(1);

        Optional<Top> result = topService.findTop(1);
        assertTrue(result.isEmpty());

        verify(topRepository).findById(1);
        verifyNoMoreInteractions(topRepository);
    }

    @Test
    void createTop_ReturnsCreatedTop() {
        // given
        var title = "Новый топ";
        var details = "Описание нового топа";
        var username = testUsername;

        doReturn(new Top(1, TopType.PHOTO, title, details, username))
                .when(this.topRepository).save(
                        new Top(null, TopType.PHOTO, title,
                                details, username));

        // when
        var result = this.topService.createTop(TopType.PHOTO, title, details, username);

        // then
        assertEquals(new Top(1, TopType.PHOTO, title,
                details, username), result);

        verify(this.topRepository).save(new Top(null, TopType.PHOTO, title,
                details, username));
        verifyNoMoreInteractions(this.topRepository);
    }

    @Test
    void updateTop_TopExists_UpdateTop() {
        // given
        var title = "Новый топ";
        var details = "Описание нового топа";
        var username = testUsername;
        Top top = new Top(1, TopType.PHOTO, title, details, username);

        doReturn(Optional.of(top)).when(this.topRepository).findById(1);

        // when
        this.topService.updateTop(1, title, details);

        // then
        verify(this.topRepository).findById(1);
        verifyNoMoreInteractions(this.topRepository);
    }

    @Test
    void updateTop_TopDoesNotExist_ThrowNoSuchElement() {
        // given
        var topId = 1234;
        var title = "Новый топ";
        var details = "Описание нового топа";

        doReturn(Optional.empty()).when(this.topRepository).findById(topId);

        // when
        assertThrows(NoSuchElementException.class, () ->  this.topService.updateTop(topId, title, details));

        // then
        verify(this.topRepository).findById(topId);
        verifyNoMoreInteractions(this.topRepository);
    }

    @Test
    public void deleteTop_DeleteTop() {
        // given
        var topId = 12;

        //when
        this.topService.deleteTop(topId);

        //then
        verify(topRepository).deleteById(topId);
        verifyNoMoreInteractions(topRepository);
    }
}