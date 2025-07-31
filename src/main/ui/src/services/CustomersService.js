import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8081/api/customers";
//const REST_API_BASE_URL = "http://4.231.167.183/customer/api/customers";

export const listCustomers = () => axios.get(REST_API_BASE_URL);

export const createCustomers = (customer) =>
  axios.post(REST_API_BASE_URL, customer);

export const getCustomers = (customerId) =>
  axios.get(REST_API_BASE_URL + "/" + customerId);

export const updateCustomers = (customersId, customers) => {
  return axios.put(`${REST_API_BASE_URL}/${customersId}`, customers, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export const deleteCustomers = (id) => {
  return axios.delete(`${REST_API_BASE_URL}/${id}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`, // Use the correct token
    },
  });
};
