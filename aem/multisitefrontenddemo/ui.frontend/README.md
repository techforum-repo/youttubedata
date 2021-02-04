# Frontend Build - Multi theme Support

This module is build based on aem arch type 24, refer https://github.com/adobe/aem-project-archetype/tree/master/src/main/archetype/ui.frontend.general  for more detail and the prerequisite.

Support for multiple tenenats/themes

## Configurations - Multi Site

Create multiple sites under ui.frontend\src\main\webpack with the default content inside webpack folder.

### webpack-common.js

Entry for all the sites

```
entry: {
	site1: SOURCE_ROOT + SITE_1 +'/site/main.ts',
	site2: SOURCE_ROOT + SITE_2 +'/site/main.ts'
    },
    output: {
        filename: (chunkData) => {
            return chunkData.chunk.name === 'dependencies' ? 'clientlib-dependencies/[name].js' : 'clientlib-[name]/[name].js';
        },
        path: path.resolve(__dirname, 'dist')
    }
	
	plugins: [
		new CleanWebpackPlugin(),
		new webpack.NoEmitOnErrorsPlugin(),
		new MiniCssExtractPlugin({
		filename: 'clientlib-[name]/[name].css'
        }),
        new CopyWebpackPlugin([
			{ from: path.resolve(__dirname, SOURCE_ROOT + SITE_1 +'/resources/'), to: './clientlib-site1'},
			{from: path.resolve(__dirname, SOURCE_ROOT + SITE_2 +'/resources/'), to: './clientlib-site2'}
        ])
    ]
```
	
### webpack.dev.js - Static file support for multiple sites

```
 plugins: [
            
            new HtmlWebpackPlugin({
                inject: true,
                template: path.resolve(__dirname, SOURCE_ROOT + '/site1/static/index.html'),
                filename: 'site1.html',
                chunks: ['site1']
            }),
            new HtmlWebpackPlugin({
                inject: true,
                template: path.resolve(__dirname, SOURCE_ROOT + '/site2/static/index.html'),
                filename: 'site2.html',
                chunks: ['site2']
            })
        ]
```
		
### package.json - using aemfed(aem-sync) and watch

```
"scripts": {
    "dev": "webpack -d --env dev --config ./webpack.dev.js && clientlib --verbose",
    "prod": "webpack -p --config ./webpack.prod.js && clientlib --verbose",
    "start": "webpack-dev-server --open --config ./webpack.dev.js",
    "aem-sync": "aemfed -t http://admin:admin@localhost:4502 -w ../ui.apps/src/main/content/jcr_root/",
    "watch": "watch \"npm run dev\" src"
  }
```
  
### clientlib.config.js - Support client library for different sites

```
//site1

{
  ...libsBaseConfig,
  name: 'clientlib-site1',
  categories: ['site1.site'],
  dependencies: ['site1.dependencies'],
  assets: {
	// Copy entrypoint scripts and stylesheets into the respective ClientLib
	// directories
	js: {
	  cwd: 'clientlib-site1',
	  files: ['**/*.js'],
	  flatten: false
	},
	css: {
	  cwd: 'clientlib-site1',
	  files: ['**/*.css'],
	  flatten: false
}

//site2
    {
      ...libsBaseConfig,	
      name: 'clientlib-dependencies-site2',
      categories: ['site2.dependencies'],
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: 'clientlib-dependencies-site2',
          files: ['**/*.js'],
          flatten: false
        },
        css: {
          cwd: 'clientlib-dependencies-site2',
          files: ['**/*.css'],
          flatten: false
        }
      }
   }
``` 

## Installation

Navigate to `ui.frontend` in your project and run `npm install`

## Testing

### Webpack DevServer - Static Markup

Navigate to `ui.frontend` in your project and run `npm start`

Now the indivual site static files can be access through(site specific local css/js files are injected to the site specific html)

http://localhost:8080/site1.html
http://localhost:8080/site2.html

The site specific style/script changes will reflect immediately to the browser.


### Watch & aemfed

Navigate to `ui.frontend` in your project and run `npm run watch`
Navigate to `ui.frontend` in your project in another terminal and run `npm run aem-sync`

Now the style/script changes are synced immediately to the AEM server.

The AEM server URL is proxied through http://localhost:3000/ e.g http://localhost:3000/editor.html/content/multisitefrontenddemo/us/en.html(the changes reflect immediately to the browser)
