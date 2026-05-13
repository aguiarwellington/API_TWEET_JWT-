package Auten.demo.controller;

import Auten.demo.controller.dto.CreateTweetDto;
import Auten.demo.controller.dto.FeedDto;
import Auten.demo.controller.dto.FeedItemDto;
import Auten.demo.entities.Tweet;
import Auten.demo.entities.User;
import Auten.demo.repository.TweetRepository;
import Auten.demo.repository.UserRepository;
import org.apache.catalina.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int size) {

        var tweets = tweetRepository.findAll(PageRequest.of(page, size, Sort.by("creationTimeStamp")))
        .map(tweet -> new FeedItemDto(
               tweet.getTweetId(),
                tweet.getUser().getUsername(),
                tweet.getContent()));

        return ResponseEntity.ok(new FeedDto(
                tweets.getContent(),
                page,
                size,
                tweets.getTotalPages(),
                tweets.getTotalElements()
        ));



    }


    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto,
                                            JwtAuthenticationToken token) {

        var user = userRepository.findById(token.getName());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (dto.content() == null || dto.content().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") long tweetId,
                                            JwtAuthenticationToken token) {
        var user = userRepository.findById(token.getName());

        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException(String.valueOf(HttpStatus.NOT_FOUND)));

        var isAdmin = user.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(User.Values.ADMIN.name()));

        if (isAdmin || tweet.getUser().getId().equals(token.getName())) {
            tweetRepository.delete(tweet);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }}