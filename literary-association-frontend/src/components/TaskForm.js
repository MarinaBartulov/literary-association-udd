import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { taskService } from "../services/task-service";
import { uploadService } from "../services/upload-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import Select from "react-dropdown-select";
import { useParams } from "react-router-dom";
import Header from "./Header";

const TaskForm = () => {
  const params = useParams();
  const [processId, setProcessId] = useState("");
  const [taskId, setTaskId] = useState(params.taskId);
  const [taskName, setTaskName] = useState("");
  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  //const [enumValues, setEnumValues] = useState([]);
  const [data, setData] = useState({});
  const history = useHistory();
  const [downloadFiles, setDownloadFiles] = useState({}); //sadrzace linkove za download fajlova
  const [uploadFiles, setUploadFiles] = useState({}); //sadrzace uploadovane fajlove
  const [showForm, setShowForm] = useState(false);

  //dodala
  const [enumFields, setEnumFields] = useState({}); //ovde ce se nalaziti sve vrednosti za enume na formi
  const [minSelectedItems, setMinSelectedItems] = useState({}); //ovde ce se nalaziti za polja koja su multiselect koliko minimalno mora da se selektuje
  const [minSelectedFiles, setMinSelectedFiles] = useState({}); //ovde ce se nalaziti za polja koja su files i multiple koliko minimalno mora da se selektuje

  const getTask = async () => {
    try {
      const taskData = await taskService.getTask(taskId);
      setFormFields(taskData.formFields);
      setTaskName(taskData.name);
      setProcessId(taskData.processId);

      //inicijalnizacija enum polja
      const temp = [];
      const enumFieldsTemp = {};
      for (let f of taskData.formFields) {
        if (f.typeName === "enum") {
          Object.keys(f.type.values).map((id) => {
            temp.push({ id: id, name: f.type.values[id] });
          });
          //break;
          enumFieldsTemp[`${f.id}`] = temp;
        }
      }
      //console.log(temp);
      //setEnumValues(temp);
      setEnumFields(enumFieldsTemp);

      const dataTemp = {};
      const uploadFilesTemp = {};
      const downloadFilesTemp = {};
      const minSelectedItemsTemp = {};
      const minSelectedFilesTemp = {};

      for (let f of taskData.formFields) {
        if (f.typeName === "string") {
          if (f.properties.file === undefined) {
            //ako to nije file, nego sve ostalo string
            if (f.defaultValue === null) {
              dataTemp[`${f.id}`] = "";
            } else {
              dataTemp[`${f.id}`] = f.defaultValue;
            }
          } else {
            //ako jeste file
            //default value ce sadrzati naziv jednog fajla ili ako je multiple moze i vise nazivFajla1|nazivFajla2|
            if (
              f.defaultValue === null ||
              f.properties.readonly === undefined
            ) {
              //ovo znaci da je ovaj form field za upload, i to naznacim u uploadFiles
              dataTemp[`${f.id}`] = ""; //ovde ce se smestiti nazivi fajl/fajlova koji ce se submit u formi
              uploadFilesTemp[`${f.id}`] = []; //ovde ce se smestiti uploadovani fajlovi za to neko odredjeno polje
              if (f.properties.multiple !== undefined) {
                //proverim da li ima minLengthFiles
                if (f.properties.minLengthFiles !== undefined) {
                  minSelectedFilesTemp[`${f.id}`] = parseInt(
                    f.properties.minLengthFiles
                  );
                }
              }
            } else {
              //ako je taj formField za download
              dataTemp[`${f.id}`] = f.defaultValue;
              if (f.properties.multiple === undefined) {
                //samo jedan file je za download
                downloadFilesTemp[`${f.id}`] = [f.defaultValue];
              } else {
                //ako je vise fajlova za download napravim listu njihovih naziva
                let fileNames = f.defaultValue.split("|");
                //fileNames.pop(); //sklonim poslednji jer je on prazan
                downloadFilesTemp[`${f.id}`] = fileNames;
              }
            }
          }
        }
        if (f.typeName === "long") {
          dataTemp[`${f.id}`] = 0;
        }
        if (f.typeName === "boolean") {
          dataTemp[`${f.id}`] = false;
        }
        if (f.typeName === "enum") {
          dataTemp[`${f.id}`] = [];
          if (f.properties.multiselect !== undefined) {
            if (f.properties.minLengthItems !== undefined) {
              minSelectedItemsTemp[`${f.id}`] = parseInt(
                f.properties.minLengthItems
              );
            }
          }
        }
      }

      setData(dataTemp);
      setUploadFiles(uploadFilesTemp);
      setDownloadFiles(downloadFilesTemp);
      setMinSelectedItems(minSelectedItemsTemp);
      setMinSelectedFiles(minSelectedFilesTemp);
      console.log(dataTemp);
      console.log(data);
      console.log(uploadFilesTemp);
      console.log(downloadFilesTemp);
      console.log(minSelectedItems);
      console.log(minSelectedFiles);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
    setShowForm(true);
  };

  useEffect(() => {
    getTask();
  }, []);

  const handleChange = (e) => {
    console.log(e.target);
    const { id, value } = e.target;
    console.log(id);
    console.log(value);
    console.log(data);
    setData((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };

  const handleFileSelect = (e, formField) => {
    console.log(formField);
    console.log(e.target.files);
    let length = e.target.files.length;
    let files = FileList;
    files = e.target.files;
    if (formField.properties.multiple !== undefined) {
      if (formField.properties.minLengthFiles !== undefined) {
        let minLength = parseInt(formField.properties.minLengthFiles);
        if (length < minLength) {
          toast.error("Required minimum " + minLength + " files", {
            hideProgressBar: true,
          });
          return;
        }
      }
    }
    let filesNames = "";
    if (formField.properties.multiple === undefined) {
      filesNames = files.item(0).name;
    } else {
      filesNames = files.item(0).name;
      for (let i = 1; i < length; i++) {
        filesNames += "|" + files.item(i).name;
      }
    }
    setData((prevState) => ({
      ...prevState,
      [formField.id]: filesNames,
    }));

    setUploadFiles((prevState) => ({
      ...prevState,
      [formField.id]: files,
    }));
  };

  const submitForm = async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    }
    setValidated(true);
    console.log(data);
    const sendData = [];

    //prvo uploadujem sve fajlove
    //let filesPath = ""; // ovo putanje gde se cuvaju na backu, i to ce biti u polju kad se submituje

    //prvo proverim da li ima dovoljno selektovanih fajlova
    for (let f in uploadFiles) {
      console.log("Uslo u proveru broja fajlova za uploadovanje");
      console.log(minSelectedFiles);
      if (minSelectedFiles[f] !== undefined) {
        if (uploadFiles[f].length < minSelectedFiles[f]) {
          toast.error("There are not enough selected files.", {
            hideProgressBar: true,
          });
          return;
        }
      }
    }
    //ako ima za sva polja dovoljno uploadovanih onda uploadujem
    for (let f in uploadFiles) {
      console.log("Uslo u uploadovanje");
      for (let i = 0; i < uploadFiles[f].length; i++) {
        console.log("File koji se upload:");
        console.log(uploadFiles[f][i]);
        const response = await uploadService.uploadFile(
          uploadFiles[f][i],
          processId,
          i + 1
        );
        console.log("Putnja uploadovanog fajla: " + response);
        /*if (i !== 0) {
          //uploaduje se vise fajlova
          filesPath += "|" + response;
        } else {
          filesPath = response;
        }*/
      }
      /*setData((prevState) => ({
        ...prevState,
        [f]: filesPath,
      }));*/
    }

    //zatim submitujem formu
    console.log(minSelectedItems);
    for (let f in data) {
      if (minSelectedItems[f] !== undefined) {
        if (data[f].length < minSelectedItems[f]) {
          toast.error("There are not enough selected values.", {
            hideProgressBar: true,
          });
          return;
        }
      }
      sendData.push({ fieldId: f, fieldValue: data[f] });
    }
    console.log("sendData");
    console.log(sendData);

    try {
      const response = await taskService.submitForm(sendData, taskId);
      console.log(response);
      if (response.taskId === null) {
        toast.success("Form successfully submitted.", {
          hideProgressBar: true,
        });
        history.push("/home");
      } else {
        toast.success("Form successfully submitted. This is your next task.", {
          hideProgressBar: true,
        });
        window.open("https://localhost:3000/task/" + response.taskId, "_self");
      }
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
      //ovo se radi da bi se uzeo novi id taska ako dodje do greske u validaciji
      //task na koji se vrati ima drugaciji id
      const response = await taskService.getCurrentTaskIdForProcess(processId);
      if (response != null) {
        setTaskId(response);
      }
    }
  };

  return (
    <div>
      <Header />
      <h2>{taskName}</h2>
      {showForm && (
        <Form
          validated={validated}
          style={{
            width: "30%",
            margin: "auto",
          }}
          onSubmit={submitForm}
        >
          {formFields.map((formField) => {
            const { id, label, typeName, properties, defaultValue } = formField;
            return (
              <>
                {typeName === "string" &&
                  properties.text_area === undefined &&
                  properties.file === undefined && (
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
                        multiple={
                          properties.multiple !== undefined ? true : false
                        }
                        onChange={handleChange}
                        placeholder={"Enter " + label}
                        value={defaultValue == null ? data[id] : defaultValue}
                        required={formField.validationConstraints.some(
                          (c) => c.name === "required"
                        )}
                        readOnly={formField.properties.readonly !== undefined}
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
                {typeName === "string" && properties.text_area !== undefined && (
                  <Form.Group key={id} controlId={id}>
                    <Form.Label>{label}:</Form.Label>
                    <Form.Control
                      as="textarea"
                      onChange={handleChange}
                      placeholder={"Enter " + label}
                      required={formField.validationConstraints.some(
                        (c) => c.name === "required"
                      )}
                      value={defaultValue == null ? data[id] : defaultValue}
                      readOnly={formField.properties.readonly !== undefined}
                      rows={5}
                    />
                  </Form.Group>
                )}
                {typeName === "string" &&
                  properties.file !== undefined &&
                  properties.readonly === undefined && (
                    <Form.Group key={id} controlId={id}>
                      <Form.File
                        required={formField.validationConstraints.some(
                          (c) => c.name === "required"
                        )}
                        label={label}
                        onChange={(e) => handleFileSelect(e, formField)}
                        multiple={
                          properties.multiple !== undefined ? true : false
                        }
                      />
                    </Form.Group>
                  )}
                {typeName === "string" &&
                  properties.file !== undefined &&
                  properties.readonly !== undefined &&
                  properties.multiple === undefined && (
                    <>
                      <h5>{label}:</h5>
                      <a href={defaultValue}>Download document</a>
                    </>
                  )}
                {typeName === "string" &&
                  properties.file !== undefined &&
                  properties.readonly !== undefined &&
                  properties.multiple !== undefined && (
                    <>
                      <h5>{label}:</h5>
                      {downloadFiles[id].map((url, i) => {
                        return (
                          <>
                            <a href={url}>Download document{i + 1}</a>
                            <br></br>
                          </>
                        );
                      })}
                    </>
                  )}
                {typeName === "long" && (
                  <Form.Group key={id} controlId={id}>
                    <Form.Label>{label}:</Form.Label>
                    <Form.Control
                      type="number"
                      onChange={handleChange}
                      placeholder={"Enter " + label}
                      required={formField.validationConstraints.some(
                        (c) => c.name === "required"
                      )}
                      value={defaultValue == null ? data[id] : defaultValue}
                      readOnly={formField.properties.readonly !== undefined}
                      min={
                        formField.validationConstraints.some(
                          (c) => c.name === "min"
                        )
                          ? formField.validationConstraints.find(
                              (c) => c.name === "min"
                            ).configuration
                          : undefined
                      }
                      max={
                        formField.validationConstraints.some(
                          (c) => c.name === "max"
                        )
                          ? formField.validationConstraints.find(
                              (c) => c.name === "max"
                            ).configuration
                          : undefined
                      }
                    />
                  </Form.Group>
                )}
                {typeName === "boolean" && (
                  <Form.Group key={id} controlId={id}>
                    <Form.Check
                      type="checkbox"
                      onChange={(e) => {
                        setData((prevState) => ({
                          ...prevState,
                          [formField.id]: e.target.checked,
                        }));
                      }}
                      required={formField.validationConstraints.some(
                        (c) => c.name === "required"
                      )}
                      readOnly={formField.properties.readonly !== undefined}
                      label={label}
                    />
                  </Form.Group>
                )}
                {typeName === "enum" && (
                  <>
                    <label key={"label-" + id}>{label}</label>
                    <Select
                      key={id}
                      placeholder={"Select " + label}
                      multi={properties.multiselect !== undefined}
                      required={formField.validationConstraints.some(
                        (c) => c.name === "required"
                      )}
                      readOnly={formField.properties.readonly !== undefined}
                      options={enumFields[id]}
                      style={{ backgroundColor: "white", marginBottom: "1em" }}
                      labelField="name"
                      valueField="id"
                      onChange={(values) => {
                        if (properties.multiselect !== undefined) {
                          console.log(values.map((v) => v.id));
                          setData((prevState) => ({
                            ...prevState,
                            [formField.id]: values.map((v) => v.id), //ovde je pisalo v.name
                          }));
                        } else {
                          console.log(values[0].id); //ovde je pisalo values[0].name
                          setData((prevState) => ({
                            ...prevState,
                            [formField.id]: values[0].id,
                          }));
                        }
                      }}
                    />
                  </>
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
      )}
    </div>
  );
};

export default TaskForm;
