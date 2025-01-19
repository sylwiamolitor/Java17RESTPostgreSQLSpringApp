import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import StudentForm from "./forms/StudentForm";
import Card from "./forms/Card";
import { useLocation, useNavigate } from "react-router-dom";
import studentsPic from "./studentsPic.jpg";
import "./css/App.css";
import "react-toastify/dist/ReactToastify.css";
import { Popup } from './forms/Popup';
import "./css/Button.css";
import "./css/Input.css";
import "./css/Card.css";

const DashboardPage = () => {
    const location = useLocation();
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { token: initialToken } = location.state || {};
    const navigate = useNavigate();
    const [student, setStudent] = useState({
        id: "",
        email: "",
        lastName: "",
        firstName: "",
        dateOfBirth: "2000-03-01",
        country: "",
    });
    const [country, setCountry] = useState("");
    const [subregion, setSubregion] = useState({
        region: "",
        subregion: "",
    });
    const [subregions, setSubregions] = useState([]);
    const [open, setOpen] = useState(false);
    const [popupText, setPopupText] = useState('');

    const fetchStudents = async () => {
        setLoading(true);
        try {
            if (initialToken !== "") {
                const response = await axios.get(
                    `api/v1/student`,
                    {
                        headers: {
                            Authorization: `Bearer ${initialToken}`,
                        },
                    }
                );
                setStudents(response.data.content);
            }
        } catch (err) {
            setError(err);
            toast.error(`Error fetching students: ${err.message}`);
            setStudents([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (initialToken) {
            fetchStudents();
        } else {
            setLoading(false);
        }
    }, []);

    const handleStudentAction = async (method, url, data) => {
        try {
            if (!initialToken) {
                toast.warning("Please register and authenticate to generate token!");
                return;
            }
            const response = await axios({
                method,
                url,
                data,
                headers: { Authorization: `Bearer ${initialToken}` },
            });
            toast.success(response.data);
            if (response.status !== 226 && response.status !== 404)
                await fetchStudents();
        } catch (error) {
            const errorMessage =
                error.response?.data || error.message || "An unknown error occurred";
            toast.error(
                `${
                    method.charAt(0).toUpperCase() + method.slice(1)
                } failed: ${errorMessage}`
            );
        }
    };
    const handleDelete = () => {
        if (!student.id) {
            toast.warning("Student ID is required for deletion!");
            return;
        }
        handleStudentAction("delete", `/api/v1/student/${student.id}`);
    };

    const handleAdd = () => {
        if (
            !student.firstName ||
            !student.lastName ||
            !student.email ||
            !student.dateOfBirth
        ) {
            toast.warning("Student data is required to update the student!");
            return;
        }
        handleStudentAction("post", "/api/v1/student/addStudent", student);
    };

    const handleUpdate = () => {
        if (!student.id) {
            toast.warning("Student ID is required for updating!");
            return;
        }
        if (
            !student.firstName ||
            !student.lastName ||
            !student.email ||
            !student.dateOfBirth
        ) {
            toast.warning("Student data is required to update the student!");
            return;
        }
        handleStudentAction("put", `/api/v1/student/${student.id}`, student);
    };
    const handleGetRegionsByStudentId = async () => {
        try {
            if (!initialToken) {
                toast.warning("Please register and authenticate to generate token!");
                return;
            }
            if (!student.id) {
                toast.warning("Please fill in student id!");
                return;
            }
            const response = await axios.get(
                `/api/v1/student/regionsByCountry/${student.id}`,
                {
                    headers: {
                        Authorization: `Bearer ${initialToken}`,
                    },
                }
            );
            if (response.status === 200) {
                setSubregions(response.data.pagedContent);
            }
        } catch (error) {
            console.error("There was a problem:", error);
            toast.error(
                `Fetch regions failed: ${error.response?.data || error.message}`
            );
        }
    };

    const fetchCountryByStudentId = async () => {
        try {
            if (!initialToken) {
                toast.warning("Please register and authenticate to generate token!");
                return;
            }
            if (!student.id) {
                toast.warning("Please fill in student id!");
                return;
            }
            const response = await axios.get(`/api/v1/student/id/${student.id}`, {
                headers: { Authorization: `Bearer ${initialToken}` },
            });
            if (response.status === 200) {
                setOpen(true);
                setPopupText("Country: " + response.data);
            }
        } catch (err) {
            console.error(err);
            setCountry("");
            toast.error(`Fetch country failed: ${err.message}`);
        }
    };
    const handleGetStudentByEmail = async () => {
        try {
            if (!initialToken) {
                toast.warning("Please register and authenticate to generate token!");
                return;
            }
            if (!student.email) {
                toast.warning("Please fill in student email!");
                return;
            }
            const response = await axios.get(`/api/v1/student/${student.email}`, {
                headers: {
                    Authorization: `Bearer ${initialToken}`,
                },
            });
            if (response.status === 200) {
                const updatedStudent = {
                    ...student,
                    id: response.data.id || student.id,
                    email: response.data.email || student.email,
                    lastName: response.data.lastName || student.lastName,
                    firstName: response.data.firstName || student.firstName,
                    dateOfBirth: response.data.dateOfBirth || student.dateOfBirth,
                    country: response.data.country || student.country,
                };
                toast.success(response.data);
                setStudent(updatedStudent);
                setOpen(true);
                setPopupText(
                    'Email: ' + updatedStudent.email + '\n' +
                    'First Name: ' + updatedStudent.firstName + '\n' +
                    'Last Name: ' + updatedStudent.lastName + '\n' +
                    'Date of Birth: ' + updatedStudent.dateOfBirth + '\n' +
                    'Country: ' + updatedStudent.country
                );
            }
        } catch (error) {
            console.error("There was a problem:", error);
            toast.error(
                `Fetch student by email failed: ${
                    error.response?.data || error.message
                }`
            );
        }
    };
    const handleLogout = () => {
        navigate("/");
    };
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    return (
        <div>
            <div>
                {open ? <Popup text={popupText} closePopup={() => setOpen(false)} /> : null}
            </div>
            <div className="image-container">
                <img src={studentsPic} alt="Students" className="scaled-image" />
            </div>
            <h2>Students Dashboard</h2>
            <div className="buttons-container">
                <button className="pretty-button" onClick={handleAdd}>
                    Add student
                </button>
                <button className="pretty-button" onClick={handleUpdate}>
                    Update student
                </button>
                <button className="pretty-button" onClick={handleDelete}>
                    Delete student
                </button>
                <button className="pretty-button" onClick={fetchCountryByStudentId}>
                    Get country for student
                </button>
                <button className="pretty-button" onClick={handleGetStudentByEmail}>
                    Get student by email
                </button>
                <button className="pretty-button" onClick={handleGetRegionsByStudentId}>
                    Get subregions by student id
                </button>
                <button className="pretty-button" onClick={handleLogout}>
                    Logout
                </button>
            </div>
            <Card>
                <h2>Add/Update/Delete Student </h2>
                <StudentForm student={student} setStudent={setStudent} />
            </Card>
            <Card>
            <section className="custom-list">
                <h2>Subregions List</h2>
                <ul>
                    {subregions?.length > 0 ? (
                        subregions.map((subregion, index) => (
                            <li key={index} className="custom-item">
                                {subregion.region} {subregion.subregion}
                            </li>
                        ))
                    ) : (
                        <li>No subregions available.</li>
                    )}
                </ul>
            </section>
            </Card>
            <Card>
            <section className="custom-list">
                <h2>Students List</h2>
                <ul>
                    {students?.length > 0 ? (
                        students.map((student, index) => (
                            <li key={index} className="custom-item">
                                {student.firstName} {student.lastName}
                            </li>
                        ))
                    ) : (
                        <li>No students available.</li>
                    )}
                </ul>
            </section>
            </Card>
            <ToastContainer />
        </div>
    );
};

export default DashboardPage;
