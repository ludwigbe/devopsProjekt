import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { deleteEntry, listEntries } from "../services/EntriesService";

/**
 * The component does not render any child components.
 */
function ListEntriesComponent() {
  const [entries, setEntries] = useState([]);
  const navigator = useNavigate();

  useEffect(() => {
    getAllEntries();
  }, []);

  function getAllEntries() {
    listEntries()
      .then((response) => {
        setEntries(response.data);
        console.log("Entries", response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }

  function addEntry() {
    navigator("/add-entries");
  }

  function updateEntry(id) {
    navigator(`/edit-entries/${id}`);
  }

  function removeEntry(id) {
    alert("you are sure to delete this entries " + id);
    deleteEntry(id)
      .then(() => {
        getAllEntries();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="container">
      <h2 className="text-center"> List of Entries </h2>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th> ID </th>
            <th> Date </th>
            <th> Hours </th>
            <th> Project </th>
            <th> Consultant </th>
            <th> Actions </th>
          </tr>
        </thead>

        <tbody>
          {entries.length > 0 ? (
            entries.map((entry) => (
              <tr key={entry.entryId}>
                <td>{entry.entryId}</td>
                <td>{entry.date}</td>
                <td>{entry.hours}</td>
                <td>
                  {entry.projectId ? (
                    <>
                      <strong>ID:</strong> {entry.projectId} ,{" "}
                      <strong>Name:</strong> {entry.projectName}
                    </>
                  ) : (
                    "No Project"
                  )}
                </td>
                <td>
                  {entry.consultantsId ? (
                    <>
                      <strong>ID:</strong> {entry.consultantsId} ,{" "}
                      <strong>Name:</strong> {entry.consultantsName}
                    </>
                  ) : (
                    "No Customer"
                  )}
                </td>

                <td>
                  <button
                    className="btn btn-info"
                    onClick={() => updateEntry(entry.entryId)}
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-danger"
                    onClick={() => removeEntry(entry.entryId)}
                    style={{ marginLeft: "10px" }}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">
                No entries found
              </td>
            </tr>
          )}
        </tbody>
      </table>

      <button className="btn btn-primary mb-2" onClick={addEntry}>
        Add Entry
      </button>
    </div>
  );
}

export default ListEntriesComponent;
