import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class IndexerService extends HttpService {
  indexBetaReaders = async () => {
    const response = await this.client.get(ROUTES.INDEX + "/betaReaders");
    return response.data;
  };

  indexBooks = async () => {
    const response = await this.client.get(ROUTES.INDEX + "/books");
    return response.data;
  };
}

export const indexerService = new IndexerService();
