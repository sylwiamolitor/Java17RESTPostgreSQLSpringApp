import React from "react";

const StudentForm = ({ student, setStudent }) => {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setStudent({ ...student, [name]: value });
  };

  return (
    <div>
      <form>
        <div>
          <label className="full-width-label">
            Id:
            <input className="input-field"
              type="number"
              name="id"
              value={student.id}
              onChange={handleChange}
              placeholder="Enter ID"
            />
          </label>
        </div>
        <div>
          <label className="full-width-label">
            First Name:
            <input className="input-field"
              type="text"
              name="firstName"
              value={student.firstName}
              onChange={handleChange}
              placeholder="Enter first name"
            />
          </label>
        </div>
        <div>
          <label className="full-width-label">
            Last Name:
            <input className="input-field"
              type="text"
              name="lastName"
              value={student.lastName}
              onChange={handleChange}
              placeholder="Enter last name"
            />
          </label>
        </div>
        <div>
          <label className="full-width-label">
            Email:
            <input className="input-field"
              type="email"
              name="email"
              value={student.email}
              onChange={handleChange}
              placeholder="Enter email address"
            />
          </label>
        </div>
        <div>
          <label className="full-width-label">
            Date of Birth:
            <input className="input-field"
              type="date"
              name="dateOfBirth"
              value={student.dateOfBirth}
              onChange={handleChange}
              placeholder="Enter birth date"
            />
          </label>
        </div>
        <div>
          <label className="full-width-label">
            Country:
            <input className="input-field"
              type="text"
              name="country"
              value={student.country}
              onChange={handleChange}
              placeholder="Enter country"
            />
          </label>
        </div>
      </form>
    </div>
  );
};

export default StudentForm;
