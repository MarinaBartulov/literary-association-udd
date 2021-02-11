import React, { useState } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import { merchantService } from "../services/merchant-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import Header from "./Header";

const RegistrationMerchant = () => {
  const formik = useFormik({
    initialValues: {
      name: "",
      email: "",
    },
    validationSchema: Yup.object({
      name: Yup.string()
        .max(50, "Must be 20 characters or less")
        .required("Required"),
      email: Yup.string().required("Required").email("Invalid email address"),
    }),
    onSubmit: async (values, { resetForm }) => {
      try {
        const response = await merchantService.registerMerchant(values);
        console.log(response);
        resetForm();
        toast.success("New merchant successfully registered.", {
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
    },
  });

  return (
    <div>
      <Header />
      <div className="pt-5">
        <div
          className="card mr-auto ml-auto mt-5"
          style={{
            width: "30%",
            backgroundColor: "#bdbbbb",
          }}
        >
          <h2 className="card-title">Register a new merchant</h2>
          <form
            onSubmit={formik.handleSubmit}
            style={{ width: "90%", margin: "auto" }}
          >
            <div className="form-group">
              <label htmlFor="name">Name:</label>
              <input
                className="form-control"
                id="name"
                name="name"
                type="text"
                placeholder="Enter name"
                onChange={formik.handleChange}
                value={formik.values.name}
              />
              {formik.touched.name && formik.errors.name ? (
                <p style={{ color: "red" }}>{formik.errors.name}</p>
              ) : null}
            </div>
            <div className="form-group">
              <label htmlFor="email">Email:</label>
              <input
                className="form-control"
                id="email"
                name="email"
                type="email"
                placeholder="Enter email"
                onChange={formik.handleChange}
                value={formik.values.email}
              />
              {formik.touched.email && formik.errors.email ? (
                <p style={{ color: "red" }}>{formik.errors.email}</p>
              ) : null}
            </div>
            <button type="submit" className="btn btn-primary mb-1">
              Submit
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegistrationMerchant;
