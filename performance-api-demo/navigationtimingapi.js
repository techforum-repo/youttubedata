var nvigationdemo = document.getElementById('nvigationdemo');
window.addEventListener('load', function() {
    var output = '';
    var navigations = window.performance.getEntriesByType("navigation");
    navigations.forEach((r) => {
        output+=`Details for ${r.name}<br/><br/>`;
        output+=`Entry Type: ${r.entryType}<br/>`;
        output+=`Type: ${r.type}<br/>`;
        output+=`Transfer Size: ${r.transferSize}<br/>`    
        output+=`Start Time: ${r.startTime}<br/>`     
        output+=`Redirect Count: ${r.redirectCount}<br/>`;
        output+=`Domain Lookup: ${r.domainLookupEnd - r.domainLookupStart} <br/>`;;
        output+=`Connect time: ${r.responseEnd - r.responseStart}<br/><br/>`;

         console.log(JSON.stringify(r.toJSON()));
    });
  
    document.getElementById('nvigationdemo').innerHTML = output;
   
});
         