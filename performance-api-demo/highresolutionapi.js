var output='';
var loops=100; 

const startTime = performance.now();

output="Start time: " + startTime + "<br />";

while(loops)
{
    loops=loops-1;
}

const endTime = performance.now();

output+="End time: " + endTime + "<br />";
output+= "Duration: " + (endTime - startTime) + "<br />";
//Returns the high resolution timestamp of the start time of the performance measurement.
output+= `Navigation Start:  ${new Date(performance.timeOrigin).toUTCString()}<br />`;
output+= `Navigation Start + Now:  ${new Date(performance.timeOrigin + performance.now() ).toUTCString()} <br /> <br />`;

document.getElementById('highresdemo').innerHTML = output;
console.log(JSON.stringify(performance.toJSON())); 