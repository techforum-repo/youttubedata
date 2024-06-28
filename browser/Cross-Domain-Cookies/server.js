const express = require("express");
const path = require("path");
const https = require("https");
const fs = require("fs");
const { createProxyMiddleware } = require("http-proxy-middleware");
const app = express();
const port = 8080;

// Load SSL key and certificate
const key = fs.readFileSync("SSL/server.key");
const cert = fs.readFileSync("SSL/server.crt");
const options = { key, cert };

// Serve static files from the "public" directory
app.use(express.static(path.join(__dirname, "public")));

// Ignore self-signed certificate errors (development only)
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

//https://test.mydomain.com:8080/api
// Proxy setup to forward API requests to the API server
app.use(
  "/api",
  createProxyMiddleware({
    //target: "https://test.api.com:8081",
	target: "https://test.api.com:8081",
    changeOrigin: true,
    pathRewrite: { "^/api": "" },
    secure: false, // Allow self-signed certificates
    /** on:{
		proxyRes: (proxyRes, req, res) => {
		
			 console.log('Inside onProxyRes...'); // Log original cookies
			
			const cookies = proxyRes.headers['set-cookie'];
			if (cookies) {
				console.log('Original cookies:', cookies); // Log original cookies
				proxyRes.headers['Set-Cookie'] = cookies.map(cookie => {
					const newCookie = cookie.replace(/Domain=[^;]+;/i, 'Domain=test.mydomain.com;');
					console.log('Modified cookie:', newCookie); // Log modified cookie
					return newCookie;
				});
			}
		}
    },**/ // if the target service can not set domain in appropriate client side root domain, the cookie need to be rewritten to the clinet side root domain from backend API domain using CDN(if supported) or another proxy server like Apache
    onError: (err, req, res) => {
      console.error("Proxy error:", err);
      res.status(500).send("Proxy error occurred");
    },
  })
);

// Endpoint to check for cookies
//https://test.mydomain.com:8080/check
app.get("/check", (req, res) => {
  const cookie = req.headers.cookie || "No cookies found";
  console.log("Cookies received:", cookie); // Add logging
  res.send(`Cookies received: ${cookie}`);
});

// Add error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send("Something broke!");
});

// Create HTTPS server
https.createServer(options, app).listen(port, () => {
  console.log(`Client server running at https://test.mydomain.com:${port}`);
});
