import React, { useState, useEffect } from "react";
import { subscriptionService } from "../services/subscription-service";
import Header from "./Header";
import { toast } from "react-toastify";
import { Button, Table, Modal } from "react-bootstrap";
import { SUBSCRIPTION_STATUS } from "../constants";
import Select from "react-select";

const Subscriptions = () => {
  const [subscriptions, setSubscriptions] = useState([]);
  const [subscriptionsToShow, setSubscriptionsToShow] = useState([]);
  const [subscriptionsStatus, setSubscriptionsStatus] = useState({
    value: "ALL",
    label: "ALL",
  });

  useEffect(() => {
    getSubscriptions();
  }, []);

  const getSubscriptions = async () => {
    try {
      const response = await subscriptionService.getSubscriptions();
      console.log(response);
      setSubscriptions(response);
      let subscriptionsToShowTemp = response;
      setSubscriptionsToShow(subscriptionsToShowTemp);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const filterSubscriptions = (status) => {
    if (status === "ALL") {
      setSubscriptionsToShow(subscriptions);
    } else {
      let subscriptionsToShowTemp = subscriptions.filter(
        (o) => o.status === status
      );
      setSubscriptionsToShow(subscriptionsToShowTemp);
    }
  };

  return (
    <div>
      <Header />
      <h2>Subscriptions</h2>
      <div className="ml-auto mr-auto" style={{ width: "10%" }}>
        Status:{" "}
        <Select
          value={subscriptionsStatus}
          onChange={(selectedValue) => {
            console.log(selectedValue);
            setSubscriptionsStatus(selectedValue);
            filterSubscriptions(selectedValue.value);
          }}
          options={SUBSCRIPTION_STATUS}
        />
      </div>

      <div style={{ width: "60%" }} className="ml-auto mr-auto mb-2">
        {subscriptionsToShow.map((subscription, index) => {
          return (
            <div
              key={subscription.id}
              style={{
                marginTop: "2em",
                paddingBottom: "1em",
                backgroundColor: "#bdbbbb",
                textAlign: "left",
              }}
            >
              <h5 className="mt-2 mb-2 text-center">
                Subscription to "{subscription.merchantName}"
              </h5>
              <Table
                className="ml-auto mr-auto"
                style={{
                  width: "98%",
                }}
                bordered
              >
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Created At:</th>
                    <th>Status</th>
                    <th>Frequency</th>
                    <th>Cycles</th>
                    <th>Price</th>
                    <th>Discount</th>
                    {subscription.status === "CREATED" && (
                      <th>Expiration Date</th>
                    )}
                  </tr>
                </thead>
                <tbody>
                  <tr key={subscription.id}>
                    <td>{index + 1}</td>
                    <td>
                      {subscription.createdAt[2] +
                        "/" +
                        subscription.createdAt[1] +
                        "/" +
                        subscription.createdAt[0] +
                        " " +
                        subscription.createdAt[3] +
                        ":" +
                        subscription.createdAt[4] +
                        ":" +
                        subscription.createdAt[5]}
                    </td>
                    <td>{subscription.status}</td>
                    <td>{subscription.frequency}</td>
                    <td>{subscription.cycles}</td>
                    <td>{subscription.price}&#36;</td>
                    <td>{subscription.discount}%</td>
                    {subscription.status === "CREATED" && (
                      <td>
                        {subscription.expirationDate[2] +
                          "/" +
                          subscription.expirationDate[1] +
                          "/" +
                          subscription.expirationDate[0]}
                      </td>
                    )}
                  </tr>
                </tbody>
              </Table>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Subscriptions;
