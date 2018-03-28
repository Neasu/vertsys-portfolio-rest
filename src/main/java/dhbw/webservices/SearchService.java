/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.album.Albums;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.Artists;
import dhbw.pojo.search.artist.Item;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.pojo.search.track.Tracks;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Marius
 */

@RestController
public class SearchService {

    @RequestMapping("/search")
    public String search(@RequestParam(value = "query") String query, @RequestParam(value = "type") String type){

        RequestCategory category;

        try{
            category = RequestCategory.valueOf(type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            category = RequestCategory.ARTIST;
        }

        SpotifyRequest   request = new SpotifyRequest(RequestType.SEARCH);
        Optional<String> response  = Optional.empty();

        try {
            response = request.performeRequestSearch(category,query);
        } catch (WrongRequestTypeException e) {
            e.printStackTrace();
        }

        if (response.isPresent()) {
            String responseJSON = response.get();

            List<SearchResultList> searchResultList = null;

            switch (category) {
                case ALBUM: {
                    searchResultList = processAlbum(responseJSON);
                    break;
                }
                case TRACK: {
                    searchResultList = processTrack(responseJSON);
                    break;
                }
                case ARTIST: {
                    searchResultList = processArtist(responseJSON);
                    break;
                }
            }

            SearchResult searchResult = new SearchResult(query, type, searchResultList);

            ObjectMapper mapper = new ObjectMapper();
            String result = "";

            try {
                result = mapper.writeValueAsString(searchResult);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return result;
        }

        return "";
    }

    private List<SearchResultList> processArtist(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SearchArtist artist = null;

        try {
             artist = mapper.readValue(json, SearchArtist.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (artist != null) {
            Artists                 artists = artist.getArtists();
            List<Item>              items   = artists.getItems();
            Iterator<Item>          it      = items.iterator();
            List<SearchResultList>  results = new ArrayList<>();

            while(it.hasNext()) {
                Item item = it.next();

                String imageURL = "";

                if(!item.getImages().isEmpty()) {
                    imageURL = item.getImages().get(0).getUrl();
                }

                SearchResultList searchResultList = new SearchResultList(
                        item.getId(),
                        item.getName(),
                        item.getName(),
                        item.getExternalUrls().getSpotify(),
                        imageURL
                );

                results.add(searchResultList);
            }

            return results;
        } else {
            return new ArrayList<>();
        }
    }

    private List<SearchResultList> processAlbum(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SearchAlbum album = null;

        try {
            album = mapper.readValue(json, SearchAlbum.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (album != null) {
            Albums                                  albums  = album.getAlbums();
            List<dhbw.pojo.search.album.Item>       items   = albums.getItems();
            Iterator<dhbw.pojo.search.album.Item>   it      = items.iterator();

            List<SearchResultList> results = new ArrayList<>();

            while(it.hasNext()) {
                dhbw.pojo.search.album.Item item = it.next();

                String imgURL = "";

                if(!item.getImages().isEmpty()) {
                    imgURL = item.getImages().get(0).getUrl();
                }

                SearchResultList searchResultList = new SearchResultList(
                        item.getId(),
                        item.getName(),
                        item.getName(),
                        item.getExternalUrls().getSpotify(),
                        imgURL
                );

                results.add(searchResultList);
            }

            return results;
        } else {
            return new ArrayList<>();
        }
    }

    private List<SearchResultList> processTrack(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SearchTrack track = null;

        try {
            track = mapper.readValue(json, SearchTrack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (track != null) {
            Tracks                                  tracks  = track.getTracks();
            List<dhbw.pojo.search.track.Item>       items   = tracks.getItems();
            Iterator<dhbw.pojo.search.track.Item>   it      = items.iterator();

            List<SearchResultList> results = new ArrayList<SearchResultList>();

            while(it.hasNext()) {
                dhbw.pojo.search.track.Item item = it.next();

                String imgURL = "";

                if(!item.getAlbum().getImages().isEmpty() ) {
                    imgURL = item.getAlbum().getImages().get(0).getUrl();
                }

                SearchResultList searchResultList = new SearchResultList(
                        item.getId(),
                        item.getName(),
                        item.getName(),
                        item.getExternalUrls().getSpotify(),
                        imgURL
                );

                results.add(searchResultList);
            }

            return results;
        } else {
            return new ArrayList<>();
        }
    }
}
