package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import com.techelevator.service.FindGroupVotesResponse;
import com.techelevator.service.FindPartyResponse;
import com.techelevator.service.FindRestaurantResponse;
import com.techelevator.security.jwt.TokenProvider;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("http://localhost:3000")
@RestController
public class RestaurantTinderController {
    private final Logger log = LoggerFactory.getLogger(RestaurantTinderController.class);

    PartyDao partyDao;
    RestaurantDao restaurantDao;
    GroupVotesDao groupVotesDao;
    RestaurantGroupDao restaurantGroupDao;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    public RestaurantTinderController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                                      UserDao userDao, PartyDao partyDao, RestaurantDao restaurantDao, GroupVotesDao groupVotesDao, RestaurantGroupDao restaurantGroupDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.partyDao = partyDao;
        this.restaurantDao = restaurantDao;
        this.groupVotesDao = groupVotesDao;
        this.restaurantGroupDao = restaurantGroupDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/create_group", method = RequestMethod.POST)
    public void createGroup(@Valid @RequestBody PartyDTO partyDTO) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)tokenProvider.getAuthentication(partyDTO.getToken());

        Party party = new Party();
        party.setName(partyDTO.getEventName());
        party.setUserId(auth.getName());
        party.setEndDate(partyDTO.getEndDate());
        party.setHasEnded(partyDTO.isHasEnded());
        party.setLocation(partyDTO.getLocation());

        int key = partyDao.create(party);
        party.setId(key);
        restaurantGroupDao.addDataToRestaurantGroup(party);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/find_groups/{token}", method = RequestMethod.GET)
    public ResponseEntity<FindPartyResponse> getGroups(@PathVariable String token) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)tokenProvider.getAuthentication(token);
        log.info("Find Groups for: " +auth.getName());
        FindPartyResponse findPartyResponse = new FindPartyResponse();
        findPartyResponse.setParties(partyDao.findAll(auth.getName()));
        return new ResponseEntity<>(findPartyResponse, null, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/find_restaurants/{location}", method = RequestMethod.GET)
    public ResponseEntity<FindRestaurantResponse> getRestaurants(@PathVariable String location) {
        FindRestaurantResponse findRestaurantResponse = new FindRestaurantResponse();
        findRestaurantResponse.setRestaurants(restaurantDao.findRestaurant(location));
        return new ResponseEntity<>(findRestaurantResponse, null, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/party/{groupId}/view-finalists", method = RequestMethod.GET)
    public ResponseEntity<FindGroupVotesResponse> getGroupVotes(@PathVariable int groupId) {
        FindGroupVotesResponse findGroupVotesResponse = new FindGroupVotesResponse();
        findGroupVotesResponse.setGroupVotes(groupVotesDao.retrieveVotes(groupId));
        return new ResponseEntity<>(findGroupVotesResponse, null, HttpStatus.OK);
    }

    //Party invite link, click button to open invite link page
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/party/{groupId}/invite", method = RequestMethod.POST)

    //Main party page
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/party/{groupId}", method = RequestMethod.GET)

    //Voting page for party, click button to open vote page
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/party/{groupId}/vote", method = RequestMethod.POST)
}