package candh.crm.controller;

import candh.crm.exceptions.FriendNotExistException;
import candh.crm.model.User;
import candh.crm.payload.request.ByIdRequest;
import candh.crm.payload.request.contact.ChangeNotesRequest;
import candh.crm.payload.request.contact.FriendRequest;
import candh.crm.repository.UserRepository;
import candh.crm.service.ContactRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ContactController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRelationService contactRelationService;

    /**
     * Handles Http Post for all friends of a user.
     */
    @PostMapping("/friend/listFriends")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listFriends(
            @Valid @RequestBody ByIdRequest byIdRequest) {
        Optional<User> user = userRepository.findById(byIdRequest.getId());
        if (user.isPresent()) {
            return ResponseEntity.ok(contactRelationService
                    .findAllFriends(byIdRequest.getId()));
        } else {
            return ResponseEntity.ok("Id not found.");
        }
    }

/*
    @PostMapping("/friend/withdrawRequest")
    @PostMapping("/friend/confirmRequest")
    @PostMapping("/friend/refuseRequest")
    @PostMapping("/friend/listSentRequests")
    @PostMapping("/friend/listReceivedRequests")
    @PostMapping("/friend/search")
*/

//    @PostMapping("/friend/sendRequest")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> sendRequest(@RequestBody Contact friendship) {
//        Contact contact = contactRelationService
//                .findByUserAndFriend(friendship.getUserId(), friendship.getFriendId());
//        if (contact != null)
//        {
//            if (contact.isAccepted()) {
//                return ResponseEntity.ok("Already friends.");
//            } else {
//                return ResponseEntity.ok("Request already sent but not yet confirmed.");
//            }
//        }
//        contactRelationService.saveContact(
//                new Contact(friendship.getUserId(), friendship.getFriendId()));
//        contactRelationService.saveContact(
//                new Contact(friendship.getFriendId(), friendship.getUserId()));
//
//        return ResponseEntity.ok("Friend request sent.");
//    }

    /**
     * Handles Http Post for friend deletion.
     * Before someone gets deleted, the initiator must be a friend of the person.
     */
    @PostMapping("/friend/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteFriend(
            @Valid @RequestBody FriendRequest friendRequest) {
        Optional<User> user = userRepository.findById(friendRequest.getUserId());
        if (user.isPresent()) {
            try {
                // delete
                contactRelationService.deleteFriend(friendRequest.getUserId(),
                        friendRequest.getFriendId());
            } catch (FriendNotExistException e) {
                return ResponseEntity.ok(e.getMessage());
            }
            return ResponseEntity.ok("Friend deleted.");
        }
        else {
            return ResponseEntity.ok("User id not found.");
        }
    }

    /**
     * Handles Http Post for friend notes change.
     * Before someone's notes get changed, the initiator must be a friend of the person.
     */
    @PostMapping("/friend/changeNotes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeNotes(
            @Valid @RequestBody ChangeNotesRequest changeNotesRequest) {
        Optional<User> user = userRepository.findById(changeNotesRequest.getUserId());
        if (user.isPresent()) {
            try {
                // change
                contactRelationService.changeNotes(changeNotesRequest.getUserId(),
                        changeNotesRequest.getFriendId(),
                        changeNotesRequest.getNotes());
            } catch (FriendNotExistException e) {
                return ResponseEntity.ok(e.getMessage());
            }
            return ResponseEntity.ok("Notes changed.");
        }
        else {
            return ResponseEntity.ok("User id not found.");
        }
    }
}
