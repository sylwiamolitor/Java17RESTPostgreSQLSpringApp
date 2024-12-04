import React from 'react';

const RegisterForm = ({ setFirstName, firstName, setLastName, lastName }) => {

    const handleFirstNameChange = (event) => {
        setFirstName(event.target.value);
    };

    const handleLastNameChange = (event) => {
        setLastName(event.target.value);
    };

    return (
        <form>
            <div>
                <label>
                    First Name:
                    <input
                        type="text"
                        name="firstName"
                        value={firstName}
                        onChange={handleFirstNameChange}
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
                        onChange={handleLastNameChange}
                    />
                </label>
            </div>
        </form>
    );
};

export default RegisterForm;