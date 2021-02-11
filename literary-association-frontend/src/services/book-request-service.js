import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class BookRequestService extends HttpService {
  startBookPublishing = async () => {
    try {
      const response = await this.client.get(
        ROUTES.BOOK_REQUEST_START_PUBLISHING
      );
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const bookRequestService = new BookRequestService();
