import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import {RestClient} from "../../../src/rest/clients/rest-client";

const mock = new MockAdapter(axios);

describe("RestClient 2** Responses", () => {
    beforeAll(() =>
        mock
            .onGet("/graphs")
            .reply(200, [{graphId: "any-graph", currentStatus: "DEPLOYED"}])
            .onGet("/graphs/graph-1")
            .reply(200, {graphId: "graph-1", currentStatus: "DELETED"})
            .onPost("/graphs", {post: "this"})
            .reply(201)
            .onDelete("/graphs/redundant-graph")
            .reply(202)
    );
    afterAll(() => mock.resetHandlers());

    it("should return status/data when GET is successful", async() => {
        const actual = await new RestClient().create().get().graphs().execute();

        expect(actual).toEqual({
            status: 200,
            data: [
                {
                    graphId: "any-graph",
                    currentStatus: "DEPLOYED",
                },
            ],
        });
    });
    it("should return status/data when GET with a path variable is successful", async() => {
        const actual = await new RestClient().create().get().graphs("graph-1").execute();

        expect(actual).toEqual({
            status: 200,
            data: {
                graphId: "graph-1",
                currentStatus: "DELETED",
            },
        });
    });
    it("should return status when POST with request body is successful", async() => {
        const actual = await new RestClient().create().post().requestBody({post: "this"}).graphs().execute();

        expect(actual).toEqual({
            status: 201,
        });
    });
    it("should return status when DELETE with path variable is successful", async() => {
        const actual = await new RestClient().create().delete().graphs("redundant-graph").execute();

        expect(actual).toEqual({
            status: 202,
        });
    });
});
describe("Gaffer REST API 2** responses", () => {
    beforeAll(() =>
        mock
            .onGet("/graph/status")
            .reply(200, {status: "UP"})
            .onGet("/graph/config/description")
            .reply(200, "test")
            .onGet("/graph/config/graphId")
            .reply(200, "test")
            .onPost("/graph/operations/execute")
            .reply(200, ["test.url", "test2.url", "test3.url"])
    );
    afterAll(() => mock.resetHandlers());
    it("should return the status when GET status is successful", async() => {
        const actual = await new RestClient().create().get().status().execute();

        expect(actual).toEqual({
            status: 200,
            data: {
                status: "UP"
            },
        });
    });
    it("should return the description when GET description is successful", async() => {
        const actual = await new RestClient().create().get().description().execute();

        expect(actual).toEqual({
            status: 200,
            data: "test",
        });
    });
    it("should return the graph id when GET graph id is successful", async() => {
        const actual = await new RestClient().create().get().graphId().execute();

        expect(actual).toEqual({
            status: 200,
            data: "test",
        });
    });
    it("Should return the graph URLs when POST operation - GetGraphUrls is successful", async() => {
        const actual = await new RestClient()
            .baseUrl("test")
            .post()
            .requestBody({
                "class": "uk.gov.gchq.gaffer.federatedstore.operation.FederatedOperationChain",
                "operationChain": {
                    "operations": [{
                        "operation": "uk.gov.gchq.gaffer.operation.impl.generate.GenerateElements",
                        "handler": {
                            "class": "uk.gov.gchq.gaffer.store.operation.handler.generate.GenerateElementsHandler"
                        }
                    }]
                }
            })
            .uri("/graph/operations/execute")
            .execute();
        expect(actual).toEqual({
            status: 200,
            data: ["test.url", "test2.url", "test3.url"],
        });
    });

})
describe("GaaS API 4**/5** Error Responses", () => {
    beforeAll(() =>
        mock
            .onGet("/graphs")
            .reply(400, {
                title: "Validation Failed",
                detail: "Graph ID can not be null",
            })
            .onGet("/graphs/unfindable-graph")
            .reply(404, {title: "Not Found", detail: "Could not find resource"})
            .onPost("/graphs", {request: "not-found"})
            .reply(500, {
                title: "Server Error",
                detail: "Null pointer in back end API",
            })
            .onDelete("/graphs/already-deleted")
            .reply(504, {title: "Server Error", detail: "Timeout"})
            .onGet("/graph/status")
            .reply(404, {title: "Not Found", detail: "Could not find resource"})
            .onGet("/graph/config/description")
            .reply(404, {title: "Not Found", detail: "Could not find resource"})
            .onGet("/graph/config/graphId")
            .reply(404, {title: "Not Found", detail: "Could not find resource"})
    );
    afterAll(() => mock.resetHandlers());

    it("should throw 400 Error Message when api returns 404", async() => {
        try {
            await new RestClient().create().get().graphs().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Validation Failed: Graph ID can not be null");
        }
    });
    it("should throw 404 Error Message when api returns 404 - get graph status", async() => {
        try {
            await new RestClient().create().get().status().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Not Found: Could not find resource");
        }
    });
    it("should throw 404 Error Message when api returns 404 - get graph description", async() => {
        try {
            await new RestClient().create().get().description().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Not Found: Could not find resource");
        }
    });
    it("should throw 404 Error Message when api returns 404 - get graph id", async() => {
        try {
            await new RestClient().create().get().graphId().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Not Found: Could not find resource");
        }
    });
    it("should throw 404 Error Message when api returns 404", async() => {
        try {
            await new RestClient().create().post().requestBody("unfindable-graph").graphs().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Error Code 404: Not Found");
        }
    });
    it("should throw 500 Error Message when api returns 404", async() => {
        try {
            await new RestClient().create().post().requestBody({request: "not-found"}).graphs().execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Server Error: Null pointer in back end API");
        }
    });
    it("should throw 504 Error Message when api returns 404", async() => {
        try {
            await new RestClient().create().delete().graphs("already-deleted").execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Server Error: Timeout");
        }
    });
});

