import axios from "axios";

//const REST_API_BASE_URL = "http://4.231.167.183/api/usermappings";
const REST_API_BASE_URL = "http://localhost/api/usermappings";

export const listUserMappings = () => axios.get(REST_API_BASE_URL);

export const createUserMapping = (userMapping) =>
  axios.post(REST_API_BASE_URL, userMapping);

export const getUserMapping = (mappingId) =>
  axios.get(`${REST_API_BASE_URL}/${mappingId}`);

export const updateUserMapping = (mappingId, userMapping) => {
  return axios.put(`${REST_API_BASE_URL}/${mappingId}`, userMapping, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export const deleteUserMapping = (mappingId) => {
  return axios.delete(`${REST_API_BASE_URL}/${mappingId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`, // Use the correct token
    },
  });
};

export const getConsultantsForUser = async (userId) => {
  try {
    return await axios.get(`${REST_API_BASE_URL}/consultants/${userId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  } catch (error) {
    console.error("Error fetching consultants for user", error);
    throw error;
  }
};

// Beispiel für das Hinzufügen einer Zuordnung (wenn Admin dies verwalten soll)
export const addUserConsultantMapping = async (userId, consultantId) => {
  try {
    return await axios.post(
      `${REST_API_BASE_URL}/add`,
      { userId, consultantId },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      },
    );
  } catch (error) {
    console.error("Error adding user-consultant mapping", error);
    throw error;
  }
};

// Beispiel für das Entfernen einer Zuordnung (wenn Admin dies verwalten soll)
export const removeUserConsultantMapping = async (mappingId) => {
  try {
    return await axios.delete(`${REST_API_BASE_URL}/remove/${mappingId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  } catch (error) {
    console.error("Error removing user-consultant mapping", error);
    throw error;
  }
};
