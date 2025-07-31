import axios from "axios";

// Die Basis-URL für die Projects-API
const REST_API_BASE_URL = "http://localhost:8082/api/projects";
//const REST_API_BASE_URL = "http://4.231.167.183/project/api/projects";

// Funktion zum Abrufen aller Projekte
export const listProjects = () => axios.get(REST_API_BASE_URL);

export const createProject = (projects) =>
  axios.post(REST_API_BASE_URL, projects);

// Funktion zum Abrufen eines Projekts nach ID
export const getProject = (projectId) =>
  axios.get(`${REST_API_BASE_URL}/${projectId}`);

// Funktion zum Aktualisieren eines bestehenden Projekts
export const updateProject = (projectId, projects) => {
  return axios.put(`${REST_API_BASE_URL}/${projectId}`, projects, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`, // Authentifizierung mit Token
    },
  });
};

// Funktion zum Löschen eines Projekts
export const deleteProject = (projectId) => {
  return axios.delete(`${REST_API_BASE_URL}/${projectId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`, // Authentifizierung mit Token
    },
  });
}; 
