import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class LiteraryWorkService extends HttpService {
  upload = async (file, processId, counter) => {
    const formdata = new FormData();
    formdata.append("file", file);
    formdata.append("processId", processId);
    formdata.append("counter", counter);

    const response = await this.client.post(
      ROUTES.LITERARY_WORK_UPLOAD,
      formdata
    );
    console.log("Odgovor iz upload literary work");
    console.log(response.data);
    return response.data;
  };
}

export const literaryWorkService = new LiteraryWorkService();
