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
import dhbw.pojo.search.artist.Image;
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

            List<SearchResultList> searchResultList = null;
            SearchResult searchResult = null;

            switch (category) {
                case ALBUM: {
                    searchResultList = processAlbum(json);
                    break;
                }
                case TRACK: {
                    searchResultList = processTrack(json);
                    break;
                }
                case ARTIST: {
                    searchResultList = processArtist(json);
                    break;
                }
            }

            searchResult = new SearchResult(query, type, searchResultList);

            ObjectMapper mapper = new ObjectMapper();
            String response = "";

            try {
                response = mapper.writeValueAsString(searchResult);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
          
            return response;
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
        Artists artists = artist.getArtists();
        List<Item> items = artists.getItems();
        Iterator<Item> it = items.iterator();

        List<SearchResultList> results = new ArrayList<SearchResultList>();

        while(it.hasNext()) {
            Item item = it.next();

            dhbw.pojo.search.artist.Image img = item.getImages().get(0);

            SearchResultList searchResultList = new SearchResultList(
                    item.getId(),
                    item.getName(),
                    item.getName(),
                    item.getExternalUrls().getSpotify(),
                    img.getUrl()
            );
            results.add(searchResultList);
        }


        return results;
    }

    private List<SearchResultList> processAlbum(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SearchAlbum album = null;

        try {
            album = mapper.readValue(json, SearchAlbum.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Albums albums = album.getAlbums();
        List<dhbw.pojo.search.album.Item> items = albums.getItems();
        Iterator<dhbw.pojo.search.album.Item> it = items.iterator();

        List<SearchResultList> results = new ArrayList<SearchResultList>();

        while(it.hasNext()) {
            dhbw.pojo.search.album.Item item = it.next();

            dhbw.pojo.search.album.Image img = item.getImages().get(0);

            SearchResultList searchResultList = new SearchResultList(
                    item.getId(),
                    item.getName(),
                    item.getName(),
                    item.getExternalUrls().getSpotify(),
                    img.getUrl()
            );
            results.add(searchResultList);
        }


        return results;
    }

    private List<SearchResultList> processTrack(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SearchTrack track = null;

        try {
            track = mapper.readValue(json, SearchTrack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tracks tracks = track.getTracks();
        List<dhbw.pojo.search.track.Item> items = tracks.getItems();
        Iterator<dhbw.pojo.search.track.Item> it = items.iterator();

        List<SearchResultList> results = new ArrayList<SearchResultList>();

        while(it.hasNext()) {
            dhbw.pojo.search.track.Item item = it.next();

            dhbw.pojo.search.track.Image img = item.getAlbum().getImages().get(0);

            SearchResultList searchResultList = new SearchResultList(
                    item.getId(),
                    item.getName(),
                    item.getName(),
                    item.getExternalUrls().getSpotify(),
                    img.getUrl()
            );
            results.add(searchResultList);
        }


        return results;
    }


}
