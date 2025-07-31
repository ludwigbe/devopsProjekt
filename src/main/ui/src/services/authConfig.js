export const msalConfig = {
    auth: {
        clientId: "9be7df1a-b965-4330-a5f2-012949fcc0b6", // Application (client) ID
        authority: "https://login.microsoftonline.com/09709c98-6093-4ae4-a11b-88a046eb1b11", // Directory (tenant) ID
        redirectUri: "http://localhost:3000/", // L'URL où l'utilisateur sera redirigé après connexion
    },
    cache: {
        cacheLocation: "localStorage", // Stocker le token dans le localStorage
        storeAuthStateInCookie: true, // Pour les navigateurs plus anciens
    },
};

export const loginRequest = {
    scopes: ["openid", "profile", "offline_access"],
};
