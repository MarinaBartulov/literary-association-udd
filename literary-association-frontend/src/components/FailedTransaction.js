import React from "react";
import Card from "react-bootstrap/Card";
import Header from "./Header";

const FailedTransaction = () => {
  return (
    <div>
      <Header />
      <div className="homeDiv">
        <Card
          body
          style={{
            width: "32rem",
            margin: "auto",
            backgroundColor: "red",
          }}
        >
          <h1>Payment transaction failed!</h1>
          <h4>Please try again.</h4>
        </Card>
      </div>
    </div>
  );
};

export default FailedTransaction;
