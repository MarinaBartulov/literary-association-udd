import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class SearchService extends HttpService {
  basicSearch = async (payload) => {
    const response = await this.client.post(ROUTES.SEARCH + "/basic", payload);
    return response.data;
  };

  advancedSearch = async (payload) => {
    const response = await this.client.post(
      ROUTES.SEARCH + "/advanced",
      payload
    );
    return response.data;
  };

  betaReadersSearch = async (payload) => {
    const response = await this.client.get(
      ROUTES.SEARCH + "/betaReaders/" + payload
    );
    return response.data;
  };

  download = async (url) => {
    const response = await this.client.get(
      "/task/downloadFile?filePath=" + url
    );
  };
}

export const searchService = new SearchService();
