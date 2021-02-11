import { toast } from "react-toastify";

class ShoppingCartService {
  getBooksFromCart = () => {
    let currentUserId = localStorage.getItem("currentUserId");
    let cart = JSON.parse(localStorage.getItem("cart"));
    if (cart === null || cart === undefined) {
      cart = {};
    }
    if (cart[currentUserId] === null || cart[currentUserId] === undefined) {
      return [];
    }
    return cart[currentUserId];
  };

  addBookToCart = (book, bookAmount) => {
    let currentUserId = localStorage.getItem("currentUserId");
    let cart = JSON.parse(localStorage.getItem("cart"));
    if (cart === null || cart === undefined) {
      cart = {};
    }
    if (cart[currentUserId] === null || cart[currentUserId] === undefined) {
      cart[currentUserId] = [];
    }
    if (cart[currentUserId].some((c) => c.id === book.id)) {
      toast.error("This book is already in your shopping cart.", {
        hideProgressBar: true,
      });
      return;
    }
    let cartItem = {
      id: book.id,
      title: book.title,
      genre: book.genre,
      writer: book.writer,
      publisher: book.publisher,
      publisherId: book.publisherId,
      price: book.price,
      amount: bookAmount,
    };
    cart[currentUserId].push(cartItem);
    localStorage.setItem("cart", JSON.stringify(cart));
    toast.success("Book has been successfully added to your shopping cart.", {
      hideProgressBar: true,
    });
  };

  removeBookfromCart = (bookId) => {
    let currentUserId = localStorage.getItem("currentUserId");
    let cart = JSON.parse(localStorage.getItem("cart"));
    let storageBooks = cart[currentUserId];
    let books = storageBooks.filter((book) => book.id !== bookId);
    cart[currentUserId] = books;
    localStorage.setItem("cart", JSON.stringify(cart));
  };

  updateAmountForCartItem = (bookId, amount) => {
    let currentUserId = localStorage.getItem("currentUserId");
    let cart = JSON.parse(localStorage.getItem("cart"));
    let storageBooks = cart[currentUserId];
    for (let i = 0; i < storageBooks.length; i++) {
      if (storageBooks[i].id == bookId) {
        storageBooks[i].amount = amount;
        break;
      }
    }
    cart[currentUserId] = storageBooks;
    localStorage.setItem("cart", JSON.stringify(cart));
  };
}

export const shoppingCartService = new ShoppingCartService();
