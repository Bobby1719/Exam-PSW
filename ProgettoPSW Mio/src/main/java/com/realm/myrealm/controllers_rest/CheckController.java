package com.realm.myrealm.controllers_rest;

import com.realm.myrealm.support.authentication.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/check")
public class CheckController {

    @GetMapping("/simple")
    public ResponseEntity checkSimple() {
        return new ResponseEntity("Check status ok!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/logged")
    public ResponseEntity checkLogged() {
        return new ResponseEntity("Check status ok, hi " + Utils.getEmail() + "!", HttpStatus.OK);
    }
}