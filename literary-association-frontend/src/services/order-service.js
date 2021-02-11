import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class OrderService extends HttpService {
  createOrder = async (payload) => {
    const response = await this.client.post(ROUTES.ORDER, payload);
    return response.data;
  };

  getOrders = async () => {
    const response = await this.client.get(ROUTES.ORDER);
    return response.data;
  };

  getPurchasedBooks = async () => {
    const response = await this.client.get(ROUTES.ORDER + "/purchasedBooks");
    return response.data;
  };
}

export const orderService = new OrderService();
