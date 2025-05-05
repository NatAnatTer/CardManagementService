package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.CardService;
import ru.project.CardManagementService.dto.CardDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CardDTO> getCard() {
        return cardService.getCards();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CardDTO> getAllCardByFilter(Pageable pageable, @RequestParam Map<String, Object> query) {
        return cardService.getCardsByFilter(pageable, query);
    }
    @GetMapping("/balance")
    public HashMap<Long,String> getBalance(@RequestParam String idCard){
        return cardService.getBalance(idCard);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }

    @PutMapping
    public ResponseEntity<CardDTO> putCard(@RequestBody CardDTO card) {
        CardDTO updatedCard = cardService.updateCard(card);
        return ResponseEntity.ok(updatedCard);
    }


    @PutMapping("/blocked")
    public ResponseEntity<CardDTO> blockedCard(@RequestParam String idCard) {
        CardDTO updatedCard = cardService.blockedCard(idCard);
        return ResponseEntity.ok(updatedCard);
    }


    @DeleteMapping("/delete")
    public void deleteById(@RequestParam String idCard) {
        cardService.deleteById(idCard);
    }


}
