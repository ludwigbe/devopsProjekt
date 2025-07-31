import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  createUserMapping,
  getUserMapping,
  updateUserMapping,
} from "../services/UserMappingService";

/**
 * A React component for creating or updating a user mapping.
 * The `validateForm` function is used to validate the form fields before
 * submitting the form. It checks that the `userId`, `externalId`, and
 * `consultantId` fields are not empty, and returns an object containing any
 * error messages. If the form is valid, it returns `true`, otherwise it
 * returns `false`.
 *
 * @returns {React.ReactElement}
 */
function UserMappingComponent() {
  const [userId, setUserId] = useState("");
  const [externalId, setExternalId] = useState("");
  const [consultantId, setConsultantId] = useState("");

  const { id } = useParams();

  const [errors, setErrors] = useState({
    userId: "",
    externalId: "",
    consultantId: "",
  });

  const navigator = useNavigate();

  useEffect(() => {
    if (id) {
      getUserMapping(id)
        .then((response) => {
          setUserId(response.data.userId);
          setExternalId(response.data.externalId);
          setConsultantId(response.data.consultantId);
        })
        .catch((error) => {
          console.error(error);
        });
    }
  }, [id]);

  function saveOrUpdateUserMapping(e) {
    e.preventDefault();

    if (validateForm()) {
      const userMapping = { userId, externalId, consultantId };

      if (id) {
        updateUserMapping(id, userMapping)
          .then((response) => {
            console.log("UserMapping updated successfully", response.data);
            navigator(`/usermappings`);
          })
          .catch((error) => {
            console.error(
              "There was an error updating the UserMapping!",
              error,
            );
          });
      } else {
        createUserMapping(userMapping)
          .then((response) => {
            console.log("UserMapping created successfully", response.data);
            navigator("/usermappings");
          })
          .catch((error) => {
            console.log(error);
          });
      }
    }
  }

  function validateForm() {
    let valid = true;
    const errorsCopy = { ...errors };

    if (userId.trim()) {
      errorsCopy.userId = "";
    } else {
      errorsCopy.userId = "The User ID is required";
      valid = false;
    }

    if (externalId.trim()) {
      errorsCopy.mappedEntityId = "";
    } else {
      errorsCopy.mappedEntityId = "The External ID is required";
      valid = false;
    }

    if (consultantId.trim()) {
      errorsCopy.mappingType = "";
    } else {
      errorsCopy.mappingType = "The Consultant ID is required";
      valid = false;
    }

    setErrors(errorsCopy);
    return valid;
  }

  function pageTitle() {
    return id ? (
      <h2 className="text-center"> Update User Mapping </h2>
    ) : (
      <h2 className="text-center"> Add User Mapping </h2>
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
                <label className="form-label">User ID:</label>
                <input
                  type="text"
                  placeholder="Enter User ID"
                  value={userId}
                  className={`form-control ${errors.userId ? "is-invalid" : ""}`}
                  onChange={(e) => setUserId(e.target.value)}
                />
                {errors.userId && (
                  <div className="invalid-feedback"> {errors.userId} </div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">External ID:</label>
                <input
                  type="text"
                  placeholder="Enter External ID"
                  value={externalId}
                  className={`form-control ${errors.externalId ? "is-invalid" : ""}`}
                  onChange={(e) => setExternalId(e.target.value)}
                />
                {errors.externalId && (
                  <div className="invalid-feedback"> {errors.externalId} </div>
                )}
              </div>

              <div className="form-group mb-2">
                <label className="form-label">Consultant ID</label>
                <input
                  type="text"
                  placeholder="Enter Consultant ID"
                  value={consultantId}
                  className={`form-control ${errors.consultantId ? "is-invalid" : ""}`}
                  onChange={(e) => setConsultantId(e.target.value)}
                />
                {errors.consultantId && (
                  <div className="invalid-feedback">
                    {" "}
                    {errors.consultantId}{" "}
                  </div>
                )}
              </div>

              <button
                className="btn btn-success"
                onClick={saveOrUpdateUserMapping}
              >
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserMappingComponent;
