import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TaskService extends HttpService {
  getFormFields = async (taskId) => {
    const response = await this.client.get(
      ROUTES.TASK_FORM_FIELDS + "/" + taskId
    );
    console.log(response.data);
    return response.data;
  };

  submitForm = async (payload, taskId) => {
    const response = await this.client.post(
      ROUTES.TASK_SUBMIT_FORM + "/" + taskId,
      payload
    );
    console.log(response.data);
    return response.data;
  };

  getAllTasks = async () => {
    const response = await this.client.get(ROUTES.TASK);
    console.log(response.data);
    return response.data;
  };

  getTask = async (taskId) => {
    const response = await this.client.get(ROUTES.TASK + "/" + taskId);
    console.log(response.data);
    return response.data;
  };

  getCurrentTaskForProcess = async (processId) => {
    const response = await this.client.get(
      ROUTES.TASK + "/process/" + processId
    );
    console.log(response.data);
    return response.data;
  };

  //vraca samo id ako pukne validacija
  getCurrentTaskIdForProcess = async (processId) => {
    const response = await this.client.get(
      ROUTES.TASK + "/taskId/" + processId
    );
    console.log(response.data);
    return response.data;
  };

  getAssigneesTaskId = async (processId) => {
    const response = await this.client.get(ROUTES.TASK_ID + "/" + processId);
    console.log("Odgovor iz get assignees task id");
    return response.data;
  };
}

export const taskService = new TaskService();
