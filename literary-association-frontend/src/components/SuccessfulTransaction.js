import React from "react";
import Card from "react-bootstrap/Card";
import Header from "./Header";

const SuccessfulTransaction = () => {
  return (
    <div>
      <Header />
      <div className="homeDiv">
        <Card
          body
          style={{
            width: "32rem",
            margin: "auto",
            backgroundColor: "green",
          }}
        >
          <h1>Successful payment transaction!</h1>
        </Card>
      </div>
    </div>
  );
};

export default SuccessfulTransaction;
