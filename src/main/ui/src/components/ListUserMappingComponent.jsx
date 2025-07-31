import React, { useEffect, useState } from "react";
import {
  deleteUserMapping,
  listUserMappings,
} from "../services/UserMappingService";
import { useNavigate } from "react-router-dom";

/**
 * Komponente, die eine Liste aller Nutzerzuordnungen anzeigt und es erlaubt,
 * Zuordnungen hinzuzufügen, bestehende zu bearbeiten oder zu löchen.
 * @returns {React.ReactElement} Die Liste der Nutzerzuordnungen.
 */
const ListUserMappingComponent = () => {
  const [userMappings, setUserMappings] = useState([]);
  const navigator = useNavigate();

  useEffect(() => {
    getAllUserMappings();
  }, []);

  function getAllUserMappings() {
    listUserMappings()
      .then((response) => {
        setUserMappings(response.data);
        console.log("usermapping", response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  function addUserMapping() {
    navigator("/add-usermappings");
  }

  function updateUserMapping(id) {
    navigator(`/edit-usermappings/${id}`);
  }

  function removeUserMapping(id) {
    alert("you are sure to delete this usermapping " + id);
    console.log(id);
    deleteUserMapping(id)
      .then((response) => {
        getAllUserMappings();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="container">
      <h2 className="text-center"> List of User Mappings </h2>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th> ID</th>
            <th> External ID</th>
            <th> Consultant ID</th>
            <th> Actions</th>
          </tr>
        </thead>

        <tbody>
          {userMappings.map((mapping) => (
            <tr key={mapping.id}>
              <td>{mapping.id}</td>
              <td>{mapping.consultantID}</td>
              <td>{mapping.externalID}</td>
              <td>
                <button
                  className="btn btn-info"
                  onClick={() => updateUserMapping(mapping.id)}
                >
                  Update
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => removeUserMapping(mapping.id)}
                  style={{ marginLeft: "10px" }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="btn btn-primary mb-2" onClick={addUserMapping}>
        Add User Mapping
      </button>
    </div>
  );
};

export default ListUserMappingComponent;
