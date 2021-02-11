import React from "react";
import Card from "react-bootstrap/Card";
import PayTest from "./PayTest";
import Header from "./Header";

const Home = () => {
  return (
    <div>
      <Header />
      <div className="homeDiv">
        <Card
          body
          style={{
            width: "25rem",
            margin: "auto",
            backgroundColor: "rgb(207, 97, 64)",
          }}
        >
          <h1>Welcome to Literary Association</h1>
        </Card>
        <PayTest></PayTest>
      </div>
    </div>
  );
};

export default Home;
