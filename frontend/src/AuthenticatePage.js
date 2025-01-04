import React, { useState } from "react";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import EmailPasswordForm from "./forms/EmailPasswordForm";
import Card from "./forms/Card";
import { useNavigate, useLocation } from "react-router-dom";
import "./css/App.css";
import "react-toastify/dist/ReactToastify.css";

const AuthenticatePage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const location = useLocation();
    const { token: initialToken } = location.state || {};
    const navigate = useNavigate();
    const isEmpty = (value) =>
        value === undefined || value === null || value === "";

    const handleAuth = async () => {
        try {
            if (!email || !password) {
                toast.warning("Please fill in email and password!");
                return;
            }
            if (isEmpty(initialToken)) {
                toast.warning("Please register to generate token!");
                return;
            }
            const response = await axios.post(
                "/api/v1/auth/authenticate",
                {
                    email: email,
                    password: password,
                },
                {
                    headers: {
                        Authorization: `Bearer ${initialToken}`,
                    },
                }
            );
            console.log("Authentication successful:", response.data);
            toast.success(response.data);
            const token = response.data.token;
            navigate("/dashboard", { state: { token } });
        } catch (error) {
            console.error(
                "There was a problem with the authentication operation:",
                error,
                ". Check if the account exists."
            );
            toast.error(
                `Authentication failed: ${
                    error.response?.data || error.message
                }. Check if the account exists.`
            );
        }
    };

    return (
        <div>
            <h2>Authenticate</h2>
            <Card>
                <EmailPasswordForm
                    setEmail={setEmail}
                    setPassword={setPassword}
                    email={email}
                    password={password}
                />
                <button onClick={handleAuth}>Authenticate</button>
            </Card>
            <ToastContainer />
        </div>
    );
};

export default AuthenticatePage;
