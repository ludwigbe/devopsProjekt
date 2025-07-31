import React, { useEffect, useState } from "react";
import { deleteProject, listProjects } from "../services/ProjectsService";
import { useNavigate } from "react-router-dom";
import { getAllConsultantsProjects } from "../services/ConsultantsProjectService";

/**
 * Component to display a list of all projects in the database.
 * This component fetches the list of projects when mounted and displays them in a table.
 * It also provides buttons to add a new project, update an existing project, or delete a project.
 * When the user clicks on the "Delete" button, the component shows a confirmation dialog
 * and then deletes the project from the database if confirmed.
 * After adding, updating, or deleting a project, the component fetches the list of projects again
 */
const ListProjectsComponent = () => {
  const [projects, setProjects] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getAllProjects();
  }, []);

  // Funktion, um alle Projekte zu laden
  function getAllProjects() {
    getAllConsultantsProjects().then((res) => {
      listProjects()
        .then((response) => {
          const tmpResult = response.data.map((value) => {
            return {
              id: value.id,
              name: value.name,
              endDate: value.endDate,
              startDate: value.startDate,
              status: value.status,
              customersId: value.customersId,
              customerName: value.customerName,
              bookedConsultants: res.data.filter(
                (element) => element.projectsId === value.id,
              ),
            };
          });
          console.log("les projectcs", tmpResult);
          setProjects(tmpResult);
        })
        .catch((error) => console.error("Error fetching consultant:", error));
    });
  }

  // Navigation zur Seite, um ein neues Projekt hinzuzufügen
  function addProject() {
    navigate("/add-projects");
  }

  // Navigation zur Seite, um ein bestehendes Projekt zu bearbeiten
  function updateProject(id) {
    navigate(`/edit-projects/${id}`);
  }

  // Delete a project and refresh the list
  function removeProject(id) {
    if (window.confirm("Are you sure you want to delete this project?")) {
      deleteProject(id)
        .then(() => {
          getAllProjects(); // Refresh list after deletion
        })
        .catch((error) => {
          console.error(error);
        });
    }
  }

  return (
    <div className="container">
      <h2 className="text-center">List of Projects</h2>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
            <th>Customer</th>
            <th>Project Staff</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {projects.map((project) => (
            <tr key={project.id}>
              <td>{project.id}</td>
              <td>{project.name}</td>
              <td>{new Date(project.startDate).toLocaleDateString()}</td>
              <td>{new Date(project.endDate).toLocaleDateString()}</td>
              <td>{project.status}</td>
              <td>
                <strong>ID:</strong> {project.customersId} ,{" "}
                <strong>Name:</strong> {project.customerName}
              </td>
              <td>
                {project.bookedConsultants &&
                project.bookedConsultants.length > 0 ? (
                  <ul>
                    {project.bookedConsultants.map((consultant) => (
                      <li key={consultant.consultantsId}>
                        <strong>ID:</strong> {consultant.consultantsId} ,{" "}
                        <strong>Name:</strong> {consultant.consultantsName}
                      </li>
                    ))}
                  </ul>
                ) : (
                  "No consultants"
                )}
              </td>

              <td>
                <button
                  className="btn btn-info"
                  onClick={() => updateProject(project.id)}
                >
                  Update
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => removeProject(project.id)}
                  style={{ marginLeft: "10px" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button className="btn btn-primary mb-2" onClick={addProject}>
        Add Project
      </button>
    </div>
  );
};

export default ListProjectsComponent;
