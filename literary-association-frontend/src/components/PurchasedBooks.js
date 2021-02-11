import React, { useState, useEffect } from "react";
import { orderService } from "../services/order-service";
import { bookService } from "../services/book-service";
import Header from "./Header";
import { toast } from "react-toastify";
import { Button, Table, Modal } from "react-bootstrap";

const PurchasedBooks = () => {
  const [purchasedBooks, setPurchasedBooks] = useState([]);

  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile";

  const getPurchasedBooks = async () => {
    try {
      const response = await orderService.getPurchasedBooks();
      console.log(response);
      setPurchasedBooks(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
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
    getPurchasedBooks();
  }, []);
  return (
    <div>
      <Header />
      <h2>Purchased Books</h2>
      <div
        style={{ width: "60%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto pt-2 pb-2"
      >
        <Table
          className="ml-auto mr-auto mt-2 mb-2"
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
              <th>Publisher</th>
              <th>Writer</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {purchasedBooks.map((book, i) => {
              return (
                <tr key={book.id}>
                  <td>{i + 1}</td>
                  <td>
                    <a href="" onClick={(e) => seeBookDetails(e, book.id)}>
                      {book.title}
                    </a>
                  </td>
                  <td>{book.genre}</td>
                  <td>{book.writer}</td>
                  <td>{book.publisher}</td>
                  <td>{book.price}&#36;</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
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

export default PurchasedBooks;
