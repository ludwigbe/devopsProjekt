import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  deleteConsultant,
  listConsultants,
} from "../services/ConsultantsService";
import { getAllConsultantsProjects } from "../services/ConsultantsProjectService";

/**
 * Component to display and manage the list
 * - Fetches all consultants on component mount.
 * - Provides functionality to add, update, and remove consultants.
 *
 * @returns {JSX.Element} Consultants component UI.
 */
function ListConsultantsComponent() {
  const [consultants, setConsultants] = useState([]);

  const navigator = useNavigate();

  useEffect(() => {
    getAllConsultants();
  }, []);

  // Funktion, um alle Consultants abzurufen
  function getAllConsultants() {
    getAllConsultantsProjects().then((res) => {
      listConsultants()
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
          console.log("tmp", tmpResult);
          setConsultants(tmpResult);
        })
        .catch((error) => console.error("Error fetching consultant:", error));
    });

    listConsultants()
      .then((response) => {
        setConsultants(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  // Navigiere zur Seite, um einen neuen Consultant hinzuzufügen
  function addConsultant() {
    navigator("/add-consultants");
  }

  // Navigiere zur Seite, um einen bestehenden Consultant zu bearbeiten
  function updateConsultant(id) {
    navigator(`/edit-consultants/${id}/`);
  }

  // Consultant löschen und die Liste aktualisieren
  function removeConsultant(id) {
    alert("you are sure to delete this consultant " + id);
    deleteConsultant(id)
      .then((response) => {
        getAllConsultants();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="container">
      <h2 className="text-center"> List of Consultants </h2>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th> ID</th>
            <th> Name</th>
            <th> Projects </th>
            <th> Actions </th>
          </tr>
        </thead>

        <tbody>
          {consultants.map((consultant) => (
            <tr key={consultant.id}>
              <td>{consultant.id}</td>
              <td>{consultant.name}</td>
              <td>
                {consultant.bookedProjects &&
                consultant.bookedProjects.length > 0 ? (
                  <ul>
                    {consultant.bookedProjects.map((project) => (
                      <li key={project.projectId}>
                        <strong> ID: </strong> {project.projectsId} ,{" "}
                        <strong> Name:</strong> {project.projectsName}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>No projects assigned</p>
                )}
              </td>
              <td>
                <button
                  className="btn btn-info"
                  onClick={() => updateConsultant(consultant.id)}
                >
                  Update
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => removeConsultant(consultant.id)}
                  style={{ marginLeft: "10px" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="btn btn-primary mb-2" onClick={addConsultant}>
        Add Consultant
      </button>
    </div>
  );
}

export default ListConsultantsComponent;
