package tripPricer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tripPricer.TripPricerApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TripPricerApplication.class)
@AutoConfigureMockMvc
public class TripPricerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void tripPrice() throws Exception {
        this.mockMvc.perform(get("/tripPrice")
                .param("apiKey", "key")
                .param("attractionId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75")
                .param("adults", "2")
                .param("children", "1")
                .param("nightsStay", "7")
                .param("rewardsPoints", "99")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}
