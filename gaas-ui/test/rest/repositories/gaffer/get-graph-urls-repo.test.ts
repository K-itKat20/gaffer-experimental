import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { IGetGraphUrlsResponse } from "../../../../src/rest/http-message-interfaces/response-interfaces";
import { RestApiError } from "../../../../src/rest/RestApiError";
import {GetGraphUrlsRepo} from "../../../../src/rest/repositories/gaffer/get-graph-urls-repo";

const mock = new MockAdapter(axios);
const repo = new GetGraphUrlsRepo();

describe("GetGraphUrlsRepo Graph Operation", () => {
    it("should make an execute GetGraphUrlsRepo request to a hosted graph", async () => {
        const apiResponse: IGetGraphUrlsResponse = ["test.url", "test2.url"];
        mock.onPost("/graph/operations/execute").reply(200, apiResponse);

        const actual: string[] = await repo.get("https://www.testURL.com/");

        expect(actual).toEqual(["test.url", "test2.url"]);
    });
    it("should throw RestApiError with correct status message when no response body", async () => {
        mock.onPost("/graph/operations/execute").reply(500);

        await expect(repo.get("https://www.testURL.com/")).rejects.toEqual(
            new RestApiError("Error Code 500", "Internal Server Error")
        );
    });

    it("should throw RestApiError with title and detail from response body", async () => {
        mock.onPost("/graph/operations/execute").reply(403, {
            title: "Forbidden",
            detail: "User does not have permission",
        });

        await expect(repo.get("https://www.testURL.com/")).rejects.toEqual(
            new RestApiError("Forbidden", "User does not have permission")
        );
    });

    it("should throw unknown RestApiError when undefined status and body", async () => {
        mock.onPost("/graph/operations/execute").reply(0);

        await expect(repo.get("https://www.testURL.com/")).rejects.toEqual(
            new RestApiError("Unknown Error", "Unable to make request")
        );
    });
});