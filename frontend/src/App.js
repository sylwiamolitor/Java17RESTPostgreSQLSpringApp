import React, {useEffect, useState} from 'react';
import axios from 'axios';
import logo from './logo.svg';
import studentsPic from './studentsPic.jpg';
import './css/App.css';
import AuthenticateForm from './forms/AuthenticateForm';
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
            await fetchStudents();
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
                <img src={studentsPic} alt="Students" className="scaled-image" />
            </div>
            <h1>Students App</h1>
            <p>Quick Instructions:</p>
            <ul>
                <li>Register using token, e-mail address, and password.</li>
                <li>Authorize using token, e-mail address, password, first name, and last name.</li>
                <li>Enter the token from the authorization and have fun using the application's functionalities :)</li>
            </ul>

            <Card>
                <h2>Register</h2>
                <RegisterForm setFirstName={setFirstName} firstName={firstName} setLastName={setLastName} lastName={lastName} />
            </Card>

            <Card>
                <h2>Authenticate</h2>
                <AuthenticateForm setEmail={setEmail} setPassword={setPassword} password={password} email={email} />
            </Card>

            <Card>
                <h3>Enter Token (current: {buttonText})</h3>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={token}
                        onChange={handleInputChange}
                        placeholder="Enter token"
                        required
                        className="token-input"
                    />
                    <button type="submit" className="token-button">Save Token</button>
                </form>
            </Card>

            <div className="buttons-container">
                <button className="Register" onClick={handleRegister}>Register</button>
                <button className="Authentication" onClick={handleAuth}>Authenticate</button>
            </div>

            <Card>
                <h2>Add/Update Student</h2>
                <StudentForm />
            </Card>

            <Card>
                <h2>Student Operations</h2>
                <ul>
                    <li>Get student by email</li>
                    <li>Delete student by ID</li>
                    <li>Get regions by student ID</li>
                </ul>
            </Card>
        </header>

        <section className="students-list">
            <h2>Students List</h2>
            <ul>
                {students.map(student => (
                    <li key={student.id}>{student.firstName}</li>
                ))}
            </ul>
        </section>

        <img src={logo} className="App-logo" alt="logo" />
    </div>
);

}

export default App;