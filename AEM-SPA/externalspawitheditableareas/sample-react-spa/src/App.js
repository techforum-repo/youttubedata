import AEMText from './components/aem/AEMText';
import AEMImage from './components/aem/AEMImage';
import AEMTitle from './components/aem/AEMTitle';
import AEMPage from './components/aem/AEMPage';

function App() {
  return (
    <div> 
 
		  <h4>SPA Only Content  </h4>	
		  
		  <p>Sample Content local to SPA </p>		
		  <h4>Content From AEM starts</h4>
		  
		  <AEMTitle
			pagePath='/content/spaeditableareas/us/en/home' 
            itemPath='root/title'/>
		  
		  <AEMText            
			pagePath='/content/spaeditableareas/us/en/home' 
            itemPath='root/text'/>
			
		  <AEMImage
			pagePath='/content/spaeditableareas/us/en/home' 
            itemPath='root/image'/>
			
		 <h4>Content Enabled through AEM Container</h4>
		  
		  <AEMPage
			pagePath='/content/spaeditableareas/us/en/home'
			/>
		  
		  <h4>Content From AEM Ends</h4>
		  
		  <h4>SPA Only Content  </h4>		
		  <p>Sample Content local to SPA </p>	
	  
    </div>
  );
}

export default App;

