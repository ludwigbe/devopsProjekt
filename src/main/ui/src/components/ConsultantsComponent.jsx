import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  createConsultant,
  getConsultant,
  updateConsultant,
  deleteConsultant,
  listConsultants,
} from "../services/ConsultantsService";

import { getAllConsultantsProjects } from "../services/ConsultantsProjectService";

function ConsultantsComponent() {
  const [consultants, setConsultants] = useState([]);
  const [name, setName] = useState("");
  const [consultantProjects, setConsultantsProject] = useState([]);
  const { id } = useParams(); // Get the consultant ID from the URL if editing
  const [errors, setErrors] = useState({ name: "" });
  const navigate = useNavigate();

  useEffect(() => {
    // If `id` exists, we are in edit mode and fetch the consultant's details
    getAllConsultantsProjects().then((res) => {
      setConsultantsProject(res.data);
      getConsultant()
        .then((response) => {
          const tmpResult = response.data.map((value) => {
            return {
              id: value.id,
              name: value.name,
              bookedProjects: res.data.filter(
                (element) => element.consultantsId === value.id,
              ),
            };
          });
          console.log(tmpResult);
        })
        .catch((error) => console.error("Error fetching consultant:", error));
    });

    // Load all consultants and available projects regardless of mode (edit or create)
    listConsultants()
      .then((response) => setConsultants(response.data))
      .catch((error) => console.error("Error listing consultants:", error));
  }, [id]);

  function saveOrUpdateConsultant(e) {
    e.preventDefault();
    if (validateForm()) {
      const consultant = {
        name,
      };

      if (id) {
        // Update an existing consultant
        updateConsultant(id, consultant)
          .then(() => navigate("/consultants"))
          .catch((error) => console.error("Error updating consultant:", error));
      } else {
        // Create a new consultant
        createConsultant(consultant)
          .then(() => navigate("/consultants"))
          .catch((error) => console.error("Error creating consultant:", error));
      }
    }
  }

  function validateForm() {
    let valid = true;
    const errorsCopy = { ...errors };
    if (!name.trim()) {
      errorsCopy.name = "The name is required";
      valid = false;
    } else {
      errorsCopy.name = "";
    }
    setErrors(errorsCopy);
    return valid;
  }

  function deleteCurrentConsultant() {
    if (id) {
      deleteConsultant(id)
        .then(() => navigate("/consultants"))
        .catch((error) => console.error("Error deleting consultant:", error));
    }
  }

  return (
    <div className="container">
      <h2 className="text-center">
        {id ? "Update Consultant" : "Add Consultant"}
      </h2>
      <form onSubmit={saveOrUpdateConsultant}>
        <div className="form-group mb-2">
          <label>Name:</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className={`form-control ${errors.name ? "is-invalid" : ""}`}
          />
          {errors.name && <div className="invalid-feedback">{errors.name}</div>}
        </div>

        <button className="btn btn-success" type="submit">
          Submit
        </button>
        {id && (
          <button
            type="button"
            className="btn btn-danger ml-2"
            onClick={deleteCurrentConsultant}
          >
            Delete Consultant
          </button>
        )}
      </form>
    </div>
  );
}

export default ConsultantsComponent;
