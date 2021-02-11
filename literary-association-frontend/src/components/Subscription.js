import React, { useState, useEffect } from "react";
import { merchantService } from "../services/merchant-service";
import { subscriptionService } from "../services/subscription-service";

import { toast } from "react-toastify";
import { Table, Modal, Button } from "react-bootstrap";
import Header from "./Header";

const Subscription = () => {
  const [merchants, setMerchants] = useState([]);
  const [merchantId, setMerchantId] = useState();
  const [billingPlans, setBillingPlans] = useState([]);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    getActiveMerchants();
  }, []);

  const getActiveMerchants = async () => {
    try {
      const response = await merchantService.getActiveMerchants();
      console.log(response);
      setMerchants(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const openModal = async (merchantId) => {
    console.log(merchantId);

    try {
      const response = await subscriptionService.getMerchantBillingPlans(
        merchantId
      );
      setBillingPlans(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
    setMerchantId(merchantId);
    setShowModal(true);
  };

  const subscribe = async (billingPlanId) => {
    console.log(billingPlanId);

    let payload = {
      billingPlanId: billingPlanId,
      currency: "USD",
      merchantId: merchantId,
    };

    try {
      const response = await subscriptionService.subscribe(payload);
      window.open(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const cancelModal = () => {
    setShowModal(false);
  };

  return (
    <div>
      <Header />
      <h1>Create subscription</h1>
      <h3> Subscribe to publisher and get discount on every order you make </h3>
      <div
        style={{ width: "40%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>Publisher</th>
              <th>Subscribe</th>
            </tr>
          </thead>
          <tbody>
            {merchants.map((merchant) => {
              return (
                <tr key={merchant.id}>
                  <td>{merchant.name}</td>
                  <td>
                    <Button
                      variant="dark"
                      onClick={() => {
                        openModal(merchant.id);
                      }}
                    >
                      Subscribe
                    </Button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </div>

      <Modal
        show={showModal}
        onHide={cancelModal}
        backdrop="static"
        keyboard={false}
        centered
      >
        <Modal.Header>
          <Modal.Title>Choose Billing Plan</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Table>
            <thead>
              <tr>
                <th> Discount </th>
                <th> Frequency </th>
                <th> Cycles </th>
                <th> Price </th>
                <th> Subscribe </th>
              </tr>
            </thead>
            <tbody>
              {billingPlans.map((billingPlan) => {
                return (
                  <tr key={billingPlan.id}>
                    <td>{billingPlan.discount} %</td>
                    <td>{billingPlan.frequency}</td>
                    <td>{billingPlan.cyclesNumber}</td>
                    <td>{billingPlan.price} $</td>
                    <td>
                      <Button
                        variant="dark"
                        onClick={() => {
                          subscribe(billingPlan.id);
                        }}
                      >
                        Subscribe
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </Table>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={cancelModal}>
            Cancel
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default Subscription;
