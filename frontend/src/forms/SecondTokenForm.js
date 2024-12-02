import React, { useState } from 'react';

const SecondTokenForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);


    const togglePasswordVisibility = () => {
        setShowPassword((prev) => !prev);
    };

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log('Submitted Email:', email);
        console.log('Submitted Password:', password);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="email">Email:</label>
                <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={handleEmailChange}
                    required
                    placeholder="Enter your email"
                />
            </div>
            <div>
                <label htmlFor="password">Password:</label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="password"
                    value={password}
                    onChange={handlePasswordChange}
                    required
                    placeholder="Enter your password"
                />
                <button type="button" onClick={togglePasswordVisibility}>
                    {showPassword ? "Hide" : "Show"} Password
                </button>
            </div>
            <button type="submit">Save</button>
        </form>
    );
};

export default SecondTokenForm;
