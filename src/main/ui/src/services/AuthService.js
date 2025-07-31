class AuthService {
  static login() {
    window.location.href = "http://localhost:8080/oauth2/authorization/azure";
  }

  static async getCurrentUser() {
    const response = await fetch("http://localhost:8080/api/auth/home", {
      credentials: "include",
    });
    if (response.ok) {
      return response.json();
    }
    return null;
  }
  static logout() {
    window.location.href = "http://localhost:8080/logout";
  }
}

export default AuthService;
