// Set a hasAccess boolean variable which defaults to false
let hasAccess = false;

const cookiesText = document.querySelector("#cookies");
const errorText = document.querySelector("#error");

const refreshCookiesButton = document.querySelector("#refresh-cookies");
if (refreshCookiesButton)
  refreshCookiesButton.addEventListener("click", refreshCookies);

function updateCookiesOutput() {
  console.log("Read the document cookies:", document.cookie);
  cookiesText.innerText = document.cookie || "No cookies found";
}

// This function is called by the refreshCookiesButton
// It either already has access - in which case it can simply access the cookies
// Or it doesn't have access because it was waiting for a prompt
async function refreshCookies() {
  if (!hasAccess) {
    console.log(
      "Don't have access. Trying again within this click handler, in case it needed a prompt."
    );
    try {
      // This should now work if it was waiting a prompt
      await document.requestStorageAccess();
      hasAccess = true;  // Can assume this was true is above did not reject
      console.log("Have access now thanks to prompt");
      errorText.innerText = "";
    } catch (err) {
      console.log("requestStorageAccess Error:", err);
      errorText.innerText =
        "Permission denied. Either blocked by user or browser";
    }

    hasAccess = await document.hasStorageAccess();
    console.log("Updated hasAccess:", hasAccess);
  }
  updateCookiesOutput();
}

async function hasCookieAccess() {
  // Check if Storage Access API is supported
  if (!document.requestStorageAccess) {
    // Storage Access API is not supported so best we can do is
    // hope it's an older browser that doesn't block 3P cookies
    console.log("Storage Acccess API not supported. Assume we have access.")
    return true;
  }

  // Check if access has already been granted
  if (await document.hasStorageAccess()) {
    console.log("Cookie access already granted");
    return true;
  }

  // Check the storage-access permission
  try {
    const permission = await navigator.permissions.query({
      name: "storage-access",
    });
    console.log("permissions:", permission);

    if (permission.state === "granted") {         
        // Can just call requestStorageAccess() without a
        // user interaction and it will resolve automatically.
        try {
          console.log("Cookie access allowed. Calling requestStorageAccess()");
          await document.requestStorageAccess();
          console.log("Cookie access granted");
          return true;
        } catch (error) {
          // This shouldn't really fail if access is granted
          return false;
        }
      return true;
    } else if (permission.state === "prompt") {
        //If the permission status is "prompt" we need to call 
        //document.requestStorageAccess() from within a user gesture, such as a button click.
        //we will call this from refresh Cookie button
      console.log("Cookie access requires a prompt");
      return false;
    } else if (permission.state === "denied") {
       console.log("Cookie access denied");
      return false;
    }
  } catch (error) {
    // storage-access permission not supported. Assume false.
      console.log("storage-access permission not supported. Assume no access.");
    return false;
  }

  // By default return false, though should really be caught by one of above.
  return false;
}

// This function runs as page loads to try to give access initially
// This can make the click handler quicker as it doesn't need to
// await the access request if it's already happened.
async function handleCookieAccessInit() {
  hasAccess = await hasCookieAccess();

  updateCookiesOutput();
}

handleCookieAccessInit();