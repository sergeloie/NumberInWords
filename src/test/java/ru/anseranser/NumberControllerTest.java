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
    public void zeroTest() throws Exception {
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
}
