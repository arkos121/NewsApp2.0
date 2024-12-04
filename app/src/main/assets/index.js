document.addEventListener("DOMContentLoaded", () => {
  // Query all path elements
  const paths = document.querySelectorAll("path");

  // Iterate over each path element
  paths.forEach((path) => {
    path.addEventListener("click", (event) => {
      // Get the ID of the clicked path
      const pathId = event.target.id;
      event.target.style.fill = "red"
       event.target.style.stroke = "black";  // Set stroke (border) to black
               event.target.style.strokeWidth = "1";  //
      console.log(`Clicked path with ID: ${pathId}`);

      // Use the pathId to find the corresponding circle element
      const circle = document.querySelector(`circle[id="${pathId}"]`);

      // If a circle element with the matching ID is found, get its class name
      if (circle) {
        const className = circle.getAttribute("class");
        console.log(`Circle corresponding to path ID: ${pathId} has class: ${className}`);
         Android.onPathClicked(pathId,className)
      }

      // Optional: You can also modify the circle, like changing the fill color
//      if (circle) {
//        circle.style.fill = "red";
//      }

    });
  });
});
