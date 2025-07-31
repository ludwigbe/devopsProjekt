import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createEntry, getEntry, updateEntry } from "../services/EntriesService";
import { listConsultants } from "../services/ConsultantsService";
import { listProjects } from "../services/ProjectsService";

function EntriesComponent() {
  const [date, setDate] = useState("");
  const [hours, setHours] = useState("");
  const [projectId, setProjectId] = useState("");
  const [consultantsId, setConsultantsId] = useState("");
  const [projectOptions, setProjectOptions] = useState([]);
  const [consultantOptions, setConsultantOptions] = useState([]);
  const [selectedProjectName, setSelectedProjectName] = useState("");
  const [selectedConsultantName, setSelectedConsultantName] = useState("");
  const { id } = useParams();
  const navigate = useNavigate();

  const [errors, setErrors] = useState({
    date: "",
    hours: "",
    projectId: "",
    consultantsId: "",
  });

  useEffect(() => {
    if (id) {
      // Fetch the entry to edit and populate form fields
      getEntry(id)
        .then((response) => {
          const entryData = response.data;
          setDate(new Date(entryData.date).toISOString().slice(0, 10));
          setHours(entryData.hours);
          setProjectId(entryData.projectId);
          setConsultantsId(entryData.consultantsId);

          // Fetch all projects and consultants
          listProjects().then((projectsResponse) => {
            const projects = projectsResponse.data;
            setProjectOptions(projects);
            const project = projects.find(
              (proj) => proj.id === entryData.projectId,
            );
            setSelectedProjectName(project ? project.name : "");
          });

          listConsultants().then((consultantsResponse) => {
            const consultants = consultantsResponse.data;
            setConsultantOptions(consultants);
            const consultant = consultants.find(
              (consult) => consult.id === entryData.consultantsId,
            );
            setSelectedConsultantName(consultant ? consultant.name : "");
          });
        })
        .catch(console.error);
    } else {
      // For a new entry, load all options without setting specific names
      listProjects()
        .then((response) => setProjectOptions(response.data))
        .catch(console.error);
      listConsultants()
        .then((response) => setConsultantOptions(response.data))
        .catch(console.error);
    }
  }, [id]);

  function updateSelectedNames() {
    const selectedProject = projectOptions.find(
      (project) => project.id === projectId,
    );
    setSelectedProjectName(selectedProject ? selectedProject.name : "");

    const selectedConsultant = consultantOptions.find(
      (consultant) => consultant.id === consultantsId,
    );
    setSelectedConsultantName(
      selectedConsultant ? selectedConsultant.name : "",
    );
  }

  useEffect(() => {
    // Update displayed names when IDs change
    updateSelectedNames();
  }, [projectId, consultantsId, projectOptions, consultantOptions]);

  function saveOrUpdateEntry(e) {
    e.preventDefault();

    if (validateForm()) {
      const entry = { date, hours, projectId, consultantsId };

      const handleResponse = () => {
        updateSelectedNames(); // Update names immediately after successful save
        navigate(`/entries`);
      };

      if (id) {
        updateEntry(id, entry)
          .then(handleResponse)
          .catch((error) => console.error("Error updating the entry!", error));
      } else {
        createEntry(entry).then(handleResponse).catch(console.error);
      }
    }
  }

  function validateForm() {
    let valid = true;
    const errorsCopy = { ...errors };

    if (date.trim()) {
      errorsCopy.date = "";
    } else {
      errorsCopy.date = "The date is required";
      valid = false;
    }

    if (hours && !isNaN(hours) && hours > 0) {
      errorsCopy.hours = "";
    } else {
      errorsCopy.hours = "Valid hours are required";
      valid = false;
    }

    if (String(projectId).trim()) {
      errorsCopy.projectId = "";
    } else {
      errorsCopy.projectId = "The project is required";
      valid = false;
    }

    if (String(consultantsId).trim()) {
      errorsCopy.consultantsId = "";
    } else {
      errorsCopy.consultantsId = "The consultant is required";
      valid = false;
    }

    setErrors(errorsCopy);
    return valid;
  }

  function pageTitle() {
    return id ? (
      <h2 className="text-center"> Update Entry </h2>
    ) : (
      <h2 className="text-center"> Add Entry </h2>
    );
  }

  return (
    <div className="container">
      <br /> <br />
      <div className="row">
        <div className="card col-md-6 offset-md-3">
          {pageTitle()}
          <div className="card-body">
            <form>
              <div className="form-group mb-2">
                <label className="form-label">Date:</label>
                <input
                  type="date"
                  value={date}
                  className={`form-control ${errors.date ? "is-invalid" : ""}`}
                  onChange={(e) => setDate(e.target.value)}
                />
                {errors.date && (
                  <div className="invalid-feedback"> {errors.date} </div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">Hours:</label>
                <input
                  type="number"
                  placeholder="Enter hours"
                  value={hours}
                  className={`form-control ${errors.hours ? "is-invalid" : ""}`}
                  onChange={(e) => setHours(e.target.value)}
                />
                {errors.hours && (
                  <div className="invalid-feedback"> {errors.hours} </div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">Select Project:</label>
                <select
                  value={projectId}
                  className={`form-control ${errors.projectId ? "is-invalid" : ""}`}
                  onChange={(e) => setProjectId(e.target.value)}
                >
                  <option value="">
                    {selectedProjectName || "-- Select Project --"}
                  </option>
                  {projectOptions.map((project) => (
                    <option key={project.id} value={project.id}>
                      {project.name}
                    </option>
                  ))}
                </select>
                {errors.projectId && (
                  <div className="invalid-feedback">{errors.projectId}</div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">Select Consultant:</label>
                <select
                  value={consultantsId}
                  className={`form-control ${errors.consultantsId ? "is-invalid" : ""}`}
                  onChange={(e) => setConsultantsId(e.target.value)}
                >
                  <option value="">
                    {selectedConsultantName || "-- Select Consultant --"}
                  </option>
                  {consultantOptions.map((consultant) => (
                    <option key={consultant.id} value={consultant.id}>
                      {consultant.name}
                    </option>
                  ))}
                </select>
                {errors.consultantsId && (
                  <div className="invalid-feedback">{errors.consultantsId}</div>
                )}
              </div>
              <button className="btn btn-success" onClick={saveOrUpdateEntry}>
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EntriesComponent;
