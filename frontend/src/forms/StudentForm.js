import React, { useState } from 'react';

const StudentForm = () => {
    const [student, setStudent] = useState({
        email: '',
        lastName: '',
        firstName: '',
        dateOfBirth: '1900-03-01',
        country: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setStudent({ ...student, [name]: value });
    };

    return (
        <div>
            <form>
                <div>
                    <label>
                        First Name:
                        <input
                            type="text"
                            name="firstName"
                            value={student.firstName}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Last Name:
                        <input
                            type="text"
                            name="lastName"
                            value={student.lastName}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Email:
                        <input
                            type="email"
                            name="email"
                            value={student.email}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Date of Birth:
                        <input
                            type="date"
                            name="dateOfBirth"
                            value={student.dateOfBirth}
                            onChange={handleChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Country:
                        <input
                            type="text"
                            name="country"
                            value={student.country}
                            onChange={handleChange}
                        />
                    </label>
                </div>
            </form>
        </div>
    );
};

export default StudentForm;
