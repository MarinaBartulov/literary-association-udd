import { Button, Modal } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { Table } from "react-bootstrap";
import Header from "./Header";
import { bookService } from "../services/book-service";
import { shoppingCartService } from "../services/shopping-cart-service";
import { toast } from "react-toastify";
import { AMOUNT } from "../constants";
import Select from "react-select";

const SearchBooks = () => {
  const [books, setBooks] = useState([]);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile";
  const [selectedAmount, setSelectedAmount] = useState({
    value: 1,
    label: "1",
  });

  const getBooks = async () => {
    try {
      const response = await bookService.getAllBooks();
      console.log(response);
      setBooks(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const addToCart = async (book, bookAmount) => {
    let currentUserId = localStorage.getItem("currentUserId");
    if (currentUserId === null || currentUserId === undefined) {
      toast.error("You have to login first.", {
        hideProgressBar: true,
      });
      return;
    } else {
      let role = localStorage.getItem("role");
      if (role !== "ROLE_READER") {
        toast.error("Only readers can buy books.", {
          hideProgressBar: true,
        });
        return;
      }
    }
    shoppingCartService.addBookToCart(book, bookAmount);
  };

  const handleCancel = () => {
    setShowDetails(false);
    setSelectedAmount({ value: 1, label: "1" });
  };

  const seeBookDetails = async (bookId) => {
    try {
      const response = await bookService.getBookDetails(bookId);
      console.log(response);
      setBookToShow(response);
      setSelectedAmount({ value: 1, label: "1" });
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
    getBooks();
  }, []);

  return (
    <div>
      <Header />
      <h2>Books</h2>
      <div
        style={{ width: "60%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Genre</th>
              <th>Writer</th>
              <th>Publisher</th>
              <th>Price</th>
              <th>Details</th>
              <th>Add to cart</th>
            </tr>
          </thead>
          <tbody>
            {books.map((book) => {
              return (
                <tr key={book.id}>
                  <td>{book.title}</td>
                  <td>{book.genre}</td>
                  <td>{book.writer}</td>
                  <td>{book.publisher}</td>
                  <td>{book.price}&#36;</td>
                  <td>
                    <Button
                      style={{ borderRadius: "2em" }}
                      variant="primary"
                      onClick={() => {
                        seeBookDetails(book.id);
                      }}
                    >
                      Details
                    </Button>
                  </td>
                  <td>
                    <Button
                      style={{ borderRadius: "2em" }}
                      variant="success"
                      onClick={() => {
                        addToCart(book, 1);
                      }}
                    >
                      Add to cart
                    </Button>
                  </td>
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
            Amount:
            <Select
              value={selectedAmount}
              onChange={(selectedValue) => {
                console.log(selectedValue);
                setSelectedAmount(selectedValue);
              }}
              options={AMOUNT}
            />
            <Button
              style={{ borderRadius: "2em" }}
              variant="success"
              onClick={() => addToCart(bookToShow, selectedAmount.value)}
            >
              Add to cart
            </Button>
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

export default SearchBooks;
