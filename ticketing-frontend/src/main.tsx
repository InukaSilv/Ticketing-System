import React from "react";
import ReactDOM from "react-dom/client";
import App from "./pages/App";
import "./pages/index.css"; // Tailwind CSS styles

const rootElement = document.getElementById("root");

if (rootElement) {
  ReactDOM.createRoot(rootElement).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
} else {
  console.error("Root element not found!");
}
