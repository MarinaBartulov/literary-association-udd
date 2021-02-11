import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { taskService } from "../services/task-service";
import { literaryWorkService } from "../services/literary-work-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";

const UploadForm = (props) => {
  const [processId, setProcessId] = useState(props.processId);
  const [taskId, setTaskId] = useState(props.taskId);

  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  const history = useHistory();

  var selectedFiles = FileList;
  var data = {};

  const getFormData = async () => {
    try {
      const taskFormData = await taskService.getFormFields(taskId);
      setFormFields(taskFormData.formFields);
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
    getFormData();
  }, []);

  function getMapSize(x) {
    var len = 0;
    for (var count in x) len++;
    return len;
  }

  /*const removeSelectedFiles = () => {
    data = {};
    selectedFiles = null;
    setFilesNumber(0);
  };*/

  const handleChange = (e, formField) => {
    selectedFiles = e.target.files;
    data = {};
    console.log("U HANDLE CHANGE");
    console.log("SELECTED FILES LENGTH: " + selectedFiles.length);

    let length = e.target.files.length;
    if (formField.properties.multiple !== undefined) {
      if (formField.properties.minLengthFiles !== undefined) {
        let minLength = parseInt(formField.properties.minLengthFiles);
        if (length < minLength) {
          toast.error("Required minimum " + minLength + " files", {
            hideProgressBar: true,
          });
          //return;
        }
      }
    }
    // nacin za cuvanje prethodno ucitanih fajlova
    //var counter = getMapSize(data);
    //console.log("COUNTER " + counter);
    //for (let i = counter, j = 0; i < counter + selectedFiles.length; i++, j++) {
    for (let i = 0; i < selectedFiles.length; i++) {
      const id = "pdf" + i;
      const name = e.target.files.item(i).name;
      //const name = e.target.files.item(j).name;
      data[id] = name;

      console.log("FAJL");
      console.log(id);
      console.log(name);
    }
    console.log("Data");
    console.log(data);
  };

  const submitForm = async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    //ovo ne radi, uploaduje i jedan fajl
    if (selectedFiles.length < 2) {
      setValidated(false);
      event.stopPropagation();
    }
    if (form.checkValidity() === false) {
      event.stopPropagation();
    }

    setValidated(true);
    console.log("Data");
    console.log(data);

    console.log("Poziva Upload");
    console.log(selectedFiles);
    for (let i = 0; i < selectedFiles.length; i++) {
      try {
        let file = selectedFiles.item(i);
        console.log(file);
        const response = await literaryWorkService.upload(file, processId, i);
        toast.success(response, {
          hideProgressBar: true,
        });
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        toast.error(error.response ? error.response.data : error.message, {
          hideProgressBar: true,
        });
      }
    }

    const sendData = [];
    for (let f in data) {
      sendData.push({ fieldId: f, fieldValue: data[f] });
    }
    console.log("Send Data");
    console.log(sendData);
    console.log("Poziva Submit");
    try {
      const response = await taskService.submitForm(sendData, taskId);
      console.log(response);
      toast.success(response, {
        hideProgressBar: true,
      });
      history.push("/home");
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  return (
    <div>
      <Form
        validated={validated}
        style={{
          width: "30%",
          margin: "auto",
        }}
        onSubmit={submitForm}
        encType="multipart/form-data"
      >
        {formFields.map((formField) => {
          const { id, label, typeName, properties } = formField;
          return (
            <>
              {typeName === "string" && (
                <Form.Group key={id} controlId={id}>
                  <Form.Label>{label}:</Form.Label>
                  <Form.Control
                    type={
                      properties.email !== undefined
                        ? "email"
                        : properties.password !== undefined
                        ? "password"
                        : properties.file !== undefined
                        ? "file"
                        : "text"
                    }
                    multiple={properties.multiple !== undefined ? true : false}
                    required
                    onChange={(e) => handleChange(e, formField)}
                    placeholder={"Enter " + label}
                    readOnly={formField.validationConstraints.some(
                      (c) => c.name === "readonly"
                    )}
                    minLength={
                      formField.validationConstraints.some(
                        (c) => c.name === "minlength"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "minlength"
                          ).configuration
                        : undefined
                    }
                    maxLength={
                      formField.validationConstraints.some(
                        (c) => c.name === "maxlength"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "maxlength"
                          ).configuration
                        : undefined
                    }
                  />
                </Form.Group>
              )}
            </>
          );
        })}

        <Button
          variant="primary"
          type="submit"
          style={{ marginBottom: "1em", marginTop: "1em" }}
        >
          Submit
        </Button>
      </Form>
    </div>
  );
};

export default UploadForm;
