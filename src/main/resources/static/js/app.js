// =========================================
// API CONFIGURATION
// =========================================

const API_BASE_URL = "http://localhost:8080";


// =========================================
// APPLICATION STATE
// =========================================

let currentPage = 0;
let pageSize = 5;
let totalPages = 0;
let editingEmployeeId = null;
let authToken = null;
let userRole = null;


// =========================================
// LOGIN ELEMENTS
// =========================================

const loginPage =
    document.getElementById("loginPage");

const appPage =
    document.getElementById("appPage");

const loginForm =
    document.getElementById("loginForm");

const loginUsername =
    document.getElementById("loginUsername");

const loginPassword =
    document.getElementById("loginPassword");

const loginError =
    document.getElementById("loginError");

const togglePassword =
    document.getElementById("togglePassword");

const logoutButton =
    document.getElementById("logoutButton");

const loggedInUsername =
    document.getElementById("loggedInUsername");

const loggedInRole =
    document.getElementById("loggedInRole");


// =========================================
// EMPLOYEE FORM ELEMENTS
// =========================================

const employeeForm =
    document.getElementById("employeeForm");

const employeeFormSection =
    document.getElementById("employeeFormSection");

const employeeFormTitle =
    document.getElementById("employeeFormTitle");

const addEmployeeButton =
    document.getElementById("addEmployeeButton");

const cancelEmployeeButton =
    document.getElementById("cancelEmployeeButton");


// =========================================
// EMPLOYEE TABLE
// =========================================

const employeeTableBody =
    document.getElementById("employeeTableBody");

const actionsHeader =
    document.getElementById("actionsHeader");


// =========================================
// SEARCH
// =========================================

const departmentInput =
    document.getElementById("department");

const statusInput =
    document.getElementById("status");

const searchButton =
    document.getElementById("searchButton");

const resetButton =
    document.getElementById("resetButton");


// =========================================
// SORTING
// =========================================

const sortBy =
    document.getElementById("sortBy");

const sortDirection =
    document.getElementById("sortDirection");


// =========================================
// PAGINATION
// =========================================

const previousButton =
    document.getElementById("previousButton");

const nextButton =
    document.getElementById("nextButton");

const pageInfo =
    document.getElementById("pageInfo");

const pageSizeSelect =
    document.getElementById("pageSize");


// =========================================
// STATISTICS
// =========================================

const totalEmployeesElement =
    document.getElementById("totalEmployees");

const activeEmployeesElement =
    document.getElementById("activeEmployees");

const inactiveEmployeesElement =
    document.getElementById("inactiveEmployees");

const totalDepartmentsElement =
    document.getElementById("totalDepartments");


// =========================================
// PASSWORD SHOW / HIDE
// =========================================

togglePassword.addEventListener(
    "click",
    function () {

        if (loginPassword.type === "password") {

            loginPassword.type = "text";

            togglePassword.textContent = "🙈";

            togglePassword.setAttribute(
                "aria-label",
                "Hide password"
            );

        } else {

            loginPassword.type = "password";

            togglePassword.textContent = "👁";

            togglePassword.setAttribute(
                "aria-label",
                "Show password"
            );

        }

    }
);


// =========================================
// AUTHORIZATION HEADERS
// =========================================

function getAuthHeaders() {

    return {

        "Content-Type": "application/json",

        "Authorization": `Bearer ${authToken}`

    };

}


// =========================================
// ROLE BASED UI
// =========================================

function applyRoleBasedUI() {

    const isAdmin =
        userRole === "ADMIN";


    // Add employee button

    if (isAdmin) {

        addEmployeeButton.style.display =
            "inline-block";

    } else {

        addEmployeeButton.style.display =
            "none";

    }


    // Employee form

    if (!isAdmin) {

        employeeFormSection.style.display =
            "none";

    }


    // Actions header

    if (actionsHeader) {

        actionsHeader.style.display =
            isAdmin ? "" : "none";

    }

}


// =========================================
// LOGIN
// =========================================

