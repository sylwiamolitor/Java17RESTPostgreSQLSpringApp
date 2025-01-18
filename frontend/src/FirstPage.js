import React from "react";
import { useNavigate } from "react-router-dom";
import Card from "./forms/Card";
import "./css/App.css";
import "./css/Button.css";
import "./css/Card.css";

const FirstPage = () => {
    const navigate = useNavigate();

    const handleRegister = (event) => {
        event.preventDefault();
        navigate("/register");
    };
    const handleAuth = (event) => {
        event.preventDefault();
        navigate("/authenticate");
    };

    return (
        <form>
            <Card>
                <button className="pretty-button" onClick={handleRegister}>
                    Register
                </button>
                <button className="pretty-button" onClick={handleAuth}>
                    Authenticate
                </button>
            </Card>
        </form>
    );
};

export default FirstPage;
