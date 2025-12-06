import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import React, { useState, useRef, useEffect } from "react";
import "./Chat.css";
import { useNavigate } from "react-router-dom";

console.log("Rendering Chat.jsx");

export default function Chat() {
  const navigate = useNavigate();

  const [messages, setMessages] = useState([
    { id: 1, role: "ai", text: "Hello!" },
    { id: 2, role: "ai", text: "How can I assist you today?" },
  ]);

  const [prompt, setPrompt] = useState("");
  const [loading, setLoading] = useState(false);
  const scrollRef = useRef(null);

  //  Auto scroll to the latest message
  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Send message handler
  const handleSend = async (e) => {
    e.preventDefault();
    if (!prompt.trim()) return;

    const userMsg = { id: Date.now(), role: "user", text: prompt.trim() };
    setMessages((prev) => [...prev, userMsg]);
    setPrompt("");
    setLoading(true);

    try {
      const token = localStorage.getItem("token");

      const response = await fetch("http://localhost:8080/api/chat/message", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ message: prompt.trim() })
      });

      if (response.ok) {
        const data = await response.json();
        setMessages((prev) => [
          ...prev,
          { id: Date.now() + 1, role: "ai", text: data.reply }
        ]);
      }
      else if (response.status === 401 || response.status === 403) {
        setMessages((prev) => [
          ...prev,
          { id: Date.now() + 1, role: "ai", text: "Unauthorized — please login again." }
        ]);
        localStorage.removeItem("token");
        window.location.href = "/login";
      }
      else {
        const errorText = await response.text();
        setMessages((prev) => [
          ...prev,
          { id: Date.now() + 1, role: "ai", text: errorText || "Error: AI not available right now." }
        ]);
      }
    } catch (err) {
      console.error("Network error:", err);
      setMessages((prev) => [
        ...prev,
        { id: Date.now() + 1, role: "ai", text: "Network error. Please try again." }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "flex-start",
        minHeight: "90vh",
        paddingTop: "200px",
        backgroundColor: "#f8f6f3",
      }}
    >
      {/* === Header === */}
      <header className="chat-header">
        <h2> Spring Bloom Project</h2>
        <button
          className="logout-btn"
          onClick={() => {
            localStorage.removeItem("token");
            navigate("/login");
          }}
        >
          Logout
        </button>
      </header>

      <h2
        style={{
          textAlign: "center",
          color: "#3a2f2f",
          fontWeight: "600",
          fontSize: "30px",
          marginBottom: "35px",
          marginTop: "10px",
          letterSpacing: "0.9px",
          fontFamily: "'Avenir', 'Helvetica Neue', sans-serif",
        }}
      >
        AI Chat Assistant
      </h2>

      <div className="chat-container">
        <div className="chat-box">
          {messages.map((msg, i) => (
            <div key={i} className={`message ${msg.role}`}>
              <ReactMarkdown remarkPlugins={[remarkGfm]}>
                {msg.text}
              </ReactMarkdown>
            </div>
          ))}
          <div ref={scrollRef} />
        </div>

        <form className="input-row" onSubmit={handleSend}>
          <input
            type="text"
            placeholder="Type your message..."
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            disabled={loading}
          />
          <button type="submit" disabled={loading}>
            {loading ? "..." : "Send"}
          </button>
        </form>
      </div>
    </div>
  );
}