loginForm.addEventListener(
    "submit",

    async function (event) {

        event.preventDefault();


        const username =
            loginUsername.value.trim();

        const password =
            loginPassword.value.trim();


        loginError.textContent = "";


        try {

            const response =
                await fetch(

                    `${API_BASE_URL}/api/auth/login`,

                    {

                        method: "POST",

                        headers: {

                            "Content-Type":
                                "application/json"

                        },

                        body: JSON.stringify({

                            username: username,

                            password: password

                        })

                    }

                );


            if (!response.ok) {

                throw new Error(
                    "Invalid username or password"
                );

            }


            const loginData =
                await response.json();


            // Save token

            authToken =
                loginData.token;


            // Save user role

            userRole =
                loginData.role;


            // Save login information

            localStorage.setItem(
                "authToken",
                authToken
            );

            localStorage.setItem(
                "username",
                loginData.username
            );

            localStorage.setItem(
                "role",
                userRole
            );


            // Show application

            loginPage.style.display =
                "none";

            appPage.style.display =
                "block";


            // Display user information

            loggedInUsername.textContent =
                loginData.username;

            loggedInRole.textContent =
                userRole;


            // Apply role permissions

            applyRoleBasedUI();


            // Reset page

            currentPage = 0;


            // Load data

            await loadEmployees();

            await loadDashboardStatistics();


            // Reset login form

            loginForm.reset();

            loginError.textContent = "";


        } catch (error) {

            console.error(
                "Login error:",
                error
            );

            loginError.textContent =
                "Invalid username or password.";

        }

    }
);


// =========================================
// LOGOUT
// =========================================

logoutButton.addEventListener(
    "click",

    function () {

        localStorage.removeItem(
            "authToken"
        );

        localStorage.removeItem(
            "username"
        );

        localStorage.removeItem(
            "role"
        );


        authToken = null;

        userRole = null;

        editingEmployeeId = null;


        appPage.style.display =
            "none";

        loginPage.style.display =
            "flex";


        loginUsername.value = "";

        loginPassword.value = "";

        loginPassword.type = "password";

        togglePassword.textContent = "👁";


        loginError.textContent = "";


        employeeForm.reset();

        employeeFormSection.style.display =
            "none";

    }
);


// =========================================
// CHECK EXISTING LOGIN
// =========================================

window.addEventListener(
    "DOMContentLoaded",

    async function () {

        const savedToken =
            localStorage.getItem(
                "authToken"
            );

        const savedUsername =
            localStorage.getItem(
                "username"
            );

        const savedRole =
            localStorage.getItem(
                "role"
            );


        if (
            savedToken &&
            savedUsername &&
            savedRole
        ) {

            authToken =
                savedToken;

            userRole =
                savedRole;


            loginPage.style.display =
                "none";

            appPage.style.display =
                "block";


            loggedInUsername.textContent =
                savedUsername;

            loggedInRole.textContent =
                savedRole;


            applyRoleBasedUI();


            try {

                await loadEmployees();

                await loadDashboardStatistics();

            } catch (error) {

                console.error(
                    "Session error:",
                    error
                );

                logoutButton.click();

            }

        }

    }
);


// =========================================
// LOAD EMPLOYEES
// PAGINATION + SEARCH + SORTING
// =========================================

async function loadEmployees() {

    try {

        const department =
            departmentInput.value.trim();

        const status =
            statusInput.value.trim();

        const selectedSortBy =
            sortBy.value;

        const direction =
            sortDirection.value;


        let url =
            `${API_BASE_URL}/api/employees/search?` +
            `page=${currentPage}` +
            `&size=${pageSize}` +
            `&sortBy=${encodeURIComponent(selectedSortBy)}` +
            `&direction=${encodeURIComponent(direction)}`;


        if (department !== "") {

            url +=
                `&department=${encodeURIComponent(
                    department
                )}`;

        }


        if (status !== "") {

            url +=
                `&status=${encodeURIComponent(
                    status
                )}`;

        }


        const response =
            await fetch(

                url,

                {

                    method: "GET",

                    headers:
                        getAuthHeaders()

                }

            );


        if (response.status === 401) {

            logoutButton.click();

            throw new Error(
                "Session expired"
            );

        }


        if (!response.ok) {

            throw new Error(
                "Failed to load employees"
            );

        }


        const pageData =
            await response.json();


        displayEmployees(
            pageData.content
        );


        totalPages =
            pageData.totalPages;


        // Prevent invalid page after delete

        if (
            totalPages > 0 &&
            currentPage >= totalPages
        ) {

            currentPage =
                totalPages - 1;

            await loadEmployees();

            return;

        }


        updatePagination();


    } catch (error) {

        console.error(
            "Error loading employees:",
            error
        );


        employeeTableBody.innerHTML = `

            <tr>

                <td
                    colspan="${userRole === "ADMIN" ? 7 : 6}"
                    style="text-align:center;">

                    Failed to load employees.

                </td>

            </tr>

        `;

    }

}


// =========================================
// DISPLAY EMPLOYEES
// =========================================

