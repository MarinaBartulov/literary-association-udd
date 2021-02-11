import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class UploadService extends HttpService {
  uploadFile = async (file, processId, counter) => {
    console.log("iz funkcije za upload:");
    console.log(file);
    console.log(processId);
    const formdata = new FormData();
    formdata.append("file", file);
    formdata.append("processId", processId);
    formdata.append("counter", counter);
    const response = await this.client.post(ROUTES.TASK_UPLOAD, formdata);
    return response.data;
  };
}

export const uploadService = new UploadService();
