import React, { useState, useEffect } from "react";
import { orderService } from "../services/order-service";
import { shoppingCartService } from "../services/shopping-cart-service";
import { bookService } from "../services/book-service";
import { Button, Table, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import Header from "./Header";
import { AMOUNT } from "../constants";
import Select from "react-select";

const ShoppingCart = () => {
  const [cartItemsPublishers, setCartItemsPublishers] = useState({});
  const [publishers, setPublishers] = useState([]);

  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:8080/api/task/downloadFile";

  const getCartItems = () => {
    let booksFromCart = shoppingCartService.getBooksFromCart();
    console.log(booksFromCart);
    //podelim po publisherima
    let tempCartItemsPublishers = {};
    let tempPublishers = [];
    for (let i = 0; i < booksFromCart.length; i++) {
      if (
        tempCartItemsPublishers[booksFromCart[i].publisherId] === undefined ||
        tempCartItemsPublishers[booksFromCart[i].publisherId] === null
      ) {
        tempCartItemsPublishers[booksFromCart[i].publisherId] = {
          cartItems: [],
          total: 0.0,
        };
        tempPublishers.push(booksFromCart[i].publisherId);
      }
      tempCartItemsPublishers[booksFromCart[i].publisherId].cartItems.push(
        booksFromCart[i]
      );
      tempCartItemsPublishers[booksFromCart[i].publisherId].total +=
        booksFromCart[i].amount * booksFromCart[i].price;
    }
    console.log(tempCartItemsPublishers);
    console.log(tempPublishers);
    setPublishers(tempPublishers);
    setCartItemsPublishers(tempCartItemsPublishers);
  };

  const removeFromCart = (id) => {
    shoppingCartService.removeBookfromCart(id);
    getCartItems();
  };

  const updateAmountForCartItem = (id, amount) => {
    shoppingCartService.updateAmountForCartItem(id, amount);
    getCartItems();
  };

  const createOrder = async (merchantId) => {
    let cartItemsPublisher = cartItemsPublishers[merchantId];
    let cartItems = cartItemsPublisher.cartItems;
    let total = cartItemsPublisher.total;
    let books = [];
    for (let i = 0; i < cartItems.length; i++) {
      books.push({ bookId: cartItems[i].id, amount: cartItems[i].amount });
    }

    let payload = {
      books: books,
      total: total,
      merchantId: merchantId,
    };
    try {
      const response = await orderService.createOrder(payload);
      for (let i = 0; i < cartItems.length; i++) {
        shoppingCartService.removeBookfromCart(cartItems[i].id);
      }
      getCartItems();
      toast.success("Order has been successfully created.", {
        hideProgressBar: true,
      });
      setTimeout(() => {
        window.open(response.redirectionURL);
      }, 2000);
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
    getCartItems();
  }, []);

  return (
    <div>
      <Header />
      <h2>Shopping cart</h2>
      <div style={{ width: "60%" }} className="ml-auto mr-auto">
        {publishers.map((publisher) => {
          return (
            <div style={{ marginTop: "2em", backgroundColor: "#bdbbbb" }}>
              <h4 style={{ marginTop: "1em", marginBottom: "1em" }}>
                Publisher:{" "}
                {cartItemsPublishers[publisher].cartItems[0].publisher}
              </h4>
              <Table
                style={{ width: "98%" }}
                className="ml-auto mr-auto"
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
                    <th>Remove from cart</th>
                  </tr>
                </thead>
                <tbody>
                  {cartItemsPublishers[publisher].cartItems.map(
                    (cartItem, i) => {
                      return (
                        <tr>
                          <td>{i + 1}</td>
                          <td>
                            <a
                              href=""
                              onClick={(e) => seeBookDetails(e, cartItem.id)}
                            >
                              {cartItem.title}
                            </a>
                          </td>
                          <td>{cartItem.genre}</td>
                          <td>{cartItem.writer}</td>
                          <td>
                            <Select
                              value={{
                                value: cartItem.amount,
                                label: cartItem.amount,
                              }}
                              onChange={(selectedValue) => {
                                console.log(selectedValue);
                                updateAmountForCartItem(
                                  cartItem.id,
                                  selectedValue.value
                                );
                              }}
                              options={AMOUNT}
                            />
                          </td>
                          <td>{cartItem.price}&#36;</td>
                          <td>
                            <Button
                              style={{ borderRadius: "2em" }}
                              variant="danger"
                              onClick={() => {
                                removeFromCart(cartItem.id);
                              }}
                            >
                              Remove from cart
                            </Button>
                          </td>
                        </tr>
                      );
                    }
                  )}
                </tbody>
              </Table>
              <h4 style={{ textAlign: "right", paddingRight: "1em" }}>
                Total: {cartItemsPublishers[publisher].total}&#36;
              </h4>
              <Button
                style={{ borderRadius: "2em", marginBottom: "1em" }}
                variant="success"
                onClick={() => {
                  createOrder(publisher);
                }}
              >
                Create order
              </Button>
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

export default ShoppingCart;
