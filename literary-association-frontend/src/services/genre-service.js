import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class GenreService extends HttpService {
  allGenres = async (payload) => {
    try {
      const response = await this.client.get(ROUTES.GENRES);
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const genreService = new GenreService();
