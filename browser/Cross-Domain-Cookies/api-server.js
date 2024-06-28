const express = require("express");
const cors = require("cors");
const https = require("https");
const fs = require("fs");
const app = express();
const port = 8081;

// Load SSL key and certificate
const key = fs.readFileSync("SSL/server.key");
const cert = fs.readFileSync("SSL/server.crt");
const options = { key, cert };

// CORS configuration to allow requests from your client domain
app.use(
  cors({
    origin: "https://test.mydomain.com:8080",
    credentials: true,
  })
);

//https://test.api.com:8081

app.get("/", (req, res) => {
  console.log("Server Name:" + req.hostname);
  console.log("Referrer:" + req.get("Referer"));
  // Get the referer header
  const referer = req.get("Referer");
  if (referer) {
    // Extract the root domain from the referer
    const url = new URL(referer);
    const domain = url.hostname.split(".").slice(-2).join("."); // Get the root domain (e.g., mydomain.com)

    console.log("Cookie Domain:" + domain);

    // Set the cookie with the dynamic domain
    res.cookie("testCookie", "cookieValue", {
      httpOnly: true,
      secure: true, // Set to true for HTTPS
      sameSite: "Strict", // Required for cross-site cookies
      domain: `.${domain}`, // Set the domain dynamically
    });
    res.send(`HttpOnly cookie has been set for domain: .${domain}`);
  } else {
    res.status(400).send("Referer header not found");
  }
});

// Add error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send("Something broke!");
});

// Create HTTPS server
https.createServer(options, app).listen(port, () => {
  console.log(`API server running at https://test.api.com:${port}`);
});
