package com.example.flowable.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorFlowService {
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    @Autowired
    public OrchestratorFlowService(RuntimeService runtimeService, HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    /**
     * Start flowable BPMN process
     *
     * @param processKey identifying flow key defined in the BPMN xml
     * @return resulting variables when the flow has been completed
     */
    public Map<String, Object> startFlow(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        return getHistoricProcessResponseVariables(processInstance);
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
        return getHistoricProcessResponseVariables(processInstance);
    }

    /**
     * Read variables from a finished process flow
     *
     * @param processInstance the process instance variable is used to read the id of the process that just has finished
     * @return output variables process flow(null variables are not returned)
     */
    private Map<String, Object> getHistoricProcessResponseVariables(ProcessInstance processInstance) {
        List<HistoricVariableInstance> responseVariables =
                historyService.createHistoricVariableInstanceQuery().executionId(processInstance.getProcessInstanceId()).list();
        return responseVariables.stream().filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue));
    }
}
