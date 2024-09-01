package ru.anseranser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NumberControllerTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void oneTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/1.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(1),
                v -> v.node("gender").isEqualTo("MASCULINE"),
                v -> v.node("case").isEqualTo("NOMINATIVE"),
                v -> v.node("numberInWords").isEqualTo("один")
        );
    }

    @Test
    void zeroTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/0.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(0),
                v -> v.node("gender").isEqualTo("MASCULINE"),
                v -> v.node("case").isEqualTo("NOMINATIVE"),
                v -> v.node("numberInWords").isEqualTo("ноль")
        );
    }

    @Test
    void allNumbersTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/111987654321.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(111987654321L),
                v -> v.node("gender").isEqualTo("FEMININE"),
                v -> v.node("case").isEqualTo("INSTRUMENTAL"),
                v -> v.node("numberInWords").isEqualTo("ста одиннадцатью миллиардами "
                        + "девятьюстами восьмьюдесятью семью миллионами "
                        + "шестьюстами пятьюдесятью четырьмя тысячами "
                        + "тремястами двадцатью одной")
        );
    }

    @Test
    void dativeTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/101311422981.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(101311422981L),
                v -> v.node("gender").isEqualTo("NEUTER"),
                v -> v.node("case").isEqualTo("DATIVE"),
                v -> v.node("numberInWords").isEqualTo("ста одному миллиарду "
                        + "трёмстам одиннадцати миллионам "
                        + "четырёмстам двадцати двум тысячам "
                        + "девятистам восьмидесяти одному")
        );
    }

    @Test
    void accusativeTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/999999999999.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(999999999999L),
                v -> v.node("gender").isEqualTo("MASCULINE"),
                v -> v.node("case").isEqualTo("ACCUSATIVE"),
                v -> v.node("numberInWords").isEqualTo("девятьсот девяносто девять миллиардов "
                        + "девятьсот девяносто девять миллионов "
                        + "девятьсот девяносто девять тысяч "
                        + "девятьсот девяносто девять")
        );
    }

    @Test
    void prepositionalTest() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:json/1001001001.json");
        String request = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        var result = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).and(
                v -> v.node("number").isEqualTo(1001001001L),
                v -> v.node("gender").isEqualTo("FEMININE"),
                v -> v.node("case").isEqualTo("PREPOSITIONAL"),
                v -> v.node("numberInWords").isEqualTo("одном миллиарде одном миллионе одной тысяче одной")
        );
    }
}
