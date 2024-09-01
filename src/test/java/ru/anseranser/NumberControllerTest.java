package ru.anseranser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anseranser.controller.NumberController;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;
import ru.anseranser.service.NumberService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NumberControllerTest {

    @Mock
    private NumberService numberService;

    @InjectMocks
    private NumberController numberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}