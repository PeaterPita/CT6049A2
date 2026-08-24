package github.peaterpita.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CoverService {
    private final RedisTemplate<String, byte[]> imgTemplate;
    private final RestTemplate restTemplate;

    public CoverService(RedisTemplate<String, byte[]> imgTemplate) {
        this.imgTemplate = imgTemplate;
        this.restTemplate = new RestTemplate();
    }

    public byte[] getCover(String isbn) {

        if (isbn == null || isbn.isBlank()) {
            return null;
        }

        String cacheKey = "cover:" + isbn;

        // ###########################################################
        // # Check if cover is cached in Redis first. If it is use
        // # that
        // ###########################################################
        if (Boolean.TRUE.equals(imgTemplate.hasKey(cacheKey))) {
            return imgTemplate.opsForValue().get(cacheKey);
        }

        try {

            // ###########################################################
            // # If the cover is not cached, reach out to the
            // # openlibrary covers api to get a cover for that isbn
            // # If a reply is recieved and the headers are good
            // # status, extract body
            // # and cache contents into redis.
            // # return same contents out
            // ###########################################################
            String url = "https://covers.openlibrary.org/b/isbn/"
                    + isbn
                    + "-M.jpg?default=false";
            ResponseEntity<byte[]> res = restTemplate.getForEntity(url, byte[].class);

            if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
                byte[] imgBytes = res.getBody();
                imgTemplate.opsForValue().set(cacheKey, imgBytes, Duration.ofHours(96));
                return imgBytes;
            }
        } catch (HttpClientErrorException.NotFound err) {

            // ###########################################################
            // # Even if the request to OpenLibary fails,
            // # still cache a result. This prevent covers that
            // # continue failing to keep calling the same endpoint.
            // # These caches only last 24 hours as book covers may be
            // # updated / be temporarily down
            // ###########################################################

            imgTemplate.opsForValue().set(cacheKey, new byte[0], Duration.ofHours(24));
        } catch (Exception err) {
            System.out.println("General Error in the getCover");
        }
        return null;
    }

}
