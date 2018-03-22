/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Marius
 */

@RestController
public class SearchService {



    @RequestMapping("/search")
    public String search(@RequestParam(value = "query") String query, @RequestParam(value = "type") String type){

        RequestCategory category = null;
        Optional<String> result = null;
        switch(type){
            case "ARTIST": {
                category = RequestCategory.ARTIST;
                break;
            }
            case "ALBUM": {
                category = RequestCategory.ALBUM;
                break;
            }
            case "TRACK": {
                category = RequestCategory.TRACK;
                break;
            }
            default:{
                System.out.println(type);
                return "";
            }
        }

        SpotifyRequest request = new SpotifyRequest(RequestType.SEARCH);

        try {
            result = request.performeRequestSearch(category,query);
        } catch (WrongRequestTypeException e) {
            e.printStackTrace();
        }

        if (result.isPresent()) {
            String json = result.get();

            SearchResult searchResult = null;

            switch (category) {
                case ALBUM: {
                    searchResult = processAlbum(json);
                    break;
                }
                case TRACK: {
                    searchResult = processTrack(json);
                    break;
                }
                case ARTIST: {
                    searchResult = processArtist(json);
                    break;
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            String response;

            try {
                response = mapper.writeValueAsString(searchResult);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    private SearchResult processArtist(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SearchArtist artist = mapper.readValue(json, SearchArtist.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchResult result = null;


        return result;
    }

    private SearchResult processAlbum(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SearchAlbum album = mapper.readValue(json, SearchAlbum.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchResult result = null;


        return result;
    }

    private SearchResult processTrack(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SearchTrack track = mapper.readValue(json, SearchTrack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchResult result = null;


        return result;
    }


}
