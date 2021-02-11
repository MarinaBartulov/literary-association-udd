import React, { useState, useEffect } from "react";
import { orderService } from "../services/order-service";
import { bookService } from "../services/book-service";
import Header from "./Header";
import { toast } from "react-toastify";
import { Button, Table, Modal } from "react-bootstrap";
import { ORDER_STATUS } from "../constants";
import Select from "react-select";

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [ordersToShow, setOrdersToShow] = useState([]);
  const [ordersStatus, setOrdersStatus] = useState({
    value: "ALL",
    label: "ALL",
  });
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile";

  const getOrders = async () => {
    try {
      console.log("Ovde");
      const response = await orderService.getOrders();
      console.log(response);
      setOrders(response);
      let ordersToShowTemp = response;
      setOrdersToShow(ordersToShowTemp);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const filterOrders = (status) => {
    if (status === "ALL") {
      setOrdersToShow(orders);
    } else {
      let ordersToShowTemp = orders.filter((o) => o.orderStatus === status);
      setOrdersToShow(ordersToShowTemp);
    }
  };

  const handleCancel = () => {
    setShowDetails(false);
  };

  const seeBookDetails = async (event, bookId) => {
    event.preventDefault();
    try {
      const response = await bookService.getBookDetails(bookId);
      console.log(response);
      setBookToShow(response);
      setShowDetails(true);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  useEffect(() => {
    getOrders();
  }, []);
  return (
    <div>
      <Header />
      <h2>Orders</h2>
      <div className="ml-auto mr-auto" style={{ width: "10%" }}>
        Status:{" "}
        <Select
          value={ordersStatus}
          onChange={(selectedValue) => {
            console.log(selectedValue);
            setOrdersStatus(selectedValue);
            filterOrders(selectedValue.value);
          }}
          options={ORDER_STATUS}
        />
      </div>
      <div style={{ width: "60%" }} className="ml-auto mr-auto mb-2">
        {ordersToShow.map((order, index) => {
          return (
            <div
              key={order.id}
              style={{
                marginTop: "2em",
                backgroundColor: "#bdbbbb",
                textAlign: "left",
              }}
            >
              <h5 className="mt-2 mb-2 text-center">Order #{index + 1}</h5>
              <h5 className="ml-2">Books:</h5>
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
                    <th>Title</th>
                    <th>Genre</th>
                    <th>Writer</th>
                    <th>Amount</th>
                    <th>Price</th>
                  </tr>
                </thead>
                <tbody>
                  {order.books.map((book, i) => {
                    return (
                      <tr key={book.bookId}>
                        <td>{i + 1}</td>
                        <td>
                          <a
                            href=""
                            onClick={(e) => seeBookDetails(e, book.bookId)}
                          >
                            {book.bookTitle}
                          </a>
                        </td>
                        <td>{book.genre}</td>
                        <td>{book.writer}</td>
                        <td>{book.amount}</td>
                        <td>{book.price}&#36;</td>
                      </tr>
                    );
                  })}
                </tbody>
              </Table>
              <p className="ml-2 mb-0">Publisher: {order.merchant}</p>
              <p className="ml-2 mb-0">
                Date created:{" "}
                {order.dateCreated[2] +
                  "/" +
                  order.dateCreated[1] +
                  "/" +
                  order.dateCreated[0] +
                  " " +
                  order.dateCreated[3] +
                  ":" +
                  order.dateCreated[4] +
                  ":" +
                  order.dateCreated[5]}
              </p>
              <p className="ml-2 mb-0">Total amount: {order.total}&#36;</p>
              <p className="ml-2 mb-0">
                Status:{" "}
                <Button
                  variant={
                    order.orderStatus === "INITIATED" ||
                    order.orderStatus === "CREATED"
                      ? "primary"
                      : order.orderStatus === "COMPLETED"
                      ? "success"
                      : "danger"
                  }
                  className="mb-1"
                  disabled
                >
                  {order.orderStatus}
                </Button>
              </p>
            </div>
          );
        })}
      </div>
      {showDetails && (
        <Modal
          size="lg"
          show={showDetails}
          onHide={handleCancel}
          backdrop="static"
          keyboard={false}
        >
          <Modal.Header
            style={{ backgroundColor: "rgb(52, 58, 64)", color: "white" }}
          >
            <Modal.Title style={{ marginLeft: "auto", marginRight: "auto" }}>
              {bookToShow.title}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <h6 style={{ display: "inline" }}>Genre:</h6>
            <span> {bookToShow.genre}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>ISBN:</h6>
            <span> {bookToShow.isbn}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Number of pages:</h6>
            <span> {bookToShow.numOfPages}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Writer:</h6>
            <span> {bookToShow.writer}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Publisher:</h6>
            <span> {bookToShow.publisher}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Publisher's address:</h6>
            <span> {bookToShow.publishersAddress}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Year:</h6>
            <span> {bookToShow.year}</span>
            <br></br>
            {bookToShow.openAccess && (
              <>
                <h6 style={{ display: "inline" }}>Open access:</h6>
                <span>
                  <a href={downloadUrl + bookToShow.pdf}> Download</a>
                </span>
                <br></br>
              </>
            )}
            <h6>Synopsis:</h6>
            <span> {bookToShow.synopsis}</span>
            <br></br>
            <br></br>
            <h5 style={{ display: "inline" }}>
              Price: {bookToShow.price}&#36;
            </h5>
            <br></br>
          </Modal.Body>
          <Modal.Footer>
            <Button
              style={{ borderRadius: "2em" }}
              variant="secondary"
              onClick={handleCancel}
            >
              Cancel
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </div>
  );
};

export default Orders;
