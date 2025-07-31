import React, { useState, useEffect } from "react";
import AuthService from "../services/AuthService"; // Assurez-vous que le chemin d'import est correct

const HeaderComponent = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Vérifiez si l'utilisateur est déjà authentifié au chargement
    AuthService.getCurrentUser()
      .then((data) => {
        if (data) {
          setUser(data.username); // Mettez à jour l'état avec le nom de l'utilisateur
        }
      })
      .catch(() => {
        setUser(null); // Réinitialisez l'utilisateur en cas d'erreur 
      });
  }, []);

  const handleLogin = async () => {
    AuthService.login(); // Redirection vers Azure pour l'authentification
  };

  const handleLogout = () => {
    AuthService.logout(); // Déconnexion via le backend
    setUser(null); // Réinitialisez l'état
  };

  return (
    <div>
      <header className="header">
        <nav className="navbar navbar-expand navbar-dark bg-primary">
          <div className="container-fluid">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0 d-flex flex-row">
              <li className="nav-item">
                <a className="nav-link text-white" href="/">
                  Home
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="/customers">
                  Customers
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="/consultants">
                  Consultants
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="/projects">
                  Projects
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="/entries">
                  Entries
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link text-white" href="/usermappings">
                  User Mapping
                </a>
              </li>
            </ul>

            <div className="d-flex">
              
              {user ? (
                <>
                  <span className="navbar-text text-white me-2">{user}</span>
                  <button
                    className="btn btn-outline-light"
                    onClick={handleLogout}
                  >
                    Logout
                  </button>
                </>
              ) : (
                <button
                  className="btn btn-outline-light"
                  onClick={handleLogin}
                >
                  Login
                </button>
              )}
            </div>
          </div>
        </nav>
      </header>
    </div>
  );
};

export default HeaderComponent;
