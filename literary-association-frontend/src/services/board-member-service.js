import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class BoardMemberService extends HttpService {
  getAllMembershipApplications = async () => {
    try {
      const response = await this.client.get(
        ROUTES.GET_MEMBERSHIP_APPLICATIONS
      );
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };

  getMembershipApplication = async (id) => {
    try {
      const response = await this.client.get(
        ROUTES.GET_MEMBERSHIP_APPLICATION + "/" + id
      );
      console.log("U get membership application");
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const boardMemberService = new BoardMemberService();
