
import React from 'react';
import useGraphQL from '../api/useGraphQL';
import Error from './Error';
import Loading from './Loading';

function SampleData(props) {

   const contentFragmentPath = '/content/dam/sampledata';

   const { data, errorMessage } = useGraphQL(sampleDataQuery(contentFragmentPath));

   if(errorMessage) return <Error errorMessage={errorMessage} />;

   if(!data) return <Loading />;

   let sampleData = data.sampledataByPath.item;

   if(!sampleData  ) {	   
	    return (
        <div className="sample-data">
			<Error errorMessage="Missing data, data could not be rendered." />			
        </div>
      );    
   }   
    return (
        <div className="sample-data">
			<div className="sample-data-details">{sampleData.data1}</div>
			<div className="sample-data-details">{sampleData.data2}</div>			
        </div>
      );
} 	
    
function sampleDataQuery(_path) {
  return `{
    sampledataByPath (_path: "${_path}") {
      item {
       data1
       data2
      }
    }
  }
  `;
}


export default SampleData;
