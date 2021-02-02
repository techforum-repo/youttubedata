const markerNameA = "example-marker-a";
const markerNameB = "example-marker-b";
var loops=1000000000;

var output = '';

performance.mark(markerNameA); 

while(loops)
{
    loops=loops-1;
}

performance.mark(markerNameB);
performance.measure("Measure measure1 to measure2", markerNameA, markerNameB); 

var usertimings = performance.getEntriesByType("measure");
usertimings.forEach((r) => {
    output+=`${r.name}<br/><br/>`; 
    output+=`Start Time: ${r.startTime }<br/>`;    
    output+=`Duration: ${r.duration }<br/>`;

    console.log(JSON.stringify(r.toJSON()));
});

performance.clearMarks();
performance.clearMeasures();

document.getElementById('userdemo').innerHTML = output;
