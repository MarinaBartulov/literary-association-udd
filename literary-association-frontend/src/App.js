import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import "./App.css";
import ErrorTransaction from "./components/ErrorTransaction";
import FailedTransaction from "./components/FailedTransaction";
import Footer from "./components/Footer";
import Home from "./components/Home";
import RegistrationConfirmation from "./components/RegistrationConfirmation";
import SuccessfulTransaction from "./components/SuccessfulTransaction";
import PayTest from "./components/PayTest";
import Login from "./components/Login";
import BoardMemberPanel from "./components/BoardMemberPanel";

import { ToastContainer } from "react-toastify";
import RegistrationMerchant from "./components/RegistrationMerchant";
import ShoppingCart from "./components/ShoppingCart";
import MembershipApplication from "./components/MembershipApplication";
import TaskList from "./components/TaskList";
import TaskForm from "./components/TaskForm";
import SearchBooks from "./components/SearchBooks";
import Orders from "./components/Orders";
import Subscription from "./components/Subscription";
import Subscriptions from "./components/Subscriptions";
import PurchasedBooks from "./components/PurchasedBooks";

function App() {
  return (
    <Router>
      <div className="App">
        <div className="mainDiv">
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar={true}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
          />
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/home">
              <Home />
            </Route>
            <Route exact path="/registrationConfirmation/:processId/:token">
              <RegistrationConfirmation />
            </Route>
            <Route exact path="/success">
              <SuccessfulTransaction />
            </Route>
            <Route exact path="/failed">
              <FailedTransaction />
            </Route>
            <Route exact path="/error">
              <ErrorTransaction />
            </Route>
            <Route exact path="/payTest">
              <PayTest />
            </Route>
            <Route exact path="/login">
              <Login />
            </Route>
            <Route exact path="/registrationMerchant">
              <RegistrationMerchant />
            </Route>
            <Route exact path="/shoppingCart">
              <ShoppingCart />
            </Route>
            <Route exact path="/allTasks">
              <TaskList />
            </Route>
            <Route exact path="/task/:taskId">
              <TaskForm />
            </Route>
            <Route exact path="/boardMemberPanel">
              <BoardMemberPanel />
            </Route>
            <Route exact path="/membershipApplication/:taskId/:processId">
              <MembershipApplication />
            </Route>
            <Route exact path="/searchBooks">
              <SearchBooks />
            </Route>
            <Route exact path="/orders">
              <Orders />
            </Route>
            <Route exact path="/subscription">
              <Subscription />
            </Route>
            <Route exact path="/subscriptions">
              <Subscriptions />
            </Route>
            <Route exact path="/purchasedBooks">
              <PurchasedBooks />
            </Route>
          </Switch>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
