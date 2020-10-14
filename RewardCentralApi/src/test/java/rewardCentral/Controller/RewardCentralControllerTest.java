package rewardCentral.Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import rewardCentral.Service.RewardCentralService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RewardCentralController.class)
public class RewardCentralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardCentralService rewardCentralService;

    @Test
    void getAttractionRewardPoints() throws Exception {
        Mockito.when(rewardCentralService.getAttractionRewardPoints(UUID.fromString("79ad4e7d-49b9-4c82-bd88-0e5dbc41fe85")
                , UUID.fromString("79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75"))).thenReturn(999);
        this.mockMvc.perform(get("/attractionrewardpoints")
                .param("attractionId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe85")
                .param("userId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}


