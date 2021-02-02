window.addEventListener('load', function() {
    var output='';
    var resources = window.performance.getEntriesByType("resource");
    resources.forEach((r) => {
        output+=`Details for ${r.name}<br/><br/>`;
        output+=`Entry Type: ${r.entryType}<br/>`;
        output+=`Initiation Type: ${r.initiatorType}<br/>`;
        output+=`Start Time: ${r.startTime}<br/>`  
        output+=`Connect time: ${r.responseEnd - r.responseStart}<br/>`;
        output+=`Duration: ${r.duration}<br/>`;
        output+=`Duration: ${r.nextHopProtocol}<br/>`;
        output+=`Domain Lookup: ${r.domainLookupEnd - r.domainLookupStart} <br/><br/>`;
      
        console.log(JSON.stringify(r.toJSON()));
    });
  
    document.getElementById('resourcedemo').innerHTML = output;
});
         