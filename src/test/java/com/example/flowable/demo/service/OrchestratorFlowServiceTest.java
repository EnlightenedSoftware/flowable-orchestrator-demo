package com.example.flowable.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.node.ArrayNode;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
class OrchestratorFlowServiceTest {
    @Autowired
    private OrchestratorFlowService orchestratorFlowService;

    //Mock API server(https://github.com/clue/docker-json-server)
//    @Container
//    private final FixedHostPortGenericContainer jsonMockServer = new FixedHostPortGenericContainer<>("clue/json-server:latest")
//            .withClasspathResourceMapping("mock.json",
//                    "/data/db.json",
//                    BindMode.READ_WRITE)
//            .withFixedExposedPort(80, 80)
//            .waitingFor(Wait.forHttp("/"));
//
//    @BeforeEach
//    void startMock() {
//        jsonMockServer.start();
//    }
//
//    @AfterEach
//    void stopMock() {
//        jsonMockServer.stop();
//    }

    @Test
    @Deployment(resources = {"processes/flow-with-hard-coded-input-without-EL.bpmn20.xml"})
    void testFlowWithoutJsonEL() {
        Map<String, Object> outputVariables = orchestratorFlowService.startFlow("OrchestrationProcess");
        ArrayNode leefsituatieHttpTaskResponseJson = (ArrayNode) outputVariables.get("leefsituatieHttpTaskResponse");
        assertFalse(leefsituatieHttpTaskResponseJson.isEmpty(), "leefsituatieHttpTaskResponse should not be empty");
        String relatiestatus = leefsituatieHttpTaskResponseJson.get(0).get("relatiestatus").textValue();
        assertEquals("alleenstaand", relatiestatus);
    }

    //TODO: Fix(Fails on JSON Expression Language)
    @Test
    @Deployment(resources = {"processes/flow-with-hard-coded-input-and-JSON-EL.bpmn20.xml"})
    void testFlowWithJsonEL() {
        Map<String, Object> outputVariables = orchestratorFlowService.startFlow("OrchestrationProcessJsonEL");
        ArrayNode leefsituatieHttpTaskResponseJson = (ArrayNode) outputVariables.get("leefsituatieHttpTaskResponse");
        assertFalse(leefsituatieHttpTaskResponseJson.isEmpty(), "leefsituatieHttpTaskResponse should not be empty");
        String relatiestatus = leefsituatieHttpTaskResponseJson.get(0).get("relatiestatus").textValue();
        assertEquals("alleenstaand", relatiestatus);
    }

    //TODO: Fix(Fails on JSON Expression Language)
    @Test
    @Deployment(resources = {"processes/full-flow.bpmn20.xml"})
    void testFullFlow() {
        Map<String, Object> inputRequestVariables = new HashMap<>();
        inputRequestVariables.put("bsn", 2);
        Map<String, Object> outputVariables = orchestratorFlowService.startFlow("OrchestrationProcessFull", inputRequestVariables);
        ArrayNode leefsituatieHttpTaskResponseJson = (ArrayNode) outputVariables.get("leefsituatieHttpTaskResponse");
        assertFalse(leefsituatieHttpTaskResponseJson.isEmpty(), "leefsituatieHttpTaskResponse should not be empty");
        String relatiestatus = leefsituatieHttpTaskResponseJson.get(0).get("relatiestatus").textValue();
        assertEquals("alleenstaand", relatiestatus);
    }
}