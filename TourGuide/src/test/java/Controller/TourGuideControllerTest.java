package Controller;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.Controller.TourGuideController;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TourGuideController.class)
public class TourGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourGuideService tourGuideService;

    @Test
    void getLocation() throws Exception {
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(10.0,10.0), new Date());
        Mockito.when(tourGuideService.getUserLocation(ArgumentMatchers.any(User.class))).thenReturn(visitedLocation);
        this.mockMvc.perform(get("/getLocation")
                .param("userName", "u1")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

    }
}
