# flowable-orchestrator demo
This project is intended to demonstrate the capabilities of the Flowable BPMN Engine as a microservice orchestrator.
In this example two microservices(mocked) are called. The result of the first call is used for the input of the second call. 
Part of the result of the second call is returned. 

The variable 'bsn' (nl gov identification id) that is used as input for the BPMN flow.
The variable 'relatiestatus' is returned(relationship status) 
#Usage
GET http://localhost:8888/relatiestatus

Content-Type: application/json

Request body: {"bsn": "1"}

# Mock Server
Two mocked services
- brp
    GET http://localhost/brp?bsn=1
- leefsituatie
    GET http://localhost/leefsituatie?brpID=123

For more details on used mock data view: [src/test/resources/mock.json](src/test/resources/mock.json)

Manually start mock server:

[docker-compose -f docker/docker-compose-json-mock-server.yml up](docker/docker-compose-json-mock-server.yml)

# all-in-one + mock
[docker-compose -f docker/docker-compose-all-in-one-with-json-mock.yml up](docker/docker-compose-all-in-one-with-json-mock.yml)

# Issue
When we use JSON expression language, with the flowable embedded engine, the JSon EL is not evaluated correctly. When I import
 the exact same BPMN flow in the [all-in-one docker image + mock server](docker/docker-compose-all-in-one-with-json-mock.yml), it works as expected. 
 
 [Used flow(flow-with-hard-coded-input-and-JSON-EL.bpmn20.xml)](src/test/resources/processes/flow-with-hard-coded-input-and-JSON-EL.bpmn20.xml)

See [com.example.flowable.demo.service.OrchestratorFlowServiceTest](com.example.flowable.demo.service.OrchestratorFlowServiceTest
) for failing unit tests. 

HttpTask Json EL
```
   <flowable:field name="requestUrl">
          <flowable:expression><![CDATA[http://localhost:80/leefsituatie?brpID=${brpHttpTaskResponse.get(0).brpID}]]></flowable:expression>
   </flowable:field>
```
Mock server log
```
clue-json-server_1  | GET /brp?bsn=1 200 0.351 ms - 61
clue-json-server_1  | GET /leefsituatie?brpID=%7BbrpHttpTaskResponse.get(0).brpID%7D 200 0.353 ms - 2
```

As you can see the JSon EL is not evaluated in the embedded version.


[Dependency analyse](doc/dependency-analyse)
- [all-in-one jars](doc/dependency-analyse/all-in-one.jars.txt)
- [embedded jars](doc/dependency-analyse/all-in-one.jars.txt)