function displayEmployees(employees) {

    employeeTableBody.innerHTML = "";


    const isAdmin =
        userRole === "ADMIN";


    if (
        !employees ||
        employees.length === 0
    ) {

        employeeTableBody.innerHTML = `

            <tr>

                <td
                    colspan="${isAdmin ? 7 : 6}"
                    style="text-align:center;">

                    No employees found.

                </td>

            </tr>

        `;

        return;

    }


    employees.forEach(
        function (employee) {

            const row =
                document.createElement("tr");


            let actionColumn = "";


            if (isAdmin) {

                actionColumn = `

                    <td>

                        <button
                            type="button"
                            class="edit-button"
                            onclick="editEmployee(${employee.id})">

                            Edit

                        </button>


                        <button
                            type="button"
                            class="delete-button"
                            onclick="deleteEmployee(${employee.id})">

                            Delete

                        </button>

                    </td>

                `;

            }


            row.innerHTML = `

                <td>
                    ${employee.id}
                </td>


                <td>
                    ${employee.firstName}
                    ${employee.lastName}
                </td>


                <td>
                    ${employee.department}
                </td>


                <td>
                    ${employee.designation}
                </td>


                <td>
                    ₹${Number(
                        employee.salary
                    ).toLocaleString("en-IN")}
                </td>


                <td>
                    ${employee.status}
                </td>


                ${actionColumn}

            `;


            employeeTableBody.appendChild(
                row
            );

        }
    );

}


// =========================================
// UPDATE PAGINATION
// =========================================

function updatePagination() {

    if (totalPages === 0) {

        pageInfo.textContent =
            "Page 0 of 0";

        previousButton.disabled =
            true;

        nextButton.disabled =
            true;

        return;

    }


    pageInfo.textContent =
        `Page ${currentPage + 1} of ${totalPages}`;


    previousButton.disabled =
        currentPage === 0;


    nextButton.disabled =
        currentPage >= totalPages - 1;

}


// =========================================
// PREVIOUS PAGE
// =========================================

previousButton.addEventListener(
    "click",

    async function () {

        if (currentPage > 0) {

            currentPage--;

            await loadEmployees();

        }

    }
);


// =========================================
// NEXT PAGE
// =========================================

nextButton.addEventListener(
    "click",

    async function () {

        if (
            currentPage <
            totalPages - 1
        ) {

            currentPage++;

            await loadEmployees();

        }

    }
);


// =========================================
// PAGE SIZE CHANGE
// =========================================

pageSizeSelect.addEventListener(
    "change",

    async function () {

        pageSize =
            Number(
                pageSizeSelect.value
            );

        currentPage = 0;

        await loadEmployees();

    }
);


// =========================================
// SEARCH
// =========================================

searchButton.addEventListener(
    "click",

    async function () {

        currentPage = 0;

        await loadEmployees();

    }
);


// =========================================
// RESET SEARCH
// =========================================

resetButton.addEventListener(
    "click",

    async function () {

        departmentInput.value = "";

        statusInput.value = "";

        sortBy.value = "id";

        sortDirection.value = "asc";

        currentPage = 0;

        await loadEmployees();

    }
);


// =========================================
// SHOW ADD EMPLOYEE FORM
// =========================================

addEmployeeButton.addEventListener(
    "click",

    function () {

        if (userRole !== "ADMIN") {

            return;

        }


        editingEmployeeId = null;

        employeeForm.reset();


        employeeFormTitle.textContent =
            "Add Employee";


        employeeFormSection.style.display =
            "block";


        employeeFormSection.scrollIntoView({

            behavior: "smooth",

            block: "start"

        });

    }
);


// =========================================
// CANCEL EMPLOYEE FORM
// =========================================

cancelEmployeeButton.addEventListener(
    "click",

    function () {

        employeeForm.reset();

        editingEmployeeId = null;

        employeeFormSection.style.display =
            "none";

    }
);


// =========================================
// SAVE EMPLOYEE
// =========================================

employeeForm.addEventListener(
    "submit",

    async function (event) {

        event.preventDefault();


        if (userRole !== "ADMIN") {

            alert(
                "You do not have permission to perform this action."
            );

            return;

        }


        const employee = {

            employeeCode:
                document.getElementById(
                    "employeeCode"
                ).value.trim(),

            firstName:
                document.getElementById(
                    "firstName"
                ).value.trim(),

            lastName:
                document.getElementById(
                    "lastName"
                ).value.trim(),

            email:
                document.getElementById(
                    "email"
                ).value.trim(),

            phone:
                document.getElementById(
                    "phone"
                ).value.trim(),

            department:
                document.getElementById(
                    "employeeDepartment"
                ).value.trim(),

            designation:
                document.getElementById(
                    "designation"
                ).value.trim(),

            salary:
                Number(
                    document.getElementById(
                        "salary"
                    ).value
                ),

            joiningDate:
                document.getElementById(
                    "joiningDate"
                ).value,

            status:
                document.getElementById(
                    "employeeStatus"
                ).value

        };


        try {

            let url =
                `${API_BASE_URL}/api/employees`;

            let method =
                "POST";


            if (editingEmployeeId !== null) {

                url +=
                    `/${editingEmployeeId}`;

                method =
                    "PUT";

            }


            const response =
                await fetch(

                    url,

                    {

                        method: method,

                        headers:
                            getAuthHeaders(),

                        body:
                            JSON.stringify(
                                employee
                            )

                    }

                );


            if (!response.ok) {

                const errorText =
                    await response.text();

                throw new Error(
                    errorText ||
                    "Failed to save employee"
                );

            }


            alert(
                editingEmployeeId !== null
                    ? "Employee updated successfully."
                    : "Employee added successfully."
            );


            employeeForm.reset();

            employeeFormSection.style.display =
                "none";

            editingEmployeeId =
                null;


            await loadEmployees();

            await loadDashboardStatistics();


        } catch (error) {

            console.error(
                "Error saving employee:",
                error
            );

            alert(
                "Failed to save employee. Please check the entered details."
            );

        }

    }
);


