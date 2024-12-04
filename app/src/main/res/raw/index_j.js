document.addEventListener("DOMContentLoaded", () => {
  const paths = document.querySelectorAll("path");
  paths.forEach((path) => {
    path.addEventListener("click", (event) => {
      const clickedId = event.target.id; // Get the ID of the clicked path
      event.target.style.fill = "yellow"; // Change the path's fill color
      // Send the ID back to the Android app
      Android.onPathClicked(clickedId);
    });
  });
});
