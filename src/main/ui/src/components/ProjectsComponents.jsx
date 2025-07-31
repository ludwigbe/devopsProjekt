import React, { useEffect, useState } from "react";
import {
  createProject,
  getProject,
  updateProject,
} from "../services/ProjectsService";
import { useNavigate, useParams } from "react-router-dom";
import { listCustomers } from "../services/CustomersService";
import { listConsultants } from "../services/ConsultantsService";
import {
  createConsultantsProject,
  deleteConsultantsByProjectId,
} from "../services/ConsultantsProjectService";

// Helper component to render a text field with handling
const TextField = ({ label, value, onChange, error, ...props }) => (
  <div className="form-group mb-2">
    <label className="form-label">{label}</label>
    <input
      value={value}
      onChange={onChange}
      className={`form-control ${error ? "is-invalid" : ""}`}
      {...props}
    />
    {error && <div className="invalid-feedback">{error}</div>}
  </div>
);

// Helper component to render a date
const DateField = ({ label, value, onChange, error }) => (
  <TextField
    label={label}
    type="date"
    value={value}
    onChange={onChange}
    error={error}
  />
);

// Helper component for select dropdowns
const SelectField = ({ label, options, value, onChange, error }) => (
  <div className="form-group mb-2">
    <label className="form-label">{label}</label>
    <select
      value={value}
      onChange={onChange}
      className={`form-control ${error ? "is-invalid" : ""}`}
    >
      <option value="">Select {label}</option>
      {options.map((opt) => (
        <option key={opt.id} value={JSON.stringify(opt)}>
          {opt.name} ({opt.city || opt.name})
        </option>
      ))}
    </select>
    {error && <div className="invalid-feedback">{error}</div>}
  </div>
);

// Multi-select field for consultants
const MultiSelectField = ({
  label,
  options,
  selectedValues,
  onChange,
  error,
}) => (
  <div className="form-group mb-2">
    <label className="form-label">{label}</label>
    <select
      multiple
      value={selectedValues.map((val) => JSON.stringify(val))}
      onChange={onChange}
      className={`form-control ${error ? "is-invalid" : ""}`}
    >
      {options.map((opt) => (
        <option key={opt.id} value={JSON.stringify(opt)}>
          {opt.name}
        </option>
      ))}
    </select>
    {error && <div className="invalid-feedback">{error}</div>}
  </div>
);

const ProjectsComponent = () => {
  const [formData, setFormData] = useState({
    name: "",
    startDate: "",
    endDate: "",
    status: "",
    customersId: "",
    customerName: "",
    selectedConsultants: [],
  });
  const [customers, setCustomers] = useState([]);
  const [consultants, setConsultants] = useState([]);
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const { id } = useParams();

  // Load customers and consultants on mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [customersData, consultantsData] = await Promise.all([
          listCustomers(),
          listConsultants(),
        ]);
        setCustomers(customersData.data);
        setConsultants(consultantsData.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  // Fetch project data if editing
  useEffect(() => {
    if (id) {
      getProject(id)
        .then((response) => {
          const project = response.data;
          setFormData({
            name: project.name,
            startDate: project.startDate.slice(0, 10),
            endDate: project.endDate.slice(0, 10),
            status: project.status,
            customersId: project.customersId,
            customerName: project.customerName,
            selectedConsultants: project.consultants || [],
          });
        })
        .catch((error) => {
          console.error("Error fetching project:", error);
        });
    }
  }, [id]);

  // Update form data and errors state
  const handleChange = (key, value) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
    setErrors((prev) => ({ ...prev, [key]: "" }));
  };

  const validateForm = () => {
    const newErrors = {};
    let isValid = true;

    if (!formData.name.trim()) {
      newErrors.name = "The project name is required";
      isValid = false;
    }
    if (!formData.startDate.trim()) {
      newErrors.startDate = "The start date is required";
      isValid = false;
    }
    if (!formData.endDate.trim()) {
      newErrors.endDate = "The end date is required";
      isValid = false;
    } else if (new Date(formData.startDate) > new Date(formData.endDate)) {
      newErrors.endDate = "End date cannot be before start date";
      isValid = false;
    }
    if (!formData.status.trim()) {
      newErrors.status = "The project status is required";
      isValid = false;
    }
    if (!formData.customersId) {
      newErrors.customersId = "Please select a customer";
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const saveOrUpdateProject = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    const projectPayload = {
      name: formData.name,
      startDate: new Date(formData.startDate).toISOString(),
      endDate: new Date(formData.endDate).toISOString(),
      status: formData.status,
      customersId: formData.customersId,
      customerName: formData.customerName,
    };

    try {
      if (id) {
        // Updating an existing project
        await updateProject(id, projectPayload);

        // Remove existing consultant associations for this project
        await deleteConsultantsByProjectId(id);

        // Add the updated consultant associations
        const updateConsultantPromises = formData.selectedConsultants.map(
          (consultant) =>
            createConsultantsProject({
              projectsId: id,
              projectsName: formData.name,
              consultantsId: consultant.id,
              consultantsName: consultant.name,
            }),
        );
        await Promise.all(updateConsultantPromises);
      } else {
        // Creating a new project
        const res = await createProject(projectPayload);

        // Add the consultant associations for the new project
        const createConsultantPromises = formData.selectedConsultants.map(
          (consultant) =>
            createConsultantsProject({
              projectsId: res.data.id,
              projectsName: res.data.name,
              consultantsId: consultant.id,
              consultantsName: consultant.name,
            }),
        );
        await Promise.all(createConsultantPromises);
      }
      navigate("/projects");
    } catch (error) {
      console.error("Error saving project:", error);
    }
  };

  return (
    <div className="container">
      <div className="row">
        <div className="card col-md-6 offset-md-3">
          <h2 className="text-center">
            {id ? "Update Project" : "Add Project"}
          </h2>
          <div className="card-body">
            <form onSubmit={saveOrUpdateProject}>
              <TextField
                label="Project Name"
                value={formData.name}
                onChange={(e) => handleChange("name", e.target.value)}
                error={errors.name}
              />
              <DateField
                label="Start Date"
                value={formData.startDate}
                onChange={(e) => handleChange("startDate", e.target.value)}
                error={errors.startDate}
              />
              <DateField
                label="End Date"
                value={formData.endDate}
                onChange={(e) => handleChange("endDate", e.target.value)}
                error={errors.endDate}
              />
              <TextField
                label="Status"
                value={formData.status}
                onChange={(e) => handleChange("status", e.target.value)}
                error={errors.status}
              />
              <SelectField
                label="Customer"
                options={customers}
                value={formData.customersId}
                onChange={(e) => {
                  const customer = JSON.parse(e.target.value);
                  handleChange("customersId", customer.id);
                  handleChange("customerName", customer.name);
                }}
                error={errors.customersId}
              />
              <MultiSelectField
                label="Consultants"
                options={consultants}
                selectedValues={formData.selectedConsultants}
                onChange={(e) => {
                  const consultantsSelected = Array.from(
                    e.target.selectedOptions,
                  ).map((option) => JSON.parse(option.value));
                  handleChange("selectedConsultants", consultantsSelected);
                }}
                error={errors.consultantsId}
              />
              <button className="btn btn-success" type="submit">
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProjectsComponent;
