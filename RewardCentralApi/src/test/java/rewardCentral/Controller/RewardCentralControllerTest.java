package rewardCentral.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import rewardCentral.RewardcentralApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RewardcentralApplication.class)
@AutoConfigureMockMvc
public class RewardCentralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAttractionRewardPoints() throws Exception {
        this.mockMvc.perform(get("/getAttractionRewardPoints")
                .param("attractionId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe85")
                .param("userId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}


