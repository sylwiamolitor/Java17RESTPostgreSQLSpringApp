import React, {useEffect, useState} from 'react';
import axios from 'axios';
import logo from './logo.svg';
import studentsPic from './studentsPic.jpg';
import './css/App.css';
import RegisterForm from './forms/RegisterForm';
import Card from './forms/Card';
import AuthorizeForm from "./forms/AuthorizeForm";
import StudentForm from "./forms/StudentForm";

function App() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [token, setToken] = useState('');

    const handleInputChange = (event) => {
        setToken(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        localStorage.setItem('token', token);
        alert('Token saved successfully!');
        await fetchStudents();
    };

    const fetchStudents = async () => {
        setLoading(true);
        try {
            if (token !== '') {
                const response = await axios.get('http://localhost:8090/api/v1/student', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setStudents(response.data.content);
            }
        } catch (err) {
            setError(err);
            setStudents([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const savedToken = localStorage.getItem('token');
        if (savedToken) {
            setToken(savedToken);
            fetchStudents();
        } else {
            setLoading(false);
        }
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <div className="App">
            <header className="App-header">
                <div className="image-container">
                    <img src={studentsPic} alt="StudentsPic" className="scaled-image"/>
                </div>
                <p>
                    Students app.
                </p>
                Quick instruction:
                <li>Register using token, e-mail address and password.</li>
                <li>Authorize using token from the registration, e-mail address, password, first name and last name.
                </li>
                <li>Enter the token from the authorization and have fun using the application's functionalities :)</li>

                <Card>
                    <h2>Register</h2>
                    <RegisterForm/>
                    <h2>Authorize</h2>
                    <AuthorizeForm/>
                </Card>
                <h2>Enter Token</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={token}
                        onChange={handleInputChange}
                        placeholder="Enter token"
                        required
                    />
                    <button type="submit">Save Token</button>
                </form>
                <a
                    className="Authenticate"
                    href="/api/v1/auth/authenticate"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Authenticate
                </a>
                <Card><h2>Add student</h2>
                    <StudentForm/>
                </Card>
            </header>
            <h2>Students List</h2>
            <ul>
                {students.map(student => (
                    <li key={student.id}>{student.firstName}</li>
                ))}
            </ul>
            <img src={logo} className="App-logo" alt="logo"/>
        </div>
    );
}

export default App;