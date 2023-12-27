function getCookies() {
    const cookies = document.cookie.split("; ");
    
    if (cookies.length > 0) {
        console.log("All Cookies - Embed:");
        cookies.forEach(cookie => {
            console.log(cookie);
        });
    } else { 
        console.log("No cookies found.");
    }
}


function doSetCookies() {
    document.cookie = '__Host-embedsitea=test value; Secure; Path=/; SameSite=None;Partitioned';
    document.cookie = 'unpartitioned-cookie=test; Secure; Max-Age=84600; Path=/; SameSite=None';
    getCookies();
}

// Call doSetCookies when the page loads
window.onload = doSetCookies;