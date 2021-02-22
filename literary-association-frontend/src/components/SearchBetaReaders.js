import React, { useState } from "react";
import { Button, Form, Table } from "react-bootstrap";
import Header from "./Header";
import { searchService } from "../services/search-service";
import { toast } from "react-toastify";

const SearchBetaReaders = () => {
  const [query, setQuery] = useState("");
  const [betaReaders, setBetaReaders] = useState([]);
  const [showResults, setShowResults] = useState(false);

  const search = async (event) => {
    event.preventDefault();

    try {
      const response = await searchService.betaReadersSearch(query);
      console.log(response);
      setBetaReaders(response);
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
      <div className="ml-auto mr-auto" style={{ width: "50%" }}>
        <h2>Search beta-readers</h2>
        <Form onSubmit={search}>
          <Form.Group>
            <Form.Control
              type="text"
              onChange={(e) => {
                const { value } = e.target;
                setQuery(value);
              }}
              placeholder="Enter genre"
              required
            />
          </Form.Group>
          <Button
            variant="success"
            type="submit"
            className="mb-1 mt-1 ml-2"
            style={{ width: "7em", borderRadius: "2em" }}
          >
            Search
          </Button>
        </Form>
        {showResults && (
          <div>
            <h6>Results:</h6>
            <Table
              className="ml-auto mr-auto"
              style={{
                backgroundColor: "#bdbbbb",
                width: "98%",
              }}
              bordered
            >
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Genres</th>
                </tr>
              </thead>
              <tbody>
                {betaReaders.map((br, i) => {
                  return (
                    <tr key={i}>
                      <td>{i + 1}</td>
                      <td>{br.fullName}</td>
                      <td>{br.genres}</td>
                    </tr>
                  );
                })}
              </tbody>
            </Table>
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchBetaReaders;
