package gps.Controller;

import gps.GpsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = GpsApplication.class)
@AutoConfigureMockMvc
public class GpsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void gpsGetAttractions() throws Exception {
        this.mockMvc.perform(get("/gpsGetAttractions").characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    void gpsGetUserLocation() throws Exception {
        this.mockMvc.perform(get("/gpsGetUserLocation").param("userId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75").characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}
