import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import StudentForm from "./forms/StudentForm";
import Card from "./forms/Card";
import { useLocation } from "react-router-dom";
import studentsPic from "./studentsPic.jpg";
import "./css/App.css";
import "react-toastify/dist/ReactToastify.css";

const DashboardPage = () => {
    const location = useLocation();
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { token: initialToken } = location.state || {};
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

    const fetchStudents = async () => {
        setLoading(true);
        try {
            if (initialToken !== "") {
                const response = await axios.get(
                    "http://localhost:8090/api/v1/student",
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
                setCountry(response.data);
            } else {
                setCountry("");
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
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    return (
        <div>
            <div className="image-container">
                <img src={studentsPic} alt="Students" className="scaled-image" />
            </div>
            <h2>Students Dashboard</h2>
            <div className="buttons-container">
                <button className="Addition" onClick={handleAdd}>
                    Add student
                </button>
                <button className="Update" onClick={handleUpdate}>
                    Update student
                </button>
                <button className="Delete" onClick={handleDelete}>
                    Delete student
                </button>
                <button className="GetCountry" onClick={fetchCountryByStudentId}>
                    Get country for student
                </button>
                <button className="GetByEmail" onClick={handleGetStudentByEmail}>
                    Get student by email
                </button>
                <button className="SubregionGet" onClick={handleGetRegionsByStudentId}>
                    Get subregions by student id
                </button>
            </div>
            <Card>
                <h2>Add/Update/Delete Student </h2>
                <StudentForm student={student} setStudent={setStudent} />
            </Card>

            <Card>
                <h2>Student Operations</h2>
                <ul>
                    <li>Get student by email</li>
                    <li>Delete student by ID</li>
                    <li>Get regions by student ID</li>
                </ul>
            </Card>
            {country && <p>Country: {country}</p>}
            <section className="subregions-list">
                <h2>Subregions List</h2>
                <ul>
                    {subregions?.length > 0 ? (
                        subregions.map((subregion, index) => (
                            <li key={index} className="subregion-item">
                                {subregion.region} {subregion.subregion}
                            </li>
                        ))
                    ) : (
                        <li>No subregions available.</li>
                    )}
                </ul>
            </section>
            <section className="students-list">
                <h2>Students List</h2>
                <ul>
                    {students?.length > 0 ? (
                        students.map((student, index) => (
                            <li key={index} className="student-item">
                                {student.firstName} {student.lastName}
                            </li>
                        ))
                    ) : (
                        <li>No students available.</li>
                    )}
                </ul>
            </section>

            <ToastContainer />
        </div>
    );
};

export default DashboardPage;