describe("Gaffer REST API 4**/5** Error Responses", () => {
    beforeAll(() =>
        mock
            .onGet("/graph/config/graphid")
            .reply(403, {
                statusCode: 403,
                status: "Forbidden",
                simpleMessage: "User does not have permission to run operation: uk.gov.gchq.gaffer.operation.impl.GetVariables"
            })
            .onPost("/graph/status")
            .reply(404, {
                statusCode: 404
            })
            .onPost("/graph/operations/execute")
            .reply(404, {title: "Not Found", detail: "Could not find resource"})
    );
    afterAll(() => mock.resetHandlers());

    it("should throw Error with simpleMessage when Gaffer API returns error response body", async() => {
        try {
            await new RestClient().create().get().uri("/graph/config/graphid").execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Forbidden: User does not have permission to run operation: uk.gov.gchq.gaffer.operation.impl.GetVariables");
        }
    });
    it("should throw generic status code error message when Gaffer API error response is not an instanceof", async() => {
        try {
            await new RestClient().create().get().uri("/graph/status").execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e.toString()).toBe("Error Code 404: Not Found");
        }
    });
    it("should throw 404 Error Message when a graph returns 404 - POST operation chain ", async() => {
        try {
            await new RestClient()
                .baseUrl("test")
                .post()
                .requestBody({
                    "class": "uk.gov.gchq.gaffer.federatedstore.operation.FederatedOperationChain",
                    "operationChain": {
                        "operations": [{
                            "operation": "uk.gov.gchq.gaffer.operation.impl.generate.GenerateElements",
                            "handler": {
                                "class": "uk.gov.gchq.gaffer.store.operation.handler.generate.GenerateElementsHandler"
                            }
                        }]
                    }
                })
                .uri("/graph/operations/execute")
                .execute();
            throw new Error("Error did not throw");
        } catch (e) {
            expect(e).toEqual({"detail": "Could not find resource", "title": "Not Found"}
            );
        }
    });
});
