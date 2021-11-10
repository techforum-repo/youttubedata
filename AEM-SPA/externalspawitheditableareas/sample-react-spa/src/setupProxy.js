const { createProxyMiddleware } = require('http-proxy-middleware');
const {REACT_APP_HOST_URI,REACT_APP_AUTHORIZATION} = process.env;

module.exports = function(app) {
	
	/**	
		Proxy the model request to AEM for development	
	**/
	
	 const toAEM = function(path, req) {
        return path.startsWith('/content') || path.endsWith('.model.json')
    }
	
	
	const pathRewriteToAEM = function (path, req) { 
        if (path === '/.model.json') {
            return '/content/spaeditableareas/us/en/home.model.json';
        }  
    }

    /**
    * Register the proxy middleware using the toAEM filter and pathRewriteToAEM rewriter 
    */
    app.use(
        createProxyMiddleware(
            toAEM, // Only route the configured requests to AEM
            {
                target: REACT_APP_HOST_URI,
                changeOrigin: true,
                // Pass in credentials when developing against an Author environment
                auth: REACT_APP_AUTHORIZATION,
                pathRewrite: pathRewriteToAEM // Rewrite SPA paths being sent to AEM
            }
        )
    );


    /**
    * Enable CORS on requests from the AEM to SPA for content Editing
    * 
    */
    app.use((req, res, next) => {
        res.header("Access-Control-Allow-Origin", REACT_APP_HOST_URI);
        next();
    });
};