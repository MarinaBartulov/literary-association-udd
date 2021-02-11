import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class ReaderService extends HttpService {
  startReg = async () => {
    const response = await this.client.get(ROUTES.READER_START_REG);
    console.log(response.data);
    return response.data;
  };

  // regFormFields = async (taskId) => {
  //   const response = await this.client.get(
  //     ROUTES.READER_REG_FORM + "/" + taskId
  //   );
  //   console.log(response.data);
  //   return response.data;
  // };

  // regReader = async (payload, taskId) => {
  //   const response = await this.client.post(
  //     ROUTES.READER_REGISTER + "/" + taskId,
  //     payload
  //   );
  //   console.log(response.data);
  //   return response.data;
  // };

  activateAccount = async (processId, token) => {
    const response = await this.client.put(
      ROUTES.READER_ACTIVATION + "/" + processId + "/" + token
    );
    console.log(response.data);
    return response.data;
  };
}

export const readerService = new ReaderService();
