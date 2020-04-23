package com.privatekit.services.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureWebMvc
public class WaypointsControllerTestSuite {

    //~ Instance Fields ..............................................................................................................................
    private MockMvc mockMvc;

    @BeforeEach
    void initValues() {

        mockMvc             = standaloneSetup(waypointsController).build();
    }

    @Autowired
    private WaypointsController waypointsController;

    //~ Methods ......................................................................................................................................

    @Test
    public void testPostWaypoint()
            throws Exception
    {
        mockMvc.perform(post("/v1.0/none/waypoints")).andDo(print()).andExpect(status().isOk());
    }
}
