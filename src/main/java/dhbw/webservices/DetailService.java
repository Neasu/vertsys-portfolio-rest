package dhbw.webservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
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

@RestController
public class DetailService {

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable(value = "id") String id, @RequestParam(value = "type") String type) {

        RequestCategory category = null;
        Optional<String> result = null;
        switch (type) {
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
            default: {
                System.out.println(type);
                return "";
            }
        }

        SpotifyRequest request = new SpotifyRequest(RequestType.DETAIL);

        try {
            result = request.performeRequestDetail(category,id);
        } catch (WrongRequestTypeException e) {
            e.printStackTrace();
        }

        if (result.isPresent()) {
            String json = result.get();

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


        }

        return "";

    }

    private List<SearchResultList> processArtist(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsArtist artist = null;

        try {
            artist = mapper.readValue(json, DetailsArtist.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<SearchResultList> processAlbum(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsAlbum album = null;

        try {
            album = mapper.readValue(json, DeatilsAlbum.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<SearchResultList> processTrack(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DetailsTrack track = null;

        try {
            track = mapper.readValue(json, DetailsTrack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
