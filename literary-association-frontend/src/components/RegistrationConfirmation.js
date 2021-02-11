import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { readerService } from "../services/reader-service";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";
import Header from "./Header";
import Spinner from "react-bootstrap/Spinner";

const RegistrationConfirmation = () => {
  const { processId, token } = useParams();
  const [message, setMessage] = useState("");
  const [showSuccess, setShowSuccess] = useState(false);
  const history = useHistory();
  const [showSpinner, setShowSpinner] = useState(true);

  const activate = async () => {
    try {
      setShowSpinner(true);
      const response = await readerService.activateAccount(processId, token);
      console.log(response);
      setShowSpinner(false);
      setShowSuccess(true);
      setMessage(response);
    } catch (error) {
      setMessage(error.response ? error.response.data : error.message);
      setShowSpinner(false);
    }
  };

  const goToLogin = () => {
    history.push("/login");
  };

  useEffect(() => {
    activate();
  }, []);

  return (
    <div>
      <Header />
      {showSpinner && (
        <div style={{ marginTop: "2em" }}>
          <Spinner animation="border" variant="dark" />
        </div>
      )}
      <h2 style={{ color: showSuccess ? "green" : "red" }}>{message}</h2>
      {showSuccess && (
        <Button className="ml-2" variant="success" onClick={goToLogin}>
          Login
        </Button>
      )}
    </div>
  );
};

export default RegistrationConfirmation;
