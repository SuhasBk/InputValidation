package com.cse5382.assignment.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Model.PhoneBookResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTest {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${READ_WRITE_USER}")
    private String username;

    @Value("${READ_WRITE_USER_PWD}")
    private String password;

    private final String AUTH_HEADER_KEY = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ConnectionSource cs;

    private ObjectMapper objectMapper;

    private String token;

    private String authHeader;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        final Map<String, Object> claims = new HashMap<>();
        claims.put("userId", username);
        claims.put("password", password);

        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)), SignatureAlgorithm.HS256)
                .compact();
        
        authHeader = "Bearer " + token;
    }

    /* Given test cases in the problem statement */

    @Test
    void testAddGivenAcceptableInput1() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("123-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isOk())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptableInput2() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Schneier, Bruce");
        pbEntry.setPhoneNumber("213-4567");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isOk())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptableInput3() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Schneier, Bruce Wayne");
        pbEntry.setPhoneNumber("321-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isOk())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testDeleteByNameGivenAcceptableInput4() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "O'Malley, John F."))
                .andExpect(status().isNotFound())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 404;
    }

    @Test
    void testDeleteByNameGivenAcceptableInput5() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "John O'Malley-Smith")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 404;
    }

    @Test
    void testDeleteByNameGivenAcceptableInput6() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "Cher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 404;
    }

    @Test
    void testAddGivenUnacceptableInput1() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Ron O''Henry");
        pbEntry.setPhoneNumber("123-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptableInput2() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Ron O'Henry-Smith-Barnes");
        pbEntry.setPhoneNumber("123-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptableInput3() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("L33t Hacker");
        pbEntry.setPhoneNumber("123-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testDeleteByNameGivenUnacceptableInput4() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "<Script>alert(\"XSS\")</Script>")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testDeleteByNameGivenUnacceptableInput5() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "Brad Everett Samuel Smith")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testDeleteByNameGivenUnacceptableInput6() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "select * from users;")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenAcceptablePhoneNumber1() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("12345");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isOk())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber2() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("(703)111-2121");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isOk())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber3() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber4() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("+1(703)111-2121");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber5() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("+32 (21) 212-2324");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber6() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("1(703)123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber7() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("011 701 111 1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber8() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("12345.12345");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenAcceptablePhoneNumber9() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("011 1 703 111 1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber1() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("123");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber2() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("1/703/123/1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber3() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("Nr 102-123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber4() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("<script>alert(\"XSS\")</script>");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber5() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("7031111234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber6() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("+1234 (201) 123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber7() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("(001) 123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }
    
    @Test
    void testAddGivenUnacceptablePhoneNumber8() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("+01 (703) 123-1234");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddGivenUnacceptablePhoneNumber9() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("(703) 123-1234 ext 204");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                        PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }


    /* 4 Additional Student Test Cases */

    @Test
    void testAddStudentUnacceptableInputStartWithCapitalize() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("firstname Lastname");
        pbEntry.setPhoneNumber("123-456-7890");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                .header(AUTH_HEADER_KEY, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEntry))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testDeleteByNameStudentUnacceptableInputEndsWithDoublePeriod() throws Exception {
        MvcResult result = mockMvc.perform(put("/phoneBook/deleteByName")
                .header(AUTH_HEADER_KEY, authHeader)
                .param("name", "John..")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @Test
    void testAddAcceptableDanishPhoneNumber() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier");
        pbEntry.setPhoneNumber("+45 00.00.00.00");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isOk())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 200;
        assert response.getMessage().equals("New Entry Added");
    }

    @Test
    void testDeleteByNumberUnacceptableNameAndPhoneNumber() throws Exception {
        PhoneBookEntry pbEntry = new PhoneBookEntry();
        pbEntry.setName("Bruce Schneier Schneier Bruce");
        pbEntry.setPhoneNumber("123-456789");

        String jsonEntry = objectMapper.writeValueAsString(pbEntry);

        MvcResult result = mockMvc.perform(post("/phoneBook/add")
                        .header(AUTH_HEADER_KEY, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntry))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        PhoneBookResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookResponse.class);
        assert response.getStatusCode() == 400;
    }

    @AfterEach
    void tearDown() throws SQLException {
        TableUtils.clearTable(cs, PhoneBookEntry.class);
    }
}
