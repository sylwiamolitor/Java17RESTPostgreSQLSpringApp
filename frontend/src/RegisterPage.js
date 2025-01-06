import React, { useState } from "react";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";
import EmailPasswordForm from "./forms/EmailPasswordForm";
import Card from "./forms/Card";
import "react-toastify/dist/ReactToastify.css";
import "./css/App.css";

const RegisterPage = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      if (!firstName || !lastName || !email || !password) {
        toast.warning("All fields are required!");
        return;
      }

      const response = await axios.post("/api/v1/auth/register", {
        firstName,
        lastName,
        email,
        password,
      });
      console.log("Registration successful:", response.data);
      toast.success("Registration successful!");
      const token = response.data.token;
      navigate("/dashboard", { state: { token } });
    } catch (error) {
      console.error(
          "There was a problem with the registration operation:",
          error
      );
      toast.error(
          `Registration failed: ${error.response?.data || error.message}`
      );
    }
  };

  return (
      <form>
        <Card>
          <h2>Register</h2>
          <div>
            <label>
              First Name:
              <input
                  type="text"
                  name="firstName"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
              />
            </label>
          </div>
          <div>
            <label>
              Last Name:
              <input
                  type="text"
                  name="lastName"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
              />
            </label>
          </div>
          <EmailPasswordForm
              setEmail={setEmail}
              setPassword={setPassword}
              password={password}
              email={email}
          />
          <button className="Register" onClick={handleRegister}>
            Register
          </button>
        </Card>
        <ToastContainer />
      </form>
  );
};

export default RegisterPage;
