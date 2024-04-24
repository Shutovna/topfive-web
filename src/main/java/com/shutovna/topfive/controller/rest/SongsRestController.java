package com.shutovna.topfive.controller.rest;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/songs")
@RequiredArgsConstructor
@Slf4j
public class SongsRestController {
    private final ItemService<Song> songService;

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findSongs() {
        log.debug("findSongs");
        try {
            ItemTable<Song> table = new ItemTable(songService.findAllItems());
            log.debug("returning " + table);
            return new ResponseEntity<>(table.getRows(), HttpStatus.OK);
        }    catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
