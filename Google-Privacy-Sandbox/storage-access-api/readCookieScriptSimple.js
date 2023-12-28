const cookiesText = document.querySelector("#cookies");

const refreshCookiesButton = document.querySelector("#refresh-cookies");
if (refreshCookiesButton) refreshCookiesButton.addEventListener('click', refreshCookies);


async function refreshCookies() {
  console.log('Read the document cookies: ', document.cookie);
  cookiesText.innerText = document.cookie || 'No cookies found';
}

cookiesText.innerText = document.cookie || 'No cookies found';