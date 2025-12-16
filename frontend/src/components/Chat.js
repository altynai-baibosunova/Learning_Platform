import React, { useState } from "react";
import "./Chat.css";

function Chat() {
  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);

  const handleSend = async () => {
    if (!message.trim()) return;

    const newMessage = { role: "user", text: message };
    setChatHistory([...chatHistory, newMessage]);
    setMessage("");

    try {
      const response = await fetch("http://localhost:8080/api/chat", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({ message }),
      });

      if (!response.ok) throw new Error("Network error");
      const data = await response.json();

      setChatHistory((prev) => [...prev, { role: "ai", text: data.response }]);
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-box">
        {chatHistory.map((msg, i) => (
          <div key={i} className={`message ${msg.role}`}>
            {msg.text}
          </div>
        ))}
      </div>
      <div className="chat-input">
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type your message..."
        />
        <button onClick={handleSend}>Send</button>
      </div>
    </div>
  );
}

export default Chat;
