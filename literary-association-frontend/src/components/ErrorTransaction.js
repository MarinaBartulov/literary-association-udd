import React from "react";
import Card from "react-bootstrap/Card";
import Header from "./Header";

const ErrorTransaction = () => {
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
          <h1>Payment transaction ended with an error!</h1>
          <h4>Please try again.</h4>
        </Card>
      </div>
    </div>
  );
};

export default ErrorTransaction;
