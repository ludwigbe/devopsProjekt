import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8084/api/entries";
//const REST_API_BASE_URL = "http://4.231.167.183/entry/api/entries";

export const listEntries = () => axios.get(REST_API_BASE_URL);

export const createEntry = (entry) => axios.post(REST_API_BASE_URL, entry);

export const getEntry = (entryId) =>
  axios.get(`${REST_API_BASE_URL}/${entryId}`);

export const updateEntry = (entryId, entry) => {
  return axios.put(`${REST_API_BASE_URL}/${entryId}`, entry, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export const deleteEntry = (entryId) => {
  return axios.delete(`${REST_API_BASE_URL}/${entryId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`, // Use the correct token
    },
  });
};
