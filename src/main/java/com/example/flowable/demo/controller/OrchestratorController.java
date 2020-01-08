package com.example.flowable.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.flowable.demo.domain.OrchestratorRequest;
import com.example.flowable.demo.domain.OrchestratorResponse;
import com.example.flowable.demo.service.OrchestratorFlowService;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * The Orchestrator Rest Controller Flowable process that calls several microservices
 */
@RestController
public class OrchestratorController {
    private final OrchestratorFlowService orchestratorFlowService;

    public OrchestratorController(
            OrchestratorFlowService orchestratorFlowService) {
        this.orchestratorFlowService = orchestratorFlowService;
    }

    /**
     * The Orchestrator Rest Controller Flowable process that calls several microservices
     *
     * @param orchestratorRequest with the variable bsn that is used as input for the BPMN flow
     * @return relatiestatus(relationship status) for the bsn (identification id)
     * TODO: handle exceptions/empty values etc
     */
    @GetMapping("/relatiestatus")
    public ResponseEntity submit(@RequestBody OrchestratorRequest orchestratorRequest) {
        //Request mapping
        Map<String, Object> inputRequestVariables = new HashMap<>();
        inputRequestVariables.put("bsn", orchestratorRequest.getBsn());

        Map<String, Object> outputVariables = orchestratorFlowService.startFlow("OrchestrationProcessFull", inputRequestVariables);
        ArrayNode leefsituatieHttpTaskResponse = (ArrayNode) outputVariables.get("leefsituatieHttpTaskResponse");
        if(leefsituatieHttpTaskResponse.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("leefsituatieHttpTaskResponse is empty\n");
        }
        //Response mapping
        OrchestratorResponse orchestratorResponse = new OrchestratorResponse();
        try{

            String relatiestatus =
                    leefsituatieHttpTaskResponse.get(0).get("relatiestatus").textValue();
            orchestratorResponse.setRelatiestatus(relatiestatus);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
        return ResponseEntity.ok(orchestratorResponse);
    }

}