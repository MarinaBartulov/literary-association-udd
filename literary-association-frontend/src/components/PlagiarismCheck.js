import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";
import Header from "./Header";
import { searchService } from "../services/search-service";
import { toast } from "react-toastify";

const PlagiarismCheck = () => {
  const [uploadedFile, setUploadedFile] = useState(null);
  const [plagiats, setPlagiats] = useState([]);
  const [showResults, setShowResults] = useState(false);

  const checkPlagiarism = async (e) => {
    e.preventDefault();
    try {
      const response = await searchService.checkPlagiarism(uploadedFile);
      console.log("Response: " + response);
      let urls = response.split("|");
      let plagiats = [];
      for (let i = 0; i < urls.length; i++) {
        let url = urls[i];
        let fileNameArr = urls[i].split("/");
        let fileName = fileNameArr[fileNameArr.length - 1];
        plagiats.push({ url: url, fileName: fileName });
      }
      console.log(plagiats);
      setPlagiats(plagiats);
      setShowResults(true);
    } catch (error) {
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const handleFileSelect = (e) => {
    let files = FileList;
    files = e.target.files;
    console.log(files[0]);
    setUploadedFile(files[0]);
  };
  return (
    <div>
      <Header />
      <div className="ml-auto mr-auto" style={{ width: "50%" }}>
        <h2>Plagiarism check</h2>
        <h5>Upload document to get potential plagiats</h5>
        <Form
          style={{
            width: "40%",
            marginLeft: "auto",
            marginRight: "auto",
          }}
          onSubmit={checkPlagiarism}
        >
          <Form.Group>
            <Form.File
              required
              label="Upload file"
              onChange={handleFileSelect}
            />
          </Form.Group>
          <Button
            variant="primary"
            type="submit"
            style={{ marginBottom: "1em", marginTop: "1em" }}
          >
            Upload
          </Button>
        </Form>
        {showResults && (
          <>
            <h5>Potential plagiats:</h5>
            {plagiats.map((plagiat, i) => {
              return (
                <div key={i}>
                  <a href={plagiat.url}>
                    Download document{i + 1} - {plagiat.fileName}
                  </a>
                </div>
              );
            })}
          </>
        )}
      </div>
    </div>
  );
};

export default PlagiarismCheck;
