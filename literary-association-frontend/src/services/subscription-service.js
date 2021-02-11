import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class SubscriptionService extends HttpService {
  getMerchantBillingPlans = async (payload) => {
    const response = await this.client.get(
      ROUTES.BILLING_PLANS + "/" + payload
    );
    console.log(response);
    return response.data;
  };

  subscribe = async (payload) => {
    const { data } = await this.client.post(ROUTES.SUBSCRIBE, payload);
    console.log(data);

    return data;
  };

  getSubscriptions = async () => {
    const response = await this.client.get(ROUTES.SUBSCRIPTIONS);
    return response.data;
  };
}

export const subscriptionService = new SubscriptionService();
