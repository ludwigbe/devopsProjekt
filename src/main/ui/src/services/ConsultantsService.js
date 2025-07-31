import axios from "axios";

const BASE_URL = "http://localhost:8083/api/consultants";

// Function to load all consultants
export const listConsultants = () => {
  return axios.get(BASE_URL);
};

// Function to create a new consultant
export const createConsultant = (consultant) => {
  return axios.post(BASE_URL, consultant);
};

// Function to get a specific consultant by ID
export const getConsultant = (id) => {
  return axios.get(`${BASE_URL}/${id}`);
};

// Function to update a specific consultant
export const updateConsultant = (id, consultant) => {
  return axios.put(`${BASE_URL}/${id}`, consultant);
};

// Function to delete a specific consultant
export const deleteConsultant = (id) => {
  return axios.delete(`${BASE_URL}/${id}`);
};

// Function to add a project to a consultant
export const addProjectToConsultant = (
  consultantId,
  projectId,
  projectName,
) => {
  return axios.post(
    `${BASE_URL}/${consultantId}/projects/${projectId}`,
    { name: projectName }, // Payload with optional custom name for the project
  );
};

// Function to load all available projects
export const getAllProjects = () => {
  return axios.get("http://localhost:8080/api/projects"); // Adjust the URL for the projects API endpoint
};
