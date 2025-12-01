import React from "react";
import "../App.css";
import { useNavigate } from "react-router-dom";

function HomePage({ Username }) {
  const navigate = useNavigate();

 // Handle logout (clears token and returns to Login page)
   const handleLogout = () => {
       // Clear authentication token
     localStorage.removeItem("token");
     navigate("/login", { replace: true });
   };

  return (
    <div className="card" style={{ textAlign: "center" }}>
      <h1>Welcome to Spring-bloom</h1>
      <p>{Username ? `Hello, ${Username}!` : "You are logged in."}</p>

      {/* Logout button */}
      <button
        onClick={handleLogout}
        style={{
          marginTop: "20px",
          padding: "8px 16px",
          borderRadius: "6px",
          border: "none",
          backgroundColor: "#b59a6a", // your warm beige tone
          color: "white",
          cursor: "pointer",
          fontWeight: "500",
          transition: "background 0.3s",
        }}
        onMouseOver={(e) => (e.target.style.backgroundColor = "#9b8355")}
        onMouseOut={(e) => (e.target.style.backgroundColor = "#b59a6a")}
      >
        Logout
      </button>
    </div>
  );
}

export default HomePage;
