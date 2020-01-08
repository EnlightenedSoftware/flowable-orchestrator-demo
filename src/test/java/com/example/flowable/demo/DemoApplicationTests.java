package com.example.flowable.demo;

import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import com.example.flowable.demo.service.OrchestratorFlowService;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
@Ignore("TODO")
@AutoConfigureMockMvc
class DemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrchestratorFlowService orchestratorFlowService;
    //Mock API server(https://github.com/clue/docker-json-server)
    @Container
    private final FixedHostPortGenericContainer jsonMockServer = new FixedHostPortGenericContainer<>("clue/json-server:latest")
            .withClasspathResourceMapping("mock.json",
                    "/data/db.json",
                    BindMode.READ_WRITE)
            .withFixedExposedPort(80, 80)
            .waitingFor(Wait.forHttp("/"));

    @BeforeEach
    void startMock() {
        jsonMockServer.start();
    }

    @AfterEach
    void stopMock() {
        jsonMockServer.stop();
    }

    @Test
    void test() {

    }
}
