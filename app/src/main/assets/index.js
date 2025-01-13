document.addEventListener("DOMContentLoaded", () => {
  // Query all path elements
  const paths = document.querySelectorAll("path");

  // Variable to track the previously clicked path
  let previouslyClickedPath = null;

  // Iterate over each path element
  paths.forEach((path) => {
    // Add click event listener to change the color
    path.addEventListener("click", (event) => {
      // Reset the style of the previously clicked path (if any)
      if (previouslyClickedPath) {
        previouslyClickedPath.style.fill = ""; // Reset fill
        previouslyClickedPath.style.stroke = ""; // Reset stroke
        previouslyClickedPath.style.strokeWidth = ""; // Reset stroke width
      }

      // Get the ID of the clicked path
      const pathId = event.target.id;
      event.target.style.fill = "#FF6347";
      console.log(`Clicked path with ID: ${pathId}`);

      // Use the pathId to find the corresponding circle element
      const circle = document.querySelector(`circle[id="${pathId}"]`);

      // If a circle element with the matching ID is found, get its class name
      if (circle) {
        const className = circle.getAttribute("class");
        console.log(`Circle corresponding to path ID: ${pathId} has class: ${className}`);
        Android.onPathClicked(pathId, className);
      }

      // Update the previously clicked path
      previouslyClickedPath = event.target;
    });

    // Add double-click event listener to rollback the color change
    path.addEventListener("dblclick", (event) => {
      event.target.style.fill = ""; // Reset fill color
      event.target.style.stroke = ""; // Reset stroke color
      event.target.style.strokeWidth = ""; // Reset stroke width
      console.log(`Double-clicked path with ID: ${event.target.id}, color reset`);
    });
  });
});
