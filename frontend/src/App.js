import React, {useEffect, useState} from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';
import RegisterForm from './RegisterForm';
import Card from './Card';

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
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Students app.
                </p>
                <h2>Enter credentials</h2>
                <Card>
                    <RegisterForm />
                </Card>
                <h2>Enter Token</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={token}
                        onChange={handleInputChange}
                        placeholder="Enter your token"
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