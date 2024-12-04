import React, {useEffect, useState} from 'react';
import axios from 'axios';
import logo from './logo.svg';
import studentsPic from './studentsPic.jpg';
import './css/App.css';
import AuthorizeForm from './forms/AuthorizeForm';
import Card from './forms/Card';
import RegisterForm from "./forms/RegisterForm";
import StudentForm from "./forms/StudentForm";

function App() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [token, setToken] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [buttonText, setButtonText] = useState('own');

    const handleInputChange = (event) => {
        setToken(event.target.value);
        setButtonText('own');
    };

    const handleSubmit = async (event) => {
        localStorage.setItem('token', token);
        setButtonText('own');
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
        } else {
            setLoading(false);
        }
    }, []);
    const handleRegister = async () => {
        try {
            const response = await axios.post('/api/v1/auth/register', {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('Registration successful:', response.data);
            setToken(response.data.token)
            setButtonText('correct registration');
        } catch (error) {
            console.error('There was a problem with the registration operation:', error);
        }
    };
    const handleAuth = async () => {
        try {
            const response = await axios.post('/api/v1/auth/authenticate', {
                email: email,
                password: password
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setToken(response.data.token)
            setButtonText('correct authentication');
            console.log('Authentication successful:', response.data);
        } catch (error) {
            console.error('There was a problem with the authentication operation:', error);
        }
    };
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
                    <RegisterForm setFirstName={setFirstName} firstName={firstName} setLastName={setLastName}
                                  lastName={lastName}/>
                    <h2>Authorize</h2>
                    <AuthorizeForm setEmail={setEmail} setPassword={setPassword} password={password} email={email}/>
                </Card>
                <h3>Enter Token (current: {buttonText})</h3>
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
                <button
                    className="Register"
                    onClick={handleRegister}
                    rel="noopener noreferrer"
                >
                    Register
                </button>
                <button
                    className="Authentication"
                    onClick={handleAuth}
                    rel="noopener noreferrer"
                >
                    Authentication
                </button>
                <Card><h2>Add/update student</h2>
                    <StudentForm/>
                </Card>
                <Card>
                    <h2>Get student by email</h2>
                    <h2>Delete student by id</h2>
                    <h2>Get regions by student id.</h2>
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