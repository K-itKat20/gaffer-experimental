import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { RestApiError } from "../../../src/rest/RestApiError";
import {GetStoreTypesRepo} from "../../../src/rest/repositories/get-store-types-repo";
import {IStoreTypesResponse} from "../../../src/rest/http-message-interfaces/response-interfaces";

const mock = new MockAdapter(axios);
const repo = new GetStoreTypesRepo();

afterEach(() => mock.resetHandlers());

describe("Get All StoreTypes Repo", () => {
    it("should return many store types when api returns many", async () => {
        const apiResponse: IStoreTypesResponse = ["accumuloStore", "mapStore", "federatedStore", "proxyStore"];

        mock.onGet("/storetypes").reply(200, apiResponse);

        const actual: Array<string> = await repo.getAll();

        const expected = ["accumuloStore", "mapStore", "federatedStore", "proxyStore"];
        expect(actual).toEqual(expected);
    });

    it("should return one store types when the api returns one", async () => {
        const apiResponse: IStoreTypesResponse = ["accumuloStore"];

        mock.onGet("/storetypes").reply(200, apiResponse);

        const actual: Array<string> = await repo.getAll();

        const expected = ["accumuloStore"];
        expect(actual).toEqual(expected);
    });
    it("should throw RestApiError with correct status message when no response body", async () => {
        mock.onGet("/storetypes").reply(404);

        await expect(repo.getAll()).rejects.toEqual(new RestApiError("Error Code 404", "Not Found"));
    });
    it("should throw RestApiError with title and detail from response body", async () => {
        mock.onGet("/storetypes").reply(404, { title: "Forbidden", detail: "Kubernetes access denied" });

        await expect(repo.getAll()).rejects.toEqual(new RestApiError("Forbidden", "Kubernetes access denied"));
    });

    it("should throw unknown RestApiError when undefined status and body", async () => {
        mock.onGet("/storetypes").reply(0);

        await expect(repo.getAll()).rejects.toEqual(new RestApiError("Unknown Error", "Unable to make request"));
    });
});