package dhbw.webservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.detail.DetailResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class DetailService {

    @RequestMapping("/detail/{id}")
    public DetailResult detail(@PathVariable(value = "id") String id, @RequestParam(value = "type") String type) {

        RequestCategory category;

        try{
            category = RequestCategory.valueOf(type);
        } catch (Exception e) {
            e.printStackTrace();
            category = RequestCategory.ARTIST;
        }

        SpotifyRequest      request = new SpotifyRequest(RequestType.DETAIL);
        Optional<String>    result  = Optional.empty();

        try {
            result = request.performeRequestDetail(category, id);
        } catch (WrongRequestTypeException e) {
            e.printStackTrace();
        }


        if (result.isPresent()) {
            String json = result.get();

            switch (category) {
                case ALBUM: {
                    return processAlbum(json);
                }
                case TRACK: {
                    return processTrack(json);
                }
                case ARTIST: {
                    return processArtist(json);
                }
                default: {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    private DetailResult processArtist(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsArtist artist = null;

        try {
            artist = mapper.readValue(json, DetailsArtist.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String details = String.format("Popularity: %d, Followers: %d, Genres: %s",
                artist.getPopularity(),
                artist.getFollowers().getTotal(),
                String.join(", ", artist.getGenres()));

        if (artist != null) {
            DetailResult result = new DetailResult(artist.getName(), details);
            return result;
        } else {
            return null;
        }
    }

    private DetailResult processAlbum(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsAlbum album = null;

        try {
            album = mapper.readValue(json, DetailsAlbum.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        String details = String.format("Released: %s, Label: %s, Popularity: %d",
                album.getReleaseDate(),
                album.getLabel(),
                album.getPopularity());

        if (album != null) {
            DetailResult result = new DetailResult(album.getName(), details);
            return result;
        } else {
            return null;
        }
    }

    private DetailResult processTrack(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsTrack track = null;

        try {
            track = mapper.readValue(json, DetailsTrack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int milliseconds = track.getDurationMs();

        int seconds = (milliseconds / 1000) % 60 ;
        int minutes = ((milliseconds / (1000*60)) % 60);
        int hours   = ((milliseconds / (1000*60*60)) % 24);

        String details = String.format("Album: %s, Duration %02d:%02d:%02d, Popularity: %d",
                track.getAlbum().getName(),
                hours,
                minutes,
                seconds,
                track.getPopularity()
        );

        if (track != null) {
            DetailResult result = new DetailResult(track.getName(), details);
            return result;
        } else {
            return null;
        }
    }
}
