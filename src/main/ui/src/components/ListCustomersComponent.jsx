import React, { useEffect, useState } from "react";
import { deleteCustomers, listCustomers } from "../services/CustomersService";
import { useNavigate } from "react-router-dom";

/**
 * A React component that displays a list of customers 
 *
 * This component retrieves a list of customers from the database on mount and
 * displays them in a table. It also provides buttons to add new customers,
 * update existing customers, and delete existing customers.
 */
const ListCustomersComponent = () => {
  const [customer, setCustomer] = useState([]);

  const navigator = useNavigate();

  useEffect(() => {
    getAllCustomers();
  }, []);

  function getAllCustomers() {
    listCustomers()
      .then((response) => {
        setCustomer(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  function addCustomers() {
    navigator("/add-customers");
  }

  function updateCustomers(id) {
    navigator(`/edit-customers/${id}/`);
  }

  /**
   * Remove a customer with the given id from the database.
   * @param {number} id - The id of the customer to remove.
   * @returns {Promise<void>}
   */
  function removeCustomers(id) {
    console.log(id);
    alert("you are sure to delete this customer " + id);

    deleteCustomers(id)
      .then((response) => {
        getAllCustomers();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="container">
      <h2 className="text-center"> List of Customers </h2>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th> ID</th>
            <th> Name</th>
            <th> City</th>
            <th> Actions </th>
          </tr>
        </thead>

        <tbody>
          {customer.map((customers) => (
            <tr key={customers.id}>
              <td>{customers.id}</td>
              <td>{customers.name}</td>
              <td>{customers.city}</td>
              <td>
                <button
                  className="btn btn-info"
                  onClick={() => updateCustomers(customers.id)}
                >
                  Update
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => removeCustomers(customers.id)}
                  style={{ marginLeft: "10px" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="btn btn-primary mb-2" onClick={addCustomers}>
        Add Customers
      </button>
    </div>
  );
};

export default ListCustomersComponent;
