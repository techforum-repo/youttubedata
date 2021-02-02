window.addEventListener('load', function() {
    var output='';
    var paints = window.performance.getEntriesByType("paint");
    paints.forEach((r) => {
        output+=`${r.name}  ${r.startTime}<br/><br/>`;    
        
        console.log(JSON.stringify(r.toJSON()));
    });

    document.getElementById('paintdemo').innerHTML = output;
});


    