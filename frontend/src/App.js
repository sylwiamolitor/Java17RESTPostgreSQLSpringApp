import React, { useEffect, useState } from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';

function App() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const token = 'TODO';

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const response = await axios.get('http://localhost:8090/api/v1/student', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setStudents(response.data.content);
                setLoading(false);
            } catch (err) {
                setError(err);
                setLoading(false);
            }
        };

        fetchStudents();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error fetching students: {error.message}</p>;

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <p>
                    Students app.
                </p>
                <a
                    className="Authenticate"
                    href="/api/v1/auth/authenticate"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Authenticate
                </a>
            </header>
            <h2>Students List</h2>
            <ul>
                {students.map(student => (
                    <li key={student.id}>{student.firstName}</li>
                ))}
            </ul>
        </div>
    );
}

export default App;