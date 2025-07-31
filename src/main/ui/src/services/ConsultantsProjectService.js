import axios from "axios";

//const BASE_URL ="http://4.231.167.183/consultantproject/api/consultantsProjects";
const BASE_URL ="http://localhost:8085/api/consultantsProjects";

// Function to create a new consult
export const createConsultantsProject = (payload) => {
  return axios.post(BASE_URL, payload);
};

export const getAllConsultantsProjects = () => {
  return axios.get(`${BASE_URL}`);
};

export const deleteConsultantsByProjectId = (projectId) => {
  return axios.delete(`${BASE_URL}/${projectId}`);
};
