let map;
let infoWindow;
let service;

//Callback method - called after the Google Maps API library is loaded
function initMap() {

  //Hide default markers on the map
  var myStyles = [
    {
      featureType: "poi",
      elementType: "labels",
      stylers: [
        { visibility: "off" }
      ]
    }
  ];
  // Create the map.
  map = new google.maps.Map(document.getElementById('location-map'), {
    zoom: 13,//specifies the initial zoom level of the map
    center: { lng: -93.16689, lat: 44.80413 },//sets the initial center coordinates of the map 
    styles: myStyles,
  });

  // Load the stores GeoJSON onto the map.
  map.data.loadGeoJson('locations.json', { idPropertyName: 'locationid' });

  //Display unique marker icon specific to category

  const icons = {
    grocery: {
      url: 'grocery.png',
      scaledSize: new google.maps.Size(30, 40)
    },
    retailer: {
      url: 'retailer.png',
      scaledSize: new google.maps.Size(30, 40)
    }
  };

  map.data.setStyle(function (feature) {
    var type = feature.getProperty('type');
    var iconUrl = icons[type];

    return {
      icon: iconUrl
    };
  });

  
  infoWindow = new google.maps.InfoWindow();
  //Service to calculate the Distance and duration between user location and specific address
  service = new google.maps.DistanceMatrixService(); 

  // Show the information for a store when its marker is clicked.
  map.data.addListener('click', (event) => {
    const infoWindowContent = createInfoWindowContent(event.feature);
    const position = event.feature.getGeometry().get();

    infoWindow.setContent(infoWindowContent);
    infoWindow.setPosition(position);
    infoWindow.setOptions({ pixelOffset: new google.maps.Size(0, -30) });
    infoWindow.open(map);
  });


  // Listen for the Escape key on the document to close the Infowindow
  document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
      infoWindow.close();
    }
  });

}

//Display the address details from the locator.json to left side pane of the window
document.addEventListener('DOMContentLoaded', function () {
  fetch('locations.json')
    .then(response => response.json())
    .then(data => populateLocations(data))
    .catch(error => console.error('Error:', error));
});

function populateLocations(data) {
  var locationsList = document.getElementById('location-list');

  // Assuming findUserLocation and calculateDistance are defined and return Promises
  findUserLocation().then(userPosition => {
    const userLocation = new google.maps.LatLng(userPosition.lat, userPosition.lng);

    // Process each feature and calculate distances in sequence
    const promises = data.features.map(feature => {
      const location = feature.properties;
      const destination = new google.maps.LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0]);

      return calculateDistance(userLocation, destination).then(result => {
        // Create the container div for each location
        var locationDiv = document.createElement('div');
        locationDiv.className = 'location';

        // Create and append the name element
        var locationName = document.createElement('h3');
        locationName.textContent = location.name;
        locationName.style.cursor = 'pointer';
        locationDiv.appendChild(locationName);

        // Event listener for opening the info window
        locationName.addEventListener('click', function () {
          console.log('Opening info window for:', location.locationid);
          openInfoWindow(location.locationid);
        });

        // Create and append other details
        var locationDescription = document.createElement('p');
        locationDescription.textContent = location.description;
        locationDiv.appendChild(locationDescription);

        var locationAddress = document.createElement('p');
        locationAddress.innerHTML = `<strong>Address:</strong> ${location.address}`;
        locationDiv.appendChild(locationAddress);

        var locationHours = document.createElement('p');
        locationHours.innerHTML = `<strong>Hours:</strong> ${location.hours}`;
        locationDiv.appendChild(locationHours);

        var locationPhone = document.createElement('p');
        locationPhone.innerHTML = `<strong>Phone:</strong> ${location.phone}`;
        locationDiv.appendChild(locationPhone);

        var distanceandduration = document.createElement('p');
        distanceandduration.innerHTML = `<strong>Distance:</strong> ${result.distance}, <strong>Duration:</strong> ${result.duration}`;
        locationDiv.appendChild(distanceandduration);

        // Append the locationDiv to the locations list
        locationsList.appendChild(locationDiv);
      });
    });

    // Wait for all distances to be processed
    return Promise.all(promises);
  })
  .catch(error => {
    console.error('Error finding user location or calculating distance:', error);
  });
}

