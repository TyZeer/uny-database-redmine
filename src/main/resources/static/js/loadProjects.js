document.addEventListener("DOMContentLoaded", () => {
    const projectDropdown = document.getElementById("projectId");

    // Function to fetch projects from the server
    const fetchProjects = async () => {
        try {
            const response = await fetch("/projects");
            if (!response.ok) {
                throw new Error("Failed to fetch projects. Please try again.");
            }
            const html = await response.text();
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, "text/html");

            // Extract project options
            const projects = Array.from(doc.querySelectorAll("[name='project_id'] option"))
                .map(option => ({
                    value: option.value,
                    text: option.textContent
                }));

            populateProjectDropdown(projects);
        } catch (error) {
            console.error(error);
            alert("Could not load projects. Check your connection or contact support.");
        }
    };

    // Populate the project dropdown with available projects
    const populateProjectDropdown = (projects) => {
        projectDropdown.innerHTML = ""; // Clear existing options
        if (projects.length > 0) {
            projects.forEach(project => {
                const option = document.createElement("option");
                option.value = project.value;
                option.textContent = project.text;
                projectDropdown.appendChild(option);
            });
        } else {
            const noProjectsOption = document.createElement("option");
            noProjectsOption.value = "";
            noProjectsOption.textContent = "No projects available";
            noProjectsOption.disabled = true;
            projectDropdown.appendChild(noProjectsOption);
        }
    };

    // Call the fetch function on page load
    fetchProjects();
});
