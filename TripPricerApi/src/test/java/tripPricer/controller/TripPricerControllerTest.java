package tripPricer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tripPricer.Provider;
import tripPricer.TripPricerApplication;
import tripPricer.service.TripPricerService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TripPricerApplication.class)
@AutoConfigureMockMvc
public class TripPricerControllerTest {

    @MockBean
    private TripPricerService tripPricerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void tripPrice() throws Exception {
        List<Provider> providers = new ArrayList<>();
        when(tripPricerService.getPrice("key", UUID.fromString("79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75")
                , 2, 1, 7, 99)).thenReturn(providers);
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
