package ru.project.CardManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.project.CardManagementService.controller.CardController;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.service.CardService;

import java.util.List;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    CardService cardService;

    private List<Card> getCard(){
     return  List.of(
               new Card()
        );
    }
}
