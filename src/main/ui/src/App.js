import "./App.css";
import ListCustomersComponent from "./components/ListCustomersComponent";

import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HeaderComponent from "./components/HeaderComponent";
import CustomersComponent from "./components/CustomersComponent";
import HomeComponent from "./components/HomeComponent";
import ListConsultantsComponent from "./components/ListConsultantsComponent";
import ConsultantsComponent from "./components/ConsultantsComponent";
import ListProjectsComponents from "./components/ListProjectsComponents";
import ProjectsComponents from "./components/ProjectsComponents";
import ListEntriesComponent from "./components/ListEntriesComponent";
import ListUserMappingComponent from "./components/ListUserMappingComponent";
import EntriesComponent from "./components/EntriesComponent";
import UserMappingComponent from "./components/UserMappingComponent";

function App() {
  return (
    <Router>
      <>
        <HeaderComponent />
        <Routes>
          <Route path="/" element={<HomeComponent />}></Route>
          <Route path="/customers" element={<ListCustomersComponent />}></Route>
          <Route path="/add-customers" element={<CustomersComponent />}></Route>
          <Route
            path="/edit-customers/:id"
            element={<CustomersComponent />}
          ></Route>
          <Route
            path="/consultants"
            element={<ListConsultantsComponent />}
          ></Route>
          <Route
            path="/add-consultants"
            element={<ConsultantsComponent />}
          ></Route>
          <Route
            path="/edit-consultants/:id"
            element={<ConsultantsComponent />}
          ></Route>
          <Route path="/projects" element={<ListProjectsComponents />}></Route>
          <Route path="/add-projects" element={<ProjectsComponents />}></Route>
          <Route
            path="/edit-projects/:id"
            element={<ProjectsComponents />}
          ></Route>
          <Route path="/entries" element={<ListEntriesComponent />}></Route>
          <Route path="/add-entries" element={<EntriesComponent />}></Route>
          <Route
            path="/edit-entries/:id"
            element={<EntriesComponent />}
          ></Route>
          <Route
            path="/usermappings"
            element={<ListUserMappingComponent />}
          ></Route>
          <Route
            path="/add-usermappings"
            element={<UserMappingComponent />}
          ></Route>
          <Route
            path="/edit-usermappings/:id"
            element={<UserMappingComponent />}
          ></Route>
        </Routes>
      </>
    </Router>
  );
}

export default App;
