package code.travelplanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RateLimitTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "max")
    void heavyDbSearch() throws Exception {
        Long tripId = 1L;
        String path = "/travelplanner/users/" + tripId + "/search";

        // 1-20: Allowed
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get(path)
                            .param("query", "john")
                            .header("Authorization", "Bearer mock-token")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // 21: Rate limited
        mockMvc.perform(get(path)
                        .param("query", "john")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void unauthenticated() throws Exception {
        String testIp = "192.168.1.100";

        // First 20 requests should pass
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/travelplanner/login")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content("{\"email\":\"max.stevenson5@outlook.com\",\"password\":\"password\"}")
                            .header("X-Forwarded-For", testIp))
                    .andExpect(status().isOk());
        }

        // 6th request must return 429 Too Many Requests
        mockMvc.perform(post("/travelplanner/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content("{\"email\":\"max.stevenson5@outlook.com\",\"password\":\"password\"}")
                        .header("X-Forwarded-For", testIp))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @WithMockUser(username = "max")
    void externalApI() throws Exception {
        Long tripId = 1L;
        String path = "/travelplanner/" + tripId + "/map-data";

        // 1-10: Allowed
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get(path)
                            .header("Authorization", "Bearer mock-token")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // 11: Rate limited
        mockMvc.perform(get(path)
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void general() throws Exception {
        String path = "/travelplanner/createTrip";
        String payload = """
            {
                "title": "Summer Trip",
                "waypoints": ["Paris", "Rome"],
                "startDate": "2026-07-01",
                "endDate": "2026-07-10"
            }
            """;

        // 1-20: Allowed
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(post(path)
                            .header("Authorization", "Bearer mock-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isOk());
        }

        // 21: Rate limited
        mockMvc.perform(post(path)
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isTooManyRequests());
    }
}
