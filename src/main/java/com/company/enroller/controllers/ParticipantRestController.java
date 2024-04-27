package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@GetMapping(value = "")
	public ResponseEntity<?> getParticipants(
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false, defaultValue = "ASC") String sortOrder,
			@RequestParam(required = false) String key) {

		if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
			sortOrder = "ASC";
		}

		Collection<Participant> participants = participantService.getAll(sortOrder, sortBy, key);
		return new ResponseEntity<>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<>(
                    "Unable to create. A participant with login " + participant.getLogin() + " already exist.",
                    HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") String login, @RequestBody Participant updatedParticipant) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		participant.setPassword(updatedParticipant.getPassword());
		participantService.update(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

}
