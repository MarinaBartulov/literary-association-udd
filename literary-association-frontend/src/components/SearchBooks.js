import { Button, Modal } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import Header from "./Header";
import { bookService } from "../services/book-service";
import { searchService } from "../services/search-service";
import { indexerService } from "../services/indexer-service";

import { shoppingCartService } from "../services/shopping-cart-service";
import { toast } from "react-toastify";
import { AMOUNT } from "../constants";
import Select from "react-select";

const SearchBooks = () => {
  const [books, setBooks] = useState([]);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile?filePath=";
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

  const seeBookDetails = async (event, bookId) => {
    event.preventDefault();
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

  const indexBetaReaders = async () => {
    try {
      const response = await indexerService.indexBetaReaders();
      toast.success("Indexing beta-readers successfully finished.", {
        hideProgressBar: true,
      });
    } catch (error) {
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const indexBooks = async () => {
    try {
      const response = await indexerService.indexBooks();
      toast.success("Indexing books successfully finished.", {
        hideProgressBar: true,
      });
    } catch (error) {
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const basicSearch = () => {};

  const advancedSearch = () => {};

  useEffect(() => {
    getBooks();
  }, []);

  return (
    <div>
      <Header />
      <div style={{ textAlign: "start", width: "95%" }}>
        <Button
          size="sm"
          variant="warning"
          className="ml-2 mt-1"
          onClick={indexBetaReaders}
        >
          Index beta-readers
        </Button>
        <Button
          className="ml-2 mt-1"
          variant="warning"
          size="sm"
          onClick={indexBooks}
        >
          Index books
        </Button>
      </div>
      <div>
        <h2>Search books</h2>
      </div>
      <Button
        className="ml-2"
        style={{ width: "9em" }}
        size="sm"
        onClick={basicSearch}
      >
        Basic search
      </Button>
      <Button
        className="ml-2"
        style={{ width: "9em" }}
        size="sm"
        onClick={advancedSearch}
      >
        Advanced search
      </Button>
      <div style={{ width: "60%" }} className="ml-auto mr-auto">
        {books.map((book) => {
          return (
            <div
              style={{
                backgroundColor: "#bdbbbb",
                textAlign: "left",
              }}
              className="card mr-auto ml-auto mb-2 pl-2"
            >
              <a href="" onClick={(e) => seeBookDetails(e, book.id)}>
                <h3 style={{ textAlign: "center" }}>{book.title}</h3>
              </a>

              <h6>Writer: {book.writer}</h6>
              <h6>Genre: {book.genre}</h6>
              <h6>Price (nepotrebo): {book.price}&#36;</h6>
              <h6>Publisher (nepotrebno): {book.publisher}</h6>
              <h6>Highlight:</h6>
              <p>{book.publisher}</p>
              {book.openAccess && (
                <Button
                  style={{ borderRadius: "2em", width: "7em" }}
                  variant="primary"
                >
                  Download
                </Button>
              )}
              {!book.openAccess && (
                <Button
                  style={{ borderRadius: "2em", width: "7em" }}
                  className="mb-1"
                  variant="success"
                  onClick={() => {
                    addToCart(book, 1);
                  }}
                >
                  Add to cart
                </Button>
              )}
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
