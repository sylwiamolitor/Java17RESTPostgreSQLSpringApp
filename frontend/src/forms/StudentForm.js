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
          <label>
            Id:
            <input className="input-field"
              type="number"
              name="id"
              value={student.id}
              onChange={handleChange}
            />
          </label>
        </div>
        <div>
          <label>
            First Name:
            <input className="input-field"
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
            <input className="input-field"
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
            <input className="input-field"
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
            <input className="input-field"
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
            <input className="input-field"
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
