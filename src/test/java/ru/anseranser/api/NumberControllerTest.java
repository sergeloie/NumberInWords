package ru.anseranser.api;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NumberControllerTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MockMvc mockMvc;

    private String readJson(String filename) throws IOException {
        var resource = resourceLoader.getResource("classpath:json/" + filename);
        return Files.readString(Path.of(resource.getURI()));
    }

    private String convert(String json) throws Exception {
        return mockMvc.perform(
                        post("/convert").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String convertExpectError(String json) throws Exception {
        return mockMvc.perform(
                        post("/convert").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void oneTest() throws Exception {
        var result = convert(readJson("1.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(1),
                        v -> v.node("gender").isEqualTo("MASCULINE"),
                        v -> v.node("case").isEqualTo("NOMINATIVE"),
                        v -> v.node("numberInWords").isEqualTo("один"));
    }

    @Test
    void zeroTest() throws Exception {
        var result = convert(readJson("0.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(0),
                        v -> v.node("gender").isEqualTo("MASCULINE"),
                        v -> v.node("case").isEqualTo("NOMINATIVE"),
                        v -> v.node("numberInWords").isEqualTo("ноль"));
    }

    @Test
    void allNumbersTest() throws Exception {
        var result = convert(readJson("111987654321.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(111987654321L),
                        v -> v.node("gender").isEqualTo("FEMININE"),
                        v -> v.node("case").isEqualTo("INSTRUMENTAL"),
                        v -> v.node("numberInWords")
                                .isEqualTo("ста одиннадцатью миллиардами "
                                        + "девятьюстами восьмьюдесятью семью миллионами "
                                        + "шестьюстами пятьюдесятью четырьмя тысячами "
                                        + "тремястами двадцатью одной"));
    }

    @Test
    void dativeTest() throws Exception {
        var result = convert(readJson("101311422981.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(101311422981L),
                        v -> v.node("gender").isEqualTo("NEUTER"),
                        v -> v.node("case").isEqualTo("DATIVE"),
                        v -> v.node("numberInWords")
                                .isEqualTo("ста одному миллиарду "
                                        + "трёмстам одиннадцати миллионам "
                                        + "четырёмстам двадцати двум тысячам "
                                        + "девятистам восьмидесяти одному"));
    }

    @Test
    void accusativeTest() throws Exception {
        var result = convert(readJson("999999999999.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(999999999999L),
                        v -> v.node("gender").isEqualTo("MASCULINE"),
                        v -> v.node("case").isEqualTo("ACCUSATIVE"),
                        v -> v.node("numberInWords")
                                .isEqualTo("девятьсот девяносто девять миллиардов "
                                        + "девятьсот девяносто девять миллионов "
                                        + "девятьсот девяносто девять тысяч "
                                        + "девятьсот девяносто девять"));
    }

    @Test
    void outOfBoundsTest() throws Exception {
        var result = convertExpectError(readJson("9999999999999.json"));

        assertThatJson(result).and(v -> v.node("error").isPresent());
    }

    @Test
    void prepositionalTest() throws Exception {
        var result = convert(readJson("1001001001.json"));

        assertThatJson(result)
                .and(
                        v -> v.node("number").isEqualTo(1001001001L),
                        v -> v.node("gender").isEqualTo("FEMININE"),
                        v -> v.node("case").isEqualTo("PREPOSITIONAL"),
                        v -> v.node("numberInWords").isEqualTo("одном миллиарде одном миллионе одной тысяче одной"));
    }
}
