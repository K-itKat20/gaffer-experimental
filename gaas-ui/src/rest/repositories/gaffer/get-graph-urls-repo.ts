import {RestClient} from "../../clients/rest-client";

export class GetGraphUrlsRepo {
    public async get(graphHost: string): Promise<Array<string>> {
        //TODO: GetUrlOperation operation name and class
        const getGraphUrlsRequestBody = {
            class: "uk.gov.gchq.gaffer.federatedstore.operation.FederatedOperationChain",
            operationChain: {
                operations: [{
                    operation: "uk.gov.gchq.gaffer.operation.impl.generate.GenerateElements",
                    handler: {
                        class: "uk.gov.gchq.gaffer.store.operation.handler.generate.GenerateElementsHandler"
                    }
                }]
            }
        };
        const response = await new RestClient()
            .baseUrl(graphHost)
            .post()
            .requestBody(getGraphUrlsRequestBody)
            .uri("/graph/operations/execute")
            .execute();

        return response.data;
    }

}