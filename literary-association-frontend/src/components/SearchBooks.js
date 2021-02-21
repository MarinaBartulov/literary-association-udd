import { Button, Modal, Form } from "react-bootstrap";
import React, { useState } from "react";
import Header from "./Header";
import { bookService } from "../services/book-service";
import { searchService } from "../services/search-service";
import { indexerService } from "../services/indexer-service";

import { shoppingCartService } from "../services/shopping-cart-service";
import { toast } from "react-toastify";
import { AMOUNT, FIELDS, OPERATORS } from "../constants";
import Select from "react-dropdown-select";
import Select1 from "react-select";

const SearchBooks = () => {
  const [books, setBooks] = useState([]);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile?filePath=";
  const [selectedAmount, setSelectedAmount] = useState({
    value: 1,
    label: "1",
  });
  const [showBasic, setShowBasic] = useState(true);
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [showResults, setShowResults] = useState(false);
  // BASIC SEARCH
  const [selectedField, setSelectedField] = useState("");
  const [query, setQuery] = useState("");
  const [phrase, setPhrase] = useState(false);
  // ADVANCED SEARCH
  const [advancedQueries, setAdvancedQueries] = useState([]);

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

  const addField = () => {
    let advancedQuery = { field: "", operator: "", query: "", phrase: false };
    setAdvancedQueries((oldArray) => [...oldArray, advancedQuery]);
  };

  const basicSearch = async (event) => {
    event.preventDefault();

    let payload = { field: selectedField, query: query, phrase: phrase };
    try {
      const response = await searchService.basicSearch(payload);
      setBooks(response);
      setShowResults(true);
    } catch (error) {
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const advancedSearch = async (event) => {
    event.preventDefault();
    if (advancedQueries.length === 0) {
      toast.error("You have to add at least one condition for search.", {
        hideProgressBar: true,
      });
      return;
    }
    console.log(advancedQueries);
    let payload = advancedQueries;
    try {
      const response = await searchService.advancedSearch(payload);
      setBooks(response);
      setShowResults(true);
    } catch (error) {
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

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
        onClick={() => {
          setShowBasic(true);
          setShowAdvanced(false);
          setShowResults(false);
          setBooks([]);
        }}
      >
        Basic search
      </Button>
      <Button
        className="ml-2"
        style={{ width: "9em" }}
        size="sm"
        onClick={() => {
          setShowAdvanced(true);
          setShowBasic(false);
          setShowResults(false);
          setAdvancedQueries([]);
          setBooks([]);
        }}
      >
        Advanced search
      </Button>
      {/***************************************** BASIC SEARCH *********************************************/}
      {showBasic && (
        <div className="mr-auto ml-auto" style={{ width: "50%" }}>
          <h6 className="mt-1">Basic search</h6>
          <Form onSubmit={basicSearch}>
            <div className="row">
              <div className="col-12">
                <Select
                  placeholder="Search by..."
                  options={FIELDS}
                  style={{ backgroundColor: "white", marginBottom: "1em" }}
                  onChange={(values) => {
                    setSelectedField(values[0].value);
                    console.log(values[0].value);
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-10">
                <Form.Group>
                  <Form.Control
                    type="text"
                    onChange={(e) => {
                      const { value } = e.target;
                      setQuery(value);
                    }}
                    placeholder="Enter query"
                    required
                  />
                </Form.Group>
              </div>
              <div className="col-2">
                <Form.Group style={{ marginTop: "0.5em" }}>
                  <Form.Check
                    type="checkbox"
                    onChange={(e) => {
                      setPhrase(e.target.checked);
                    }}
                    label="Phrase?"
                  />
                </Form.Group>
              </div>
            </div>
            <Button
              variant="success"
              type="submit"
              className="mb-1 mt-1 ml-2"
              style={{ width: "7em", borderRadius: "2em" }}
            >
              Search
            </Button>
          </Form>
        </div>
      )}
      {/***************************************** ADVANCED SEARCH ******************************************/}
      {showAdvanced && (
        <div className="mr-auto ml-auto" style={{ width: "60%" }}>
          <h6 className="mt-1">Advanced search</h6>
          <Form onSubmit={advancedSearch}>
            {advancedQueries.map((advancedQuery, i) => {
              return (
                <div key={i}>
                  <div className="row">
                    <div className="col-5">
                      <Select
                        placeholder="Operator"
                        required
                        options={OPERATORS}
                        style={{
                          backgroundColor: "white",
                          marginBottom: "1em",
                        }}
                        onChange={(values) => {
                          const newAdvancedQueries = [...advancedQueries];
                          newAdvancedQueries[i].operator = values[0].value;
                          setAdvancedQueries(newAdvancedQueries);
                        }}
                      />
                    </div>
                    <div className="col-7">
                      <Select
                        placeholder="Search by..."
                        required
                        options={FIELDS}
                        style={{
                          backgroundColor: "white",
                          marginBottom: "1em",
                        }}
                        onChange={(values) => {
                          const newAdvancedQueries = [...advancedQueries];
                          newAdvancedQueries[i].field = values[0].value;
                          setAdvancedQueries(newAdvancedQueries);
                        }}
                      />
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-10">
                      <Form.Group>
                        <Form.Control
                          type="text"
                          onChange={(e) => {
                            const { value } = e.target;
                            const newAdvancedQueries = [...advancedQueries];
                            newAdvancedQueries[i].query = value;
                            setAdvancedQueries(newAdvancedQueries);
                          }}
                          placeholder="Enter query"
                          required
                        />
                      </Form.Group>
                    </div>
                    <div className="col-2">
                      <Form.Group style={{ marginTop: "0.5em" }}>
                        <Form.Check
                          type="checkbox"
                          onChange={(e) => {
                            const newAdvancedQueries = [...advancedQueries];
                            newAdvancedQueries[i].phrase = e.target.checked;
                            setAdvancedQueries(newAdvancedQueries);
                          }}
                          label="Phrase?"
                        />
                      </Form.Group>
                    </div>
                  </div>
                </div>
              );
            })}
            <Button
              variant="primary"
              type="button"
              className="mb-1 mt-1 ml-2"
              onClick={addField}
              style={{ width: "7em", borderRadius: "2em" }}
            >
              Add field
            </Button>
            <Button
              variant="success"
              type="submit"
              className="mb-1 mt-1 ml-2"
              style={{ width: "7em", borderRadius: "2em" }}
            >
              Search
            </Button>
            <Button
              variant="danger"
              type="button"
              className="mb-1 mt-1 ml-2"
              onClick={() => {
                setAdvancedQueries([]);
                setShowResults(false);
                setBooks([]);
              }}
              style={{ width: "7em", borderRadius: "2em" }}
            >
              Reset all
            </Button>
          </Form>
        </div>
      )}

      {/***************************************** RESULTS ******************************************/}
      {showResults && (
        <div style={{ width: "60%" }} className="ml-auto mr-auto">
          <h6>Results:</h6>
          {books.map((book, index) => {
            return (
              <div
                key={index}
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
                <h6>Highlight:</h6>
                <div
                  dangerouslySetInnerHTML={{ __html: book.highlights }}
                ></div>
                {book.openAccess && (
                  <Button
                    style={{ borderRadius: "2em", width: "7em" }}
                    className="mb-1"
                    variant="primary"
                    onClick={() => {}}
                  >
                    <a style={{ color: "white" }} href={downloadUrl + book.pdf}>
                      Download
                    </a>
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
      )}

      {/***************************************** DETAILS ******************************************/}
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
            <Select1
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
