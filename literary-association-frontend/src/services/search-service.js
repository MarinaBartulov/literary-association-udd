import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class SearchService extends HttpService {
  createOrder = async (payload) => {
    const response = await this.client.post(ROUTES.ORDER, payload);
    return response.data;
  };
}

export const searchService = new SearchService();