// =========================================
// EDIT EMPLOYEE
// =========================================

async function editEmployee(id) {

    if (userRole !== "ADMIN") {

        return;

    }


    try {

        const response =
            await fetch(

                `${API_BASE_URL}/api/employees/${id}`,

                {

                    method: "GET",

                    headers:
                        getAuthHeaders()

                }

            );


        if (!response.ok) {

            throw new Error(
                "Failed to fetch employee"
            );

        }


        const employee =
            await response.json();


        editingEmployeeId =
            employee.id;


        document.getElementById(
            "employeeCode"
        ).value =
            employee.employeeCode || "";


        document.getElementById(
            "firstName"
        ).value =
            employee.firstName || "";


        document.getElementById(
            "lastName"
        ).value =
            employee.lastName || "";


        document.getElementById(
            "email"
        ).value =
            employee.email || "";


        document.getElementById(
            "phone"
        ).value =
            employee.phone || "";


        document.getElementById(
            "employeeDepartment"
        ).value =
            employee.department || "";


        document.getElementById(
            "designation"
        ).value =
            employee.designation || "";


        document.getElementById(
            "salary"
        ).value =
            employee.salary || "";


        document.getElementById(
            "joiningDate"
        ).value =
            employee.joiningDate || "";


        document.getElementById(
            "employeeStatus"
        ).value =
            employee.status || "";


        employeeFormTitle.textContent =
            "Edit Employee";


        employeeFormSection.style.display =
            "block";


        employeeFormSection.scrollIntoView({

            behavior: "smooth",

            block: "start"

        });


    } catch (error) {

        console.error(
            "Error loading employee:",
            error
        );

        alert(
            "Failed to load employee details."
        );

    }

}


// =========================================
// DELETE EMPLOYEE
// =========================================

async function deleteEmployee(id) {

    if (userRole !== "ADMIN") {

        return;

    }


    const confirmed =
        confirm(
            "Are you sure you want to delete this employee?"
        );


    if (!confirmed) {

        return;

    }


    try {

        const response =
            await fetch(

                `${API_BASE_URL}/api/employees/${id}`,

                {

                    method: "DELETE",

                    headers:
                        getAuthHeaders()

                }

            );


        if (!response.ok) {

            throw new Error(
                "Failed to delete employee"
            );

        }


        alert(
            "Employee deleted successfully."
        );


        await loadEmployees();

        await loadDashboardStatistics();


    } catch (error) {

        console.error(
            "Error deleting employee:",
            error
        );

        alert(
            "Failed to delete employee."
        );

    }

}


// =========================================
// LOAD DASHBOARD STATISTICS
// =========================================

async function loadDashboardStatistics() {

    try {

        const response =
            await fetch(

                `${API_BASE_URL}/api/employees`,

                {

                    method: "GET",

                    headers:
                        getAuthHeaders()

                }

            );


        if (!response.ok) {

            throw new Error(
                "Failed to load statistics"
            );

        }


        const employees =
            await response.json();


        // Total employees

        totalEmployeesElement.textContent =
            employees.length;


        // Active employees

        const activeEmployees =
            employees.filter(
                employee =>
                    employee.status &&
                    employee.status.toUpperCase() ===
                    "ACTIVE"
            );


        activeEmployeesElement.textContent =
            activeEmployees.length;


        // Inactive employees

        const inactiveEmployees =
            employees.filter(
                employee =>
                    employee.status &&
                    employee.status.toUpperCase() ===
                    "INACTIVE"
            );


        inactiveEmployeesElement.textContent =
            inactiveEmployees.length;


        // Unique departments

        const departments =
            new Set(
                employees
                    .map(
                        employee =>
                            employee.department
                    )
                    .filter(
                        department =>
                            department
                    )
            );


        totalDepartmentsElement.textContent =
            departments.size;


    } catch (error) {

        console.error(
            "Error loading statistics:",
            error
        );

    }

}