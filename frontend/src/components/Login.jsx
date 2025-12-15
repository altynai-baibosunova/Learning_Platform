 import React, { useState } from "react";
 import "../App.css";
 import { useLocation, useNavigate } from "react-router-dom";
 import { useEffect } from "react";

 function Login({ onSwitchToRegister }) {
   // State variables for user input
   const [email, setEmail] = useState("");
   const [password, setPassword] = useState("");
   const [message, setMessage] = useState("");

   const navigate = useNavigate();
   const location = useLocation();

   // Show alert message if redirected from a protected route
     useEffect(() => {
       if (location.state?.msg) {
         alert(location.state.msg);
       }
     }, [location.state]);

   // Handle login form submission
   const handleLogin = async (e) => {
     e.preventDefault();

     const loginData = { email, password };

     try {
       const response = await fetch("http://localhost:8080/api/auth/login", {
         method: "POST",
         headers: { "Content-Type": "application/json" },
         body: JSON.stringify(loginData),
       });

       if (response.ok) {
         const data = await response.json(); // await обязательно
         console.log("Login response:", data);

         if (data.token) {
           localStorage.setItem("token", data.token); // сохраняем JWT
           setMessage(data.message || "Login successful");
           navigate("/chat", { replace: true }); // переходим в чат
         } else {
           setMessage("No token received from server");
         }
       } else {
         const err = await response.text();
         console.error("Login failed:", err);
         setMessage("Invalid email or password");
       }
     } catch (error) {
       console.error("Login error:", error);
       setMessage("Something went wrong");
     }
   };


   return (
     <div className="form-container">
       <div className="form-box">
         <h2>User Login</h2>

         <form onSubmit={handleLogin}>
           <input
             type="email"
             placeholder="Email"
             value={email}
             onChange={(e) => setEmail(e.target.value)}
             required
           />

           <input
             type="password"
             placeholder="Password"
             value={password}
             onChange={(e) => setPassword(e.target.value)}
             required
           />

           <button type="submit">Login</button>
         </form>

         <p>{message}</p>

         <p style={{ marginTop: "20px" }}>
           Don't have an account?{" "}
           <button
             onClick={() => navigate("/register")}
             style={{
               background: "none",
               border: "none",
               color: "#007bff",
               cursor: "pointer",
               padding: 0,
               fontSize: "inherit",
               textDecoration: "underline"
             }}
           >
             Register
           </button>
         </p>
       </div>
     </div>
   );
 }

 export default Login;
