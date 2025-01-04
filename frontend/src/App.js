import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import RegisterPage from "./RegisterPage";
import AuthenticatePage from "./AuthenticatePage";
import DashboardPage from "./DashboardPage";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import FirstPage from "./FirstPage";
import "./css/App.css";

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<FirstPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/authenticate" element={<AuthenticatePage />} />
                    <Route path="/dashboard" element={<DashboardPage />} />
                </Routes>
                <ToastContainer />
            </div>
        </Router>
    );
}

export default App;
