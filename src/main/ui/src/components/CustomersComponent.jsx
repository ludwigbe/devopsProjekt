import React, { useEffect, useState } from "react";
import {
  createCustomers,
  getCustomers,
  updateCustomers,
} from "../services/CustomersService";
import { useNavigate, useParams } from "react-router-dom";

/**
 * Functional component for managing for the customers 
 * Handles creation, updating, and validation of customer information.
 * @returns {JSX.Element} The JSX element representing the customer management form.
 */
const CustomersComponent = () => {
  const [name, setName] = useState("");
  const [city, setCity] = useState("");

  const { id } = useParams();

  const [errors, setErrors] = useState({
    name: "",
    city: "",
  });

  const navigator = useNavigate();

  useEffect(() => {
    if (id) {
      getCustomers(id)
        .then((response) => {
          setName(response.data.name);
          setCity(response.data.city);
        })
        .catch((error) => {
          console.error(error);
        });
    }
  }, [id]);

  function saveOrUpdateCustomer(e) {
    e.preventDefault();

    if (validateForm()) {
      const customer = { name, city };

      if (id) {
        updateCustomers(id, customer)
          .then((response) => {
            console.log("Customer updated successfully", response.data);
            navigator(`/customers`);
          })
          .catch((error) => {
            console.error("There was an error updating the customer!", error);
          });
      } else {
        createCustomers(customer)
          .then((response) => {
            console.log("Customer created successfully", response.data);
            navigator("/customers");
          })
          .catch((error) => {
            console.log(error);
          });
      }
    }
  }

  function validateForm() {
    let valid = true;

    const errorsCopy = { ...errors };

    if (name.trim()) {
      errorsCopy.name = "";
    } else {
      errorsCopy.name = "The name is required";
      valid = false;
    }

    if (city.trim()) {
      errorsCopy.city = "";
    } else {
      errorsCopy.city = "The city is required";
      valid = false;
    }

    setErrors(errorsCopy);

    return valid;
  }

  function pageTitle() {
    if (id) {
      return <h2 className="text-center"> Update Customer </h2>;
    } else {
      return <h2 className="text-center"> Add Customer </h2>;
    }
  }

  return (
    <div className="container">
      <br /> <br />
      <div className="row">
        <div className="card col-md-6 offset-md-3 offset-md-3">
          {pageTitle()}
          <div className="card-body">
            <form>
              <div className="form-group mb-2">
                <label className="form-label">Name:</label>
                <input
                  type="text"
                  placeholder="Enter Customer name"
                  name="ID"
                  value={name}
                  className={`form-control ${errors.name ? "is-invalid" : ""}`}
                  onChange={(e) => setName(e.target.value)}
                />
                {errors.name && (
                  <div className="invalid-feedback"> {errors.name} </div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">City:</label>
                <input
                  type="text"
                  placeholder="Enter Customer City"
                  name="City"
                  value={city}
                  className={`form-control ${errors.city ? "is-invalid" : ""}`}
                  onChange={(e) => setCity(e.target.value)}
                />
                {errors.city && (
                  <div className="invalid-feedback"> {errors.city} </div>
                )}
              </div>

              <button
                className="btn btn-success"
                onClick={saveOrUpdateCustomer}
              >
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
export default CustomersComponent;
