import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { IGetStoreTypesResponse } from "../../../src/rest/http-message-interfaces/response-interfaces";
import { GetStoreTypesRepo } from "../../../src/rest/repositories/get-store-types-repo";
import { RestApiError } from "../../../src/rest/RestApiError";

const mock = new MockAdapter(axios);
const repo = new GetStoreTypesRepo();

afterEach(() => mock.resetHandlers());

describe("Get Store Types Repository", () => {
  it("should return a list of store type config names", async () => {
    const apiResponse: IGetStoreTypesResponse = {
      storeTypes: ["accumulo-big", "accumulo-small"],
    };
    mock.onGet("/storetypes").reply(200, apiResponse);

    const actual: string[] = await repo.get();

    const expected = ["accumulo-big", "accumulo-small"];
    expect(actual).toEqual(expected);
  });

  it("should throw RestApiError with correct status message when no response body", async () => {
    mock.onGet("/storetypes").reply(404);

    await expect(repo.get()).rejects.toEqual(
      new RestApiError("Error Code 404", "Not Found")
    );
  });

  it("should throw RestApiError with title and detail from response body", async () => {
    mock
      .onGet("/storetypes")
      .reply(403, { title: "Forbidden", detail: "Graph is invalid" });

    await expect(repo.get()).rejects.toEqual(
      new RestApiError("Forbidden", "Graph is invalid")
    );
  });

  it("should throw unknown RestApiError when undefined status and body", async () => {
    mock.onGet("/storetypes").reply(0);

    await expect(repo.get()).rejects.toEqual(
      new RestApiError("Unknown Error", "Unable to make request")
    );
  });
});
