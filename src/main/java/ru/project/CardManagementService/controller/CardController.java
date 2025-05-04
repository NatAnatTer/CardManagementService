package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.CardService;
import ru.project.CardManagementService.dto.CardDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CardDTO> GetCard() {
        return cardService.getCards();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }

    @PutMapping("/{id}")
    ResponseEntity<CardDTO> putCard(@PathVariable String id, @RequestBody CardDTO card) {
      //  cardService.cardIsExist(id);

        CardDTO updatedCard = cardService.updateCard(id, card);
        return ResponseEntity.ok(updatedCard);
    }


    @DeleteMapping("/delete/{id}")
    void deleteById(@PathVariable String id) {
        cardService.deleteById(id);
    }


}
