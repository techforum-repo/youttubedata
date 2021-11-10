import { withMappable, MapTo } from '@adobe/aem-react-editable-components';

import { TitleV2, TitleV2IsEmptyFn } from "@adobe/aem-core-components-react-base";

const RESOURCE_TYPE = "spaeditableareas/components/title";

const EditConfig = {    
    emptyLabel: "Title",  
    isEmpty: TitleV2IsEmptyFn, 
    resourceType: RESOURCE_TYPE 
};

// MapTo allows the AEM SPA Editor JS SDK to dynamically render components added to SPA Editor Containers
MapTo(RESOURCE_TYPE)(TitleV2, EditConfig);

// withMappable allows the component to be hardcoded into the SPA; <AEMTitle .../>
const AEMTitle = withMappable(TitleV2, EditConfig);

export default AEMTitle;