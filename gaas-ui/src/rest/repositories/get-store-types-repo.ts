import {IApiResponse, RestClient} from "../clients/rest-client";
import {Config} from "../config";
import {IStoreTypesResponse} from "../http-message-interfaces/response-interfaces";

export class GetStoreTypesRepo {
    public async getAll(): Promise<string[]>{
        const response: IApiResponse<IStoreTypesResponse> =await new RestClient()
            .baseUrl(Config.REACT_APP_KAI_REST_API_HOST)
            .get()
            .storeTypes()
            .execute();
        return response.data;
    }
    
}