//Open Info Window on Click of Marker
function openInfoWindow(locationId) {
  console.log("openInfoWindow called with locationId:", locationId); // Debugging
  map.data.forEach(function (feature) {
    if (feature.getProperty('locationid') === locationId) {
      const infoWindowContent = createInfoWindowContent(feature);
      const position = feature.getGeometry().get();

      infoWindow.setContent(infoWindowContent);
      infoWindow.setPosition(position);
      infoWindow.setOptions({ pixelOffset: new google.maps.Size(0, -30) });
      infoWindow.open(map);
    }
  });
}

//Create Content for Info Window based on the feature data from locations.json
function createInfoWindowContent(feature) {
  const name = feature.getProperty('name');
  const description = feature.getProperty('description');
  const hours = feature.getProperty('hours');
  const address = feature.getProperty('address');
  const website = feature.getProperty('website');
  const phone = feature.getProperty('phone');

  return `<div class="map-info-window">
              <p class="map-info-window-store-name">${name}</p>
              <p><b>${description}</b></p>
              <p><b>Open:</b> ${hours}<br>
                  <b>Phone:</b> ${phone} <br>
                  <b>Address:</b> ${address}<br>
              </p>
              <a class="button" target="_blank" href="https://www.google.com/maps/dir/?api=1&destination=${address}">Directions</a>
              <a class="button" target="_blank" href="${website}">Website</a>
          </div>`;
}

//To demo seeting the map center based on the specific address or users current location
document.addEventListener('DOMContentLoaded', function () {
  var changeMapCenterButton = document.getElementById('changemapcenter');
  changeMapCenterButton.addEventListener('click', function () {

    centerToAddress('7835 150th St W, Apple Valley, MN 55124');
    //centerToUserLocation();

  });
});

//Find the lat and lng for a address and center the map to that
function centerToAddress(address) {
  const geocoder = new google.maps.Geocoder();
  geocoder.geocode(
    { address: address, componentRestrictions: { country: "us" } },
    function (results, status) {
      if (status === "OK") {
        console.log("Geocode results:", results); // Log results
        var lat = results[0].geometry.location.lat();
        var lng = results[0].geometry.location.lng();

        var myLatLng = new google.maps.LatLng(lat, lng);
        map.setCenter(myLatLng);
      } else {
        console.log("Geocode was not successful for the following reason:", status);
      }
    }
  );
}

//Find the lat and lng for the users current location and center the map to that
function centerToUserLocation() {
  findUserLocation()
    .then(userPosition => {
      var myLatLng = new google.maps.LatLng(userPosition.lat, userPosition.lng);
      map.setCenter(myLatLng);
    })
    .catch(error => {
      // Handle any errors here
      console.error(error);
    });
}

function findUserLocation() {
  // Return a new promise
  return new Promise((resolve, reject) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        function (position) {
          const userPosition = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
          resolve(userPosition); // Resolve the promise with the position
        },
        function (error) {
          reject(error); // Reject the promise if there's an error
        }
      );
    } else {
      reject("Geolocation is not supported by this browser.");
    }
  });
}


// Define a function that returns a promise
function getDistanceMatrix(origin, destination) {
  return new Promise((resolve, reject) => {
    service.getDistanceMatrix(
      {
        origins: [origin],
        destinations: [destination],
        travelMode: 'DRIVING',
        unitSystem: google.maps.UnitSystem.IMPERIAL
      },
      (response, status) => {
        if (status === 'OK') {
          resolve(response);
        } else {
          reject('Distance Matrix request failed due to ' + status);
        }
      }
    );
  });
}
function calculateDistance(origin, destination) {
  // Return a new promise
  return new Promise((resolve, reject) => {
    getDistanceMatrix(origin, destination)
      .then(response => {
        // Process the response here
        const distance = response.rows[0].elements[0].distance.text;
        const duration = response.rows[0].elements[0].duration.text;

        // Resolve the promise with the distance and duration
        resolve({ distance, duration });
      })
      .catch(error => {
        // Reject the promise if there's an error
        reject(error);
      });
  });
}




