package com.example.flowable.demo.service;

import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorFlowService {
    private final RuntimeService runtimeService;

    @Autowired
    public OrchestratorFlowService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    /**
     * Start flowable BPMN process
     *
     * @param processKey identifying flow key defined in the BPMN xml
     * @return resulting variables when the flow has been completed
     */
    public Map<String, Object> startFlow(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        return ((ExecutionEntity) processInstance).getVariables();
    }

    /**
     * Start flowable BPMN process
     *
     * @param processKey            identifying flow key defined in the BPMN xml
     * @param inputRequestVariables flow input variables
     * @return resulting variables when the flow has been completed
     */
    public Map<String, Object> startFlow(String processKey, Map<String, Object> inputRequestVariables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, inputRequestVariables);
        return ((ExecutionEntity) processInstance).getVariables();
    }
}